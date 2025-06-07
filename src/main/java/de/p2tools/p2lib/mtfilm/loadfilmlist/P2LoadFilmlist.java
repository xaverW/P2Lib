/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.FilmFactory;
import de.p2tools.p2lib.mtfilm.film.Filmlist;
import de.p2tools.p2lib.mtfilm.film.FilmlistFactory;
import de.p2tools.p2lib.mtfilm.readwritefilmlist.P2ReadFilmlist;
import de.p2tools.p2lib.mtfilm.readwritefilmlist.P2WriteFilmlistJson;
import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2EventHandler;
import de.p2tools.p2lib.p2event.P2Events;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class P2LoadFilmlist {

    public static final double PROGRESS_MIN = 0.0;
    public static final double PROGRESS_MAX = 1.0;
    public static final double PROGRESS_INDETERMINATE = -1.0;

    private static final AtomicBoolean stop = new AtomicBoolean(false); // damit kann das Laden gestoppt werden kann
    private final HashSet<String> hashSet;
    private final Filmlist<? extends FilmData> filmListDiff;
    private final Filmlist<? extends FilmData> filmListNew;
    private final P2ImportFilmlistFromServer p2ImportFilmlistFromServer;
    //    public final P2LoadNotifier p2LoadNotifier = new P2LoadNotifier();
    private final P2EventHandler p2EventHandler;
    private final BooleanProperty propLoadFilmlist = new SimpleBooleanProperty(false);
    private boolean filmlistTooOld = false;

    public P2LoadFilmlist(P2EventHandler p2EventHandler) {
        this.p2EventHandler = p2EventHandler;
        this.filmListDiff = new Filmlist<>();
        this.filmListNew = new Filmlist<>();
        hashSet = new HashSet<>();
        p2ImportFilmlistFromServer = new P2ImportFilmlistFromServer();
    }

    public P2LoadFilmlist(P2EventHandler p2EventHandler, Filmlist<? extends FilmData> filmlistNew, Filmlist<? extends FilmData> filmlistDiff) {
        this.p2EventHandler = p2EventHandler;
        this.filmListDiff = filmlistDiff;
        this.filmListNew = filmlistNew;
        hashSet = new HashSet<>();
        p2ImportFilmlistFromServer = new P2ImportFilmlistFromServer();
    }

    public P2EventHandler getP2EventHandler() {
        return p2EventHandler;
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
        if (P2LoadFactory.checkAllSenderSelectedNotToLoad(P2LibConst.actStage)) {
            // alle Sender sind vom Laden ausgenommen
            P2Log.sysLog("Filmliste laden: Es sind keine Sender eingeschaltet!!");
            return;
        }

        // Start des Ladens, gibts keine Fortschrittsanzeige und kein Abbrechen
        setPropLoadFilmlist(true);
        p2EventHandler.notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_START, "Programmstart, Filmliste laden",
                P2LoadFilmlist.PROGRESS_INDETERMINATE));

        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            P2Duration.counterStart("loadFilmlistProgStart");

            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("## Filmliste laden");
            logList.add("## Filmliste beim **Programmstart** laden - start");

            loadFilmlistProgStart(logList);
            afterLoading(logList);

            logList.add("## Filmliste beim Programmstart laden - ende");
            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("");

            P2Log.emptyLine();
            P2Log.sysLog(logList);
            P2Log.emptyLine();

            setPropLoadFilmlist(false);
            P2Duration.counterStop("loadFilmlistProgStart");
        }).start();
    }

    public void loadNewFilmlistFromWeb(boolean alwaysLoadNew/*, String localFilmListFile*/) {
        if (P2LoadFactory.checkAllSenderSelectedNotToLoad(P2LibConst.actStage)) {
            // alle Sender sind vom Laden ausgenommen
            P2Log.sysLog("Filmliste laden: Es sind keine Sender eingeschaltet!!");
            //todo Dialog anzeigen
            return;
        }

        setPropLoadFilmlist(true);
        p2EventHandler.notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_START, "Filmliste aus dem Web laden",
                P2LoadFilmlist.PROGRESS_INDETERMINATE));

        new Thread(() -> {
            final List<String> logList = new ArrayList<>();
            P2Duration.counterStart("loadNewFilmlistFromWeb");

            //damit wird eine neue Filmliste (Web) geladen UND auch gleich im Config-Ordner gespeichert
            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("## Filmliste laden");
            logList.add("## Filmliste aus dem Web laden - start");
            logList.add("## Alte Liste erstellt  am: " + LoadFactoryConst.filmlist.genDate());
            logList.add("##            Anzahl Filme: " + LoadFactoryConst.filmlist.size());
            logList.add("##            Anzahl  Neue: " + LoadFactoryConst.filmlist.countNewFilms());
            logList.add("##");

            loadNewFilmlistFromWeb(logList, alwaysLoadNew, false, LoadFactoryConst.localFilmListFile);
            afterLoading(logList);

            logList.add("## Filmliste aus dem Web laden - ende");
            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("");

            P2Log.emptyLine();
            P2Log.sysLog(logList);
            P2Log.emptyLine();

            setPropLoadFilmlist(false);
            P2Duration.counterStop("loadNewFilmlistFromWeb");
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
        // einer der ZWEI Einstiegspunkte zum Laden: ProgStart / sofort Web
        // hier wird die gespeicherte Filmliste geladen und wenn zu alt, wird eine neue aus
        // dem Web geladen

        filmListNew.setMeta(LoadFactoryConst.filmlist);
        filmListNew.setAll(LoadFactoryConst.filmlist);

        if (LoadFactoryConst.firstProgramStart) {
            // gespeicherte Filmliste laden, macht beim ersten Programmstart keinen Sinn
            logList.add("## Erster Programmstart -> Liste aus dem Web laden");

            loadNewFilmlistFromWeb(logList, false, true, LoadFactoryConst.localFilmListFile);
            P2Duration.onlyPing("Erster Programmstart: Neu Filmliste aus dem Web geladen");

        } else {
            // dann ist ein normaler Start mit vorhandener Filmliste
            if (!LoadFactoryConst.loadNewFilmlistOnProgramStart) {
                //dann wird keine neue Liste aus dem Web beim Programmstart geladen, immer gespeicherte Liste laden
                logList.add("## Beim Programmstart soll keine neue Liste geladen werden");
                logList.add("## Programmstart: Gespeicherte Liste aus laden");
//                LoadFactoryConst.filmInitNecessary = true;
                loadStoredList(logList, filmListNew, LoadFactoryConst.localFilmListFile);
                logList.add("## Programmstart: Gespeicherte Liste aus geladen");

            } else {
                if (FilmlistFactory.isTooOld(LoadFactoryConst.dateStoredFilmlist)) {
                    //gespeicherte Liste zu alt und muss aber trotzdem geladen werden :(
                    //den Hash für "neue" braucht es immer
                    logList.add("## Gespeicherte Filmliste ist zu alt: " + LoadFactoryConst.dateStoredFilmlist);
                    filmlistTooOld = true;

                    //und jetzt noch schauen, ob ein diff reicht
                    if (FilmlistFactory.isTooOldForDiff(LoadFactoryConst.dateStoredFilmlist)) {
                        logList.add("## Gespeicherte Filmliste zu alt für ein DIFF, kein FILM_INIT");
//                        LoadFactoryConst.filmInitNecessary = false;

                    } else {
                        logList.add("## Gespeicherte Filmliste zu alt, DIFF reicht, FILM_INIT wird gemacht");
//                        LoadFactoryConst.filmInitNecessary = true;
                    }
                } else {
                    logList.add("## Gespeicherte Filmliste ist nicht zu alt: " + LoadFactoryConst.dateStoredFilmlist);
//                    LoadFactoryConst.filmInitNecessary = true;
                }

                P2Duration.counterStart("loadStoredList");
                logList.add("## Programmstart: Gespeicherte Liste laden");
                loadStoredList(logList, filmListNew, LoadFactoryConst.localFilmListFile);
//                LoadFactoryConst.filmInitNecessary = true;//!! jetzt gleich wieder setzen, sonst klappt das weitere Laden nicht mehr

                logList.add("## Programmstart: Gespeicherte Liste geladen");
                P2Log.debugLog("## loadStoredList: " + P2Duration.counterStop("loadStoredList"));

                if (filmListNew.isEmpty()) {
                    //dann ist sie leer
                    logList.add("## Gespeicherte Filmliste ist leer, neue Filmliste aus dem Web laden");
                    logList.add("## Alter|min]: " + filmListNew.getAge() / 60);
                    logList.add("## " + P2Log.LILNE3);
                }

                if (filmlistTooOld || filmListNew.isEmpty()) {
                    //dann war sie zu alt oder ist leer
                    filmlistTooOld = false;//dann gleich wieder ausschalten
                    p2EventHandler.notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_PROGRESS, "Filmliste ist zu alt, eine neue laden",
                            P2LoadFilmlist.PROGRESS_INDETERMINATE));

                    logList.add("## Programmstart: Neue Liste aus dem Web laden");
                    loadNewFilmlistFromWeb(logList, false, true, LoadFactoryConst.localFilmListFile);
                    P2Duration.onlyPing("Programmstart: Neu Filmliste aus dem Web geladen");
                }
            }
        }
        getP2EventHandler().notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_LOADED, "Filme verarbeiten",
                P2LoadFilmlist.PROGRESS_INDETERMINATE));
    }

    // #######################################
    // #######################################


    private void afterLoading(List<String> logList) {
        filmListNew.forEach(FilmData::init); // damit wird auch das Datum! gesetzt

        logList.add("##");
        logList.add("## Jetzige Liste erstellt am: " + filmListNew.genDate());
        logList.add("##   Anzahl Filme: " + filmListNew.size());
        logList.add("##   Anzahl Neue:  " + filmListNew.countNewFilms());
        logList.add("##");
        logList.add("## " + P2Log.LILNE2);
        logList.add("##");

        getP2EventHandler().notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_LOADED, "Filme markieren, Themen suchen",
                P2LoadFilmlist.PROGRESS_INDETERMINATE));
        logList.add("## Filme markieren");
        final int count = filmListNew.markFilms();
        logList.add("## Anzahl doppelte Filme: " + count);

        filmListNew.loadSender();

