/*
 * MTViewer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2Lib.mtFilm.loadFilmlist;


import de.p2tools.p2Lib.mtFilm.film.FilmData;
import de.p2tools.p2Lib.mtFilm.film.Filmlist;
import de.p2tools.p2Lib.mtFilm.film.FilmlistFactory;
import de.p2tools.p2Lib.mtFilm.readWriteFilmlist.ReadFilmlist;
import de.p2tools.p2Lib.mtFilm.readWriteFilmlist.WriteFilmlistJson;
import de.p2tools.p2Lib.mtFilm.tools.LoadFactoryConst;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LoadFilmlist {

    private static final AtomicBoolean stop = new AtomicBoolean(false); // damit kann das Laden gestoppt werden kann
    private final HashSet<String> hashSet;
    private final Filmlist filmListDiff;
    private final Filmlist filmListNew;
    private final ImportNewFilmlistFromServer importNewFilmlisteFromServer;
    private final NotifyProgress notifyProgress = new NotifyProgress();

    public LoadFilmlist() {
        filmListDiff = new Filmlist();
        filmListNew = new Filmlist();
        hashSet = new HashSet<>();

        importNewFilmlisteFromServer = new ImportNewFilmlistFromServer();
    }

    public void addListenerLoadFilmlist(ListenerLoadFilmlist listener) {
        notifyProgress.listeners.add(ListenerLoadFilmlist.class, listener);
    }

    public void setStart(ListenerFilmlistLoadEvent event) {
        notifyProgress.notifyEvent(NotifyProgress.NOTIFY.START, event);
    }

    public void setProgress(ListenerFilmlistLoadEvent event) {
        notifyProgress.notifyEvent(NotifyProgress.NOTIFY.PROGRESS, event);
    }

    public void setLoaded(ListenerFilmlistLoadEvent event) {
        notifyProgress.notifyEvent(NotifyProgress.NOTIFY.LOADED, event);
    }

    public void setFinished(ListenerFilmlistLoadEvent event) {
        notifyProgress.notifyEvent(NotifyProgress.NOTIFY.FINISHED, event);
    }

    public void setFinished() {
        notifyProgress.notifyFinishedOk();
    }

    public synchronized boolean isStop() {
        return stop.get();
    }

    public synchronized void setStop(boolean set) {
        stop.set(set);
    }

    /**
     * Filmliste beim Programmstart laden
     */
    public void loadFilmlistProgStart(boolean firstProgramStart, String filmListFile, boolean loadOnStartUp) {
        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            loadFilmlistStart(logList, firstProgramStart, filmListFile, loadOnStartUp);
            PLog.addSysLog(logList);
        }).start();
    }

    public void loadNewFilmlist(boolean alwaysLoadNew, String filmListFile) {
        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            loadNewFilmlistFromServer(logList, alwaysLoadNew, filmListFile);
            PLog.addSysLog(logList);

        }).start();
    }

    /**
     * Filmliste beim Programmstart laden
     */
    private void loadFilmlistStart(List<String> logList, boolean firstProgramStart, String filmListFile, boolean loadOnStartUp) {
        PDuration.counterStart("LoadFilmlist.loadFilmlistStart");
        // Start des Ladens, gibts keine Fortschrittsanzeige und kein Abbrechen
        if (LoadFactory.checkAllSenderSelectedNotToLoad()) {
            // alle Sender sind vom Laden ausgenommen
            return;
        }

        PDuration.onlyPing("Programmstart Filmliste laden: start");
        startMsg();
        setStart(new ListenerFilmlistLoadEvent("", "gespeicherte Filmliste laden",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false));

        filmListNew.setMeta(LoadFactoryConst.filmlist);
        filmListNew.setAll(LoadFactoryConst.filmlist);

        if (!firstProgramStart) {
            // gespeicherte Filmliste laden, macht beim ersten Programmstart keinen Sinn
            loadStoredList(logList, filmListNew, filmListFile);
            PDuration.onlyPing("Programmstart Filmliste laden: geladen");
        }

        if (filmListNew.isTooOld() && loadOnStartUp) {
            //eine neue Filmliste laden wenn die gespeicherte zu alt ist
            final String text;
            logList.add("Filmliste zu alt, neue Filmliste laden");
            logList.add("Alter|min]: " + filmListNew.getAge() / 60);
            text = "Filmliste ist zu alt, eine neue downloaden";
            logList.add(PLog.LILNE3);

            setProgress(new ListenerFilmlistLoadEvent("", text,
                    ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

            PDuration.onlyPing("Programmstart Filmliste laden: neue Liste laden");
            loadNewList(logList, false, true, filmListFile);
            PDuration.onlyPing("Programmstart Filmliste laden: neue Liste geladen");
        }

        setLoaded(new ListenerFilmlistLoadEvent("", "Filme verarbeiten",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        afterLoading(logList);
        PDuration.counterStop("LoadFilmlist.loadFilmlistStart");
    }

    // #######################################
    // #######################################

    private void loadNewFilmlistFromServer(List<String> logList, boolean alwaysLoadNew, String filmListFile) {
        PDuration.counterStart("LoadFilmlist.loadNewFilmlistFromServer");
        // damit wird eine neue Filmliste (Web) geladen UND auch gleich im Config-Ordner gespeichert
        if (LoadFactory.checkAllSenderSelectedNotToLoad()) {
            // alle Sender sind vom Laden ausgenommen
            return;
        }

//        progData.maskerPane.setButtonVisible(true);
        PDuration.onlyPing("Filmliste laden: start");
        startMsg();
        logList.add("");
        logList.add("Alte Liste erstellt  am: " + LoadFactoryConst.filmlist.genDate());
        logList.add("           Anzahl Filme: " + LoadFactoryConst.filmlist.size());
        logList.add("           Anzahl  Neue: " + LoadFactoryConst.filmlist.countNewFilms());
        logList.add(" ");

        loadNewList(logList, alwaysLoadNew, false, filmListFile);
        afterLoading(logList);
        PDuration.counterStop("LoadFilmlist.loadNewFilmlistFromServer");
    }

    private void afterLoading(List<String> logList) {
        logList.add("");
        logList.add("Jetzige Liste erstellt am: " + filmListNew.genDate());
        logList.add("  Anzahl Filme: " + filmListNew.size());
        logList.add("  Anzahl Neue:  " + filmListNew.countNewFilms());
        logList.add("");
        logList.add(PLog.LILNE2);
        logList.add("");

        setLoaded(new ListenerFilmlistLoadEvent("", "Filme markieren, Themen suchen",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        logList.add("Filme markieren");
        final int count = filmListNew.markFilms();
        logList.add("Anzahl doppelte Filme: " + count);

        filmListNew.loadSender();

        setLoaded(new ListenerFilmlistLoadEvent("", "Filme in Downloads eingetragen",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        logList.add("Filme in Downloads eingetragen");
//        progData.downloadList.addFilmInList(filmListNew);

        //die FilmList wieder füllen
        logList.add("==> und jetzt die Filmliste wieder füllen :)");
        LoadFactoryConst.filmlist.metaData = filmListNew.metaData;
        LoadFactoryConst.filmlist.sender = filmListNew.sender;
        LoadFactoryConst.filmlist.addAll(filmListNew);
        filmListNew.clear();

        stopMsg();
        setFinished();
    }

    private void loadStoredList(List<String> logList, Filmlist filmlist, String filmListFile) {
        new ReadFilmlist().readFilmlist(logList, filmListFile, filmlist);
    }

    private void loadNewList(List<String> logList, boolean alwaysLoadNew, boolean intern, String filmListFile) {
        if (intern) {
            // Hash mit URLs füllen
            fillHash(logList, filmListNew);

        } else {
            // Hash mit URLs füllen
            fillHash(logList, LoadFactoryConst.filmlist);

            filmListNew.setMeta(LoadFactoryConst.filmlist);
            if (!alwaysLoadNew) {
                //dann die alte Filmliste nicht löschen, aber erst nach dem Hash!!
                filmListNew.addAll(LoadFactoryConst.filmlist);
            }
            LoadFactoryConst.filmlist.clear();
        }

        setStop(false);
        logList.add("Filmliste laden (auto)");
        // Filmliste laden und Url automatisch ermitteln
        importNewFilmlisteFromServer.importFilmListAuto(logList, filmListNew, filmListDiff);
        afterImportNewFilmlistFromServer(logList, filmListFile);
    }

    private void startMsg() {
        PLog.addSysLog("");
        PLog.sysLog(PLog.LILNE1);
        PLog.addSysLog("Filmliste laden");
    }

    private void stopMsg() {
        PLog.addSysLog("Filmliste geladen");
        PLog.sysLog(PLog.LILNE1);
        PLog.addSysLog("");
    }

    /**
     * wird nach dem Import einer neuen Liste gemacht
     */
    private void afterImportNewFilmlistFromServer(List<String> logList, String filmListFile) {
        logList.add(PLog.LILNE3);

        if (!filmListDiff.isEmpty()) {
            // wenn nur ein Update
            filmListNew.updateList(filmListDiff, true/* Vergleich über Index, sonst nur URL */, true /* ersetzen */);
            filmListNew.metaData = filmListDiff.metaData;
            filmListNew.sort(); // jetzt sollte alles passen
            filmListDiff.clear();
        }

        logList.add("Neue Filme markieren");
        findAndMarkNewFilms(logList, filmListNew);

        logList.add("Unicode-Zeichen korrigieren");
        FilmlistFactory.cleanFaultyCharacterFilmlist(filmListNew);

        logList.add("Diacritics setzen/ändern, Diacritics suchen");
        FilmlistFactory.setDiacritic(filmListNew, false);

        logList.add("");
        logList.add("Filme schreiben (" + filmListNew.size() + " Filme) :");
        logList.add("   --> Start Schreiben nach: " + filmListFile);
        new WriteFilmlistJson().write(filmListFile, filmListNew);
        logList.add("   --> geschrieben!");
        logList.add("");
    }

    private void fillHash(List<String> logList, Filmlist filmlist) {
        logList.add(PLog.LILNE3);
        logList.add("Hash füllen, Größe vorher: " + hashSet.size());

        hashSet.addAll(filmlist.stream().map(FilmData::getUrlHistory).collect(Collectors.toList()));
        logList.add("                  nachher: " + hashSet.size());
        logList.add(PLog.LILNE3);
    }

    private void findAndMarkNewFilms(List<String> logList, Filmlist filmlist) {
        filmlist.stream() //genauso schnell wie "parallel": ~90ms
                .peek(film -> film.setNewFilm(false))
                .filter(film -> !hashSet.contains(film.getUrlHistory()))
                .forEach(film -> {
                    film.setNewFilm(true);
                });

        hashSet.clear();
    }
}