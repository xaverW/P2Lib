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

package de.p2tools.p2lib.mediathek.audiolistload;

import de.p2tools.p2lib.mediathek.filmdata.FilmData;
import de.p2tools.p2lib.mediathek.filmdata.Filmlist;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2EventHandler;
import de.p2tools.p2lib.p2event.P2Events;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class P2ReadAudioToFilmList {
    // es wird in eine FILMLIST geladen

    private final P2EventHandler p2EventHandler;
    public final Filmlist audioListNew;

    public P2ReadAudioToFilmList(P2EventHandler p2EventHandler) {
        // Filmlist: Damit die evtl. überschriebene Version verwendet wird
        this.p2EventHandler = p2EventHandler;
        this.audioListNew = null;
    }

    public P2ReadAudioToFilmList(P2EventHandler p2EventHandler, Filmlist<? extends FilmData> audioListNew) {
        // Filmlist: Damit die evtl. überschriebene Version verwendet wird
        this.p2EventHandler = p2EventHandler;
        this.audioListNew = audioListNew;
    }

    /**
     * Audioliste beim Programmstart laden
     */
    public void loadAudioListAtProgStart() {
        // nur einmal direkt nach dem Programmstart
        new Thread(() -> {
            P2Duration.counterStart("loadAudioListAtProgStart");
            p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_START, "Programmstart, Liste laden", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
            final List<String> logList = new ArrayList<>();

            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("## Audioliste laden");
            logList.add("## Audioliste beim **Programmstart** laden - start");
            logList.add("## ");

            loadAudioListAtProgStart(logList);
            afterLoading(logList);

            logList.add("## Audioliste beim Programmstart laden - ende");
            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("");

            P2Log.emptyLine();
            P2Log.sysLog(logList);
            P2Log.emptyLine();
            P2Duration.counterStop("loadAudioListAtProgStart");
        }).start();
    }

    public void loadNewAudioListFromWeb() {
        // aus dem Menü oder Button in den Einstellungen
        new Thread(() -> {
            //damit wird eine neue Liste (Web) geladen UND auch gleich im Config-Ordner gespeichert
            P2Duration.counterStart("loadNewAudioListFromWeb");
            p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_START, "Audioliste aus dem Web laden", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
            final List<String> logList = new ArrayList<>();

            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("## Audioliste laden");
            logList.add("## Audioliste aus dem Web laden - start");
            logList.add("## Alte Liste erstellt  am: " + P2LoadConst.dateStoredAudiolist.getValueSafe());
            logList.add("##            Anzahl Beiträge: " + P2LoadConst.audioListLocal.size());
            logList.add("##");

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioWebToFilmList(logList, audioListNew).readWebList(P2LoadConst.localAudioListFile);
            afterLoading(logList);

            logList.add("## Audioliste aus dem Web laden - ende");
            logList.add("## " + P2Log.LILNE1);
            logList.add("## " + P2Log.LILNE1);
            logList.add("");
            P2Log.emptyLine();
            P2Log.sysLog(logList);
            P2Log.emptyLine();

            P2Duration.counterStop("loadNewAudioListFromWeb");
        }).start();
    }

    /**
     * Audioliste beim Programmstart laden
     */
    private void loadAudioListAtProgStart(List<String> logList) {
        // ProgStart, hier wird die gespeicherte Audioliste geladen und
        // wenn zu alt, wird eine neue aus dem Web geladen

        // ====
        // erster Start
        if (P2LoadConst.firstProgramStart) {
            // gespeicherte Audioliste laden, macht beim ersten Programmstart keinen Sinn
            logList.add("## Erster Programmstart -> Liste aus dem Web laden");

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioWebToFilmList(logList, audioListNew).readWebList(P2LoadConst.localAudioListFile);

            P2Duration.onlyPing("Erster Programmstart: Neu Audioliste aus dem Web geladen");
            p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_LOADED, "Audios verarbeiten", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
            return;
        }


        // ====
        // gespeicherte Liste -> User will kein Update
        if (!P2LoadConst.loadNewFilmlistOnProgramStart) {
            logList.add("## Beim Programmstart soll keine neue Liste geladen werden");
            logList.add("## dann gespeicherte Liste laden");

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioLocalFromToFilmList(logList, audioListNew).readLocalList(P2LoadConst.localAudioListFile);

            logList.add("## Gespeicherte Liste geladen");
            p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_LOADED, "Audios verarbeiten", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
            return;
        }


        // ===
        // laden mit evtl. Web-Update
        boolean loadFromWeb = false;
        //dann soll eine neue Liste beim Programmstart geladen, wenn nötig
        if (P2ReadAudioFactory.isNotFromToday(P2LoadConst.dateStoredAudiolist.getValueSafe())) {
            //gespeicherte Liste zu alt > Hash
            logList.add("## Gespeicherte Audioliste ist zu alt: " + P2LoadConst.dateStoredAudiolist.getValueSafe());
            logList.add("## Zuerst gespeicherte Liste laden");

            loadFromWeb = true;
            P2LoadConst.audioInitNecessary = false;
            new P2ReadAudioLocalFromToFilmList(logList, audioListNew).readLocalList(P2LoadConst.localAudioListFile); // Liste in new laden

            logList.add("## Programmstart: Gespeicherte Liste geladen");

        } else {
            // nicht zu alt
            logList.add("## Gespeicherte Audioliste ist nicht zu alt: " + P2LoadConst.dateStoredAudiolist.getValueSafe());
            logList.add("## Gespeicherte Liste laden");

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioLocalFromToFilmList(logList, audioListNew).readLocalList(P2LoadConst.localAudioListFile); // Liste in new laden

            logList.add("## Programmstart: Gespeicherte Liste geladen");
        }


        if (audioListNew.isEmpty() || loadFromWeb) {
            //dann war sie zu alt oder ist leer
            logList.add("## " + P2Log.LILNE3);
            logList.add("## Gespeicherte Audioliste ist leer oder zu alt -> neue Audioliste aus dem Web laden");
            p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_PROGRESS, "Audioliste ist zu alt, eine neue laden", P2ReadAudioFactory.PROGRESS_INDETERMINATE));

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioWebToFilmList(logList, audioListNew).readWebList(P2LoadConst.localAudioListFile);

            P2Duration.onlyPing("Programmstart: Neu Audioliste aus dem Web geladen");
        }


        if (audioListNew.isEmpty()) {
            // dann hat alles nicht geklappt?
            logList.add("## Das Laden der Liste hat nicht geklappt");
            logList.add("## Noch ein Versuch: Gespeicherte Liste laden");

            P2LoadConst.audioInitNecessary = true;
            new P2ReadAudioLocalFromToFilmList(logList, audioListNew).readLocalList(P2LoadConst.localAudioListFile);

            logList.add("## Gespeicherte Liste geladen");
        }

        p2EventHandler.notifyListener(
                new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_LOADED, "Audios verarbeiten", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
    }

    // #######################################
    // #######################################
    private void afterLoading(List<String> logList) {
        logList.add("##");
        logList.add("## Jetzige Liste erstellt am: " + P2LDateFactory.getNowString());
        logList.add("##   Anzahl Audios: " + audioListNew.size());
        logList.add("##");
        logList.add("## " + P2Log.LILNE2);
        logList.add("##");

        p2EventHandler.notifyListener(
                new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_LOADED, "Audios markieren, Themen suchen", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
        logList.add("## Audios markieren");
        final int count = audioListNew.markFilms(logList);
        logList.add("## Anzahl doppelte Filme: " + count);

        //die Liste wieder füllen
        logList.add("## ==> und jetzt die Audioliste wieder füllen :)");
        Platform.runLater(() -> {
            P2LoadConst.audioListLocal.sender = audioListNew.sender;
            P2LoadConst.audioListLocal.metaData = audioListNew.metaData;
            P2LoadConst.audioListLocal.setAll(audioListNew);
            audioListNew.clear();
            p2EventHandler.notifyListener(new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_FINISHED));
        });
    }
}