//        getP2EventHandler().notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_LOADED, "Filme in Downloads eingetragen",
//                LoadFilmlist.PROGRESS_INDETERMINATE));
//        logList.add("## Filme in Downloads eingetragen");

        //die FilmList wieder füllen
        logList.add("## ==> und jetzt die Filmliste wieder füllen :)");
        LoadFactoryConst.filmlist.metaData = filmListNew.metaData;
        LoadFactoryConst.filmlist.sender = filmListNew.sender;
        LoadFactoryConst.filmlist.addAll(filmListNew);
        filmListNew.clear();
        p2EventHandler.notifyListener(new P2Event(P2Events.EVENT_FILMLIST_LOAD_FINISHED));
    }

    private void loadStoredList(List<String> logList, Filmlist filmlist, String localFilmListFile) {
        new P2ReadFilmlist().readFilmlistWebOrLocal(logList, filmlist, localFilmListFile);
    }

    private void loadNewFilmlistFromWeb(List<String> logList, boolean alwaysLoadNew,
                                        boolean intern, String localFilmListFile) {
        // einer der ZWEI Einstiegspunkte zum Laden: ProgStart / sofort Web
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
        boolean wasOk = p2ImportFilmlistFromServer.importFilmListFromWebAuto(logList, filmListNew, filmListDiff);
        if (wasOk) {
            //dann hats geklappt
            afterLoadingNewFilmlistFromServer(logList, localFilmListFile);
        } else {
            //dann wars fehlerhaft
            logList.add("## " + P2Log.LILNE3);
            logList.add("## Das Laden hat nicht geklappt, alte Liste wieder laden");
            LoadFactoryConst.p2LoadFilmlist.setStop(false);
            loadStoredList(logList, filmListNew, localFilmListFile);
            logList.add("##");
        }
    }

    /**
     * wird nach dem Import einer neuen Liste gemacht
     */
    private void afterLoadingNewFilmlistFromServer(List<String> logList, String localFilmListFile) {
        logList.add("## " + P2Log.LILNE3);

        if (!filmListDiff.isEmpty()) {
            //dann wars nur ein Update und das muss einsortiert werden
            filmListNew.updateList(filmListDiff, true/* Vergleich über Index, sonst nur URL */, true /* ersetzen */);

            filmListNew.metaData = filmListDiff.metaData;
            P2Duration.counterStart("sortNewList");
            filmListNew.sort(); //~3s, jetzt sollte alles passen
            P2Log.debugLog("## Update List, Sort: " + P2Duration.counterStop("sortNewList"));

            filmListDiff.clear();
        }

        logList.add("## Neue Filme markieren");
        findAndMarkNewFilms(logList, filmListNew);

        //Unicode-Zeichen korrigieren
        P2Duration.counterStart("cleanFaultyCharacter");
        FilmFactory.cleanFaultyCharacterFilmlist(logList, filmListNew);
        P2Log.debugLog("## Unicode-Zeichen korrigieren: " + P2Duration.counterStop("cleanFaultyCharacter"));

        logList.add("## Diakritika setzen/ändern, Diakritika suchen");
        if (LoadFactoryConst.removeDiacritic) {
            FilmFactory.flattenDiacritic(filmListNew);
        } else {
            logList.add("## Diakritika: nicht gewollt");
        }

        logList.add("##");
        logList.add("## Filme schreiben (" + filmListNew.size() + " Filme) :");
        logList.add("##    --> Start Schreiben nach: " + localFilmListFile);
        new P2WriteFilmlistJson().write(localFilmListFile, filmListNew);
        logList.add("##    --> geschrieben!");
        logList.add("##");
    }

    private void fillHash(List<String> logList, Filmlist<? extends FilmData> filmlist) {
        //alle historyURLs in den hash schreiben
        logList.add("## " + P2Log.LILNE3);
        logList.add("## Hash füllen, Größe vorher: " + hashSet.size());

        hashSet.addAll(filmlist.stream().map(FilmData::getUrlHistory).collect(Collectors.toList()));
        logList.add("##                   nachher: " + hashSet.size());
        logList.add("## " + P2Log.LILNE3);
    }

    private void findAndMarkNewFilms(List<String> logList, Filmlist<? extends FilmData> filmlist) {
        filmlist.stream() //genauso schnell wie "parallel": ~90ms
                .peek(film -> film.setNewFilm(false))
                .filter(film -> !hashSet.contains(film.getUrlHistory()))
                .forEach(film -> {
                    film.setNewFilm(true);
                });

        hashSet.clear();
    }
}