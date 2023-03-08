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

package de.p2tools.p2lib.mtfilm.loadfilmlist;


import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.FilmFactory;
import de.p2tools.p2lib.mtfilm.film.Filmlist;
import de.p2tools.p2lib.mtfilm.film.FilmlistFactory;
import de.p2tools.p2lib.mtfilm.readwritefilmlist.ReadFilmlist;
import de.p2tools.p2lib.mtfilm.readwritefilmlist.WriteFilmlistJson;
import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
    private BooleanProperty propLoadFilmlist = new SimpleBooleanProperty(false);

    public LoadFilmlist() {
        this.filmListDiff = new Filmlist();
        this.filmListNew = new Filmlist();
        hashSet = new HashSet<>();
        importNewFilmlisteFromServer = new ImportNewFilmlistFromServer();
    }

    public LoadFilmlist(Filmlist filmlistNew, Filmlist filmlistDiff) {
        this.filmListDiff = filmlistDiff;
        this.filmListNew = filmlistNew;
        hashSet = new HashSet<>();
        importNewFilmlisteFromServer = new ImportNewFilmlistFromServer();
    }

    public void addListenerLoadFilmlist(ListenerLoadFilmlist listener) {
        notifyProgress.listeners.add(ListenerLoadFilmlist.class, listener);
        System.out.println("======>" + notifyProgress.listeners.getListenerCount());
    }

    public void removeListenerLoadFilmlist(ListenerLoadFilmlist listener) {
        notifyProgress.listeners.remove(ListenerLoadFilmlist.class, listener);
        System.out.println("======>" + notifyProgress.listeners.getListenerCount());
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
    public void loadFilmlistProgStart() {
        setPropLoadFilmlist(true);
        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            PDuration.counterStart("loadFilmlistProgStart");

            logList.add("## " + PLog.LILNE1);
            logList.add("## Filmliste beim **Programmstart** laden - start");
            loadFilmlistProgStart(logList);
            logList.add("## Filmliste beim Programmstart laden - ende");
            logList.add("## " + PLog.LILNE1);
            PLog.addSysLog(logList);

            setPropLoadFilmlist(false);
            PDuration.counterStop("loadFilmlistProgStart");
        }).start();
    }

    public void loadNewFilmlistFromWeb(boolean alwaysLoadNew, String localFilmListFile) {
        setPropLoadFilmlist(true);
        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            PDuration.counterStart("loadNewFilmlistFromWeb");

            logList.add("## " + PLog.LILNE1);
            logList.add("## Filmliste aus dem Web laden - start");
            loadNewFilmlistFromWeb(logList, alwaysLoadNew, localFilmListFile);
            logList.add("## Filmliste aus dem Web laden - ende");
            logList.add("## " + PLog.LILNE1);
            PLog.addSysLog(logList);

            setPropLoadFilmlist(false);
            PDuration.counterStop("loadNewFilmlistFromWeb");
        }).start();
    }

    public boolean getPropLoadFilmlist() {
        return propLoadFilmlist.get();
    }

    public BooleanProperty propLoadFilmlistProperty() {
        return propLoadFilmlist;
    }

    public void setPropLoadFilmlist(boolean propLoadFilmlist) {
        this.propLoadFilmlist.set(propLoadFilmlist);
    }

    /**
     * Filmliste beim Programmstart laden
     */
    private void loadFilmlistProgStart(List<String> logList) {
        //hier wird die gespeicherte Filmliste geladen und wenn zu alt, wird eine neue aus
        //dem Web geladen

        // Start des Ladens, gibts keine Fortschrittsanzeige und kein Abbrechen
        if (LoadFactory.checkAllSenderSelectedNotToLoad()) {
            // alle Sender sind vom Laden ausgenommen
            logList.add("## Es sind keine Sender eingeschaltet!!");
            return;
        }

        setStart(new ListenerFilmlistLoadEvent("gespeicherte Filmliste laden",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false));

        filmListNew.setMeta(LoadFactoryConst.filmlist);
        filmListNew.setAll(LoadFactoryConst.filmlist);

        if (LoadFactoryConst.firstProgramStart) {
            //gespeicherte Filmliste laden, macht beim ersten Programmstart keinen Sinn
            logList.add("## Erster Programmstart -> Liste aus dem Web laden");
        } else {
            if (LoadFactoryConst.loadNewFilmlistOnProgramStart) {
                //dann bei Bedarf, eine neue Liste aus dem Web laden
                if (FilmlistFactory.isTooOld(LoadFactoryConst.dateStoredFilmlist)) {
                    //und wenn zu alt und neue soll beim ProgStart geladen werden, dann gleich weiter
                    logList.add("## Gespeicherte Filmliste zu alt, gleich Neue aus dem Web laden");

                } else {
                    logList.add("## Gespeicherte Filmliste nicht zu alt, gespeicherte laden");
                    loadStoredList(logList, filmListNew, LoadFactoryConst.localFilmListFile);
                    if (filmListNew.isEmpty()) {
                        //dann ist sie leer
                        logList.add("## Gespeicherte Filmliste ist leer, neue Filmliste aus dem Web laden");
                        logList.add("## Alter|min]: " + filmListNew.getAge() / 60);
                        logList.add("## " + PLog.LILNE3);
                    }
                }

                if (filmListNew.isEmpty()) {
                    //dann war sie zu alt oder ist leer
                    setProgress(new ListenerFilmlistLoadEvent("Filmliste ist zu alt, eine neue laden",
                            ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
                    logList.add("## Neue Liste aus dem Web laden");
                    loadNewFilmlistFromWeb(logList, false, true, LoadFactoryConst.localFilmListFile);
                    PDuration.onlyPing("Programmstart: Neu Filmliste aus dem Web geladen");
                }

            } else {
                //Keine neue Liste aus dem Web beim Programmstart, immer gespeicherte Liste laden
                logList.add("## Beim Programmstart soll keine neue Liste geladen werden");
                logList.add("## Gespeicherte Liste laden");
                loadStoredList(logList, filmListNew, LoadFactoryConst.localFilmListFile);
            }
        }

        setLoaded(new ListenerFilmlistLoadEvent("Filme verarbeiten",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        afterLoading(logList);
    }

    // #######################################
    // #######################################

    private void loadNewFilmlistFromWeb(List<String> logList, boolean alwaysLoadNew, String localFilmListFile) {
        //damit wird eine neue Filmliste (Web) geladen UND auch gleich im Config-Ordner gespeichert
        if (LoadFactory.checkAllSenderSelectedNotToLoad()) {
            // alle Sender sind vom Laden ausgenommen
            logList.add("## Es sind keine Sender eingeschaltet!!");
            return;
        }

        logList.add("##");
        logList.add("## Alte Liste erstellt  am: " + LoadFactoryConst.filmlist.genDate());
        logList.add("##            Anzahl Filme: " + LoadFactoryConst.filmlist.size());
        logList.add("##            Anzahl  Neue: " + LoadFactoryConst.filmlist.countNewFilms());
        logList.add("##");

        loadNewFilmlistFromWeb(logList, alwaysLoadNew, false, localFilmListFile);
        afterLoading(logList);
    }

    private void afterLoading(List<String> logList) {
        logList.add("##");
        logList.add("## Jetzige Liste erstellt am: " + filmListNew.genDate());
        logList.add("##   Anzahl Filme: " + filmListNew.size());
        logList.add("##   Anzahl Neue:  " + filmListNew.countNewFilms());
        logList.add("##");
        logList.add("## " + PLog.LILNE2);
        logList.add("##");

        setLoaded(new ListenerFilmlistLoadEvent("Filme markieren, Themen suchen",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        logList.add("## Filme markieren");
        final int count = filmListNew.markFilms();
        logList.add("## Anzahl doppelte Filme: " + count);

        filmListNew.loadSender();

        setLoaded(new ListenerFilmlistLoadEvent("Filme in Downloads eingetragen",
                ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
        logList.add("## Filme in Downloads eingetragen");

        //die FilmList wieder füllen
        logList.add("## ==> und jetzt die Filmliste wieder füllen :)");
        LoadFactoryConst.filmlist.metaData = filmListNew.metaData;
        LoadFactoryConst.filmlist.sender = filmListNew.sender;
        LoadFactoryConst.filmlist.addAll(filmListNew);
        filmListNew.clear();

        setFinished();
    }

    private void loadStoredList(List<String> logList, Filmlist filmlist, String localFilmListFile) {
        new ReadFilmlist().readFilmlistWebOrLocal(logList, filmlist, localFilmListFile);
    }

    private void loadNewFilmlistFromWeb(List<String> logList, boolean alwaysLoadNew, boolean intern, String
            localFilmListFile) {
        if (intern) {
            //dann ist die lokale Liste schon geladen und zu alt
            //Hash mit URLs füllen
            fillHash(logList, filmListNew);

        } else {
            //Start durch Button und die bestehende Liste wird aktualisiert
            //Hash mit URLs füllen
            fillHash(logList, LoadFactoryConst.filmlist);

            filmListNew.setMeta(LoadFactoryConst.filmlist);
            if (!alwaysLoadNew) {
                //dann die alte Filmliste nicht löschen, aber erst nach dem Hash!!
                filmListNew.addAll(LoadFactoryConst.filmlist);
            }
            LoadFactoryConst.filmlist.clear();
        }

        setStop(false);
        logList.add("## Filmliste laden (auto)");
        // Filmliste laden und Url automatisch ermitteln
        boolean wasOk = importNewFilmlisteFromServer.importFilmListFromWebAuto(logList, filmListNew, filmListDiff);
        if (wasOk) {
            //dann hats geklappt
            afterLoadingNewFilmlistFromServer(logList, localFilmListFile);
        } else {
            //dann wars fehlerhaft
            logList.add("## " + PLog.LILNE3);
            logList.add("## Das Laden hat nicht geklappt, alte Liste wieder laden");
            LoadFactoryConst.loadFilmlist.setStop(false);
            loadStoredList(logList, filmListNew, localFilmListFile);
            logList.add("##");
        }
    }

    /**
     * wird nach dem Import einer neuen Liste gemacht
     */
    private void afterLoadingNewFilmlistFromServer(List<String> logList, String localFilmListFile) {
        logList.add("## " + PLog.LILNE3);

        if (!filmListDiff.isEmpty()) {
            //dann wars nur ein Update
            filmListNew.updateList(filmListDiff, true/* Vergleich über Index, sonst nur URL */, true /* ersetzen */);
            filmListNew.metaData = filmListDiff.metaData;
            filmListNew.sort(); // jetzt sollte alles passen
            filmListDiff.clear();
        }

        logList.add("## Neue Filme markieren");
        findAndMarkNewFilms(logList, filmListNew);

        logList.add("## Unicode-Zeichen korrigieren");
        FilmFactory.cleanFaultyCharacterFilmlist(filmListNew);

        logList.add("## Diakritika setzen/ändern, Diakritika suchen");
        if (LoadFactoryConst.removeDiacritic) {
            FilmFactory.flattenDiacritic(filmListNew);
        } else {
            logList.add("## Diakritika: nicht gewollt");
        }

        logList.add("##");
        logList.add("## Filme schreiben (" + filmListNew.size() + " Filme) :");
        logList.add("##    --> Start Schreiben nach: " + localFilmListFile);
        new WriteFilmlistJson().write(localFilmListFile, filmListNew);
        logList.add("##    --> geschrieben!");
        logList.add("##");
    }

    private void fillHash(List<String> logList, Filmlist<FilmData> filmlist) {
        //alle historyURLs in den hash schreiben
        logList.add("## " + PLog.LILNE3);
        logList.add("## Hash füllen, Größe vorher: " + hashSet.size());

        hashSet.addAll(filmlist.stream().map(FilmData::getUrlHistory).collect(Collectors.toList()));
        logList.add("##                   nachher: " + hashSet.size());
        logList.add("## " + PLog.LILNE3);
    }

    private void findAndMarkNewFilms(List<String> logList, Filmlist<FilmData> filmlist) {
        filmlist.stream() //genauso schnell wie "parallel": ~90ms
                .peek(film -> film.setNewFilm(false))
                .filter(film -> !hashSet.contains(film.getUrlHistory()))
                .forEach(film -> {
                    film.setNewFilm(true);
                });

        hashSet.clear();
    }
}