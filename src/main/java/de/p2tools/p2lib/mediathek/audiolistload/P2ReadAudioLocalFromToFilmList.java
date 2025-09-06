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

import de.p2tools.p2lib.mediathek.film.P2FilmlistFactory;
import de.p2tools.p2lib.mediathek.filmdata.FilmData;
import de.p2tools.p2lib.mediathek.filmdata.Filmlist;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.mediathek.filmlistreadwrite.P2ReadFilmlist;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Events;
import de.p2tools.p2lib.tools.log.P2Log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class P2ReadAudioLocalFromToFilmList {
    // es wird in eine FILMLIST geladen

    private final List<String> logList;
    public Filmlist<FilmData> audioListNew;

    public P2ReadAudioLocalFromToFilmList(List<String> logList, Filmlist audioListNew) {
        this.logList = logList;
        this.audioListNew = audioListNew;
    }

    public boolean readLocalList(String path) {
        // beim Programmstart wird die gespeicherte Liste geladen
        boolean ret;

        try {
            Path listPath = Paths.get(path);
            if (!Files.exists(listPath) || listPath.toFile().length() == 0) {
                return false;
            }

            P2LoadConst.audioListLocal.clear();
            audioListNew.clear();
            logList.add("## " + "Audioliste lesen");
            logList.add("## " + "   --> Lesen von: " + path);
            new P2ReadFilmlist(false).readFilmlistWebOrLocal(logList, audioListNew, path);
            setDateFromLocal();

            audioListNew.loadSender();

            logList.add("##   Audioliste gelesen, OK");
            logList.add("##   Anzahl gelesen: " + audioListNew.size());
            ret = true;

        } catch (final Exception ex) {
            logList.add("##   Audioliste lesen hat nicht geklappt");
            P2Log.errorLog(645891204, ex);
            P2LoadConst.p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_FINISHED, "Laden hat nicht geklappt", P2ReadAudioFactory.PROGRESS_INDETERMINATE));
            ret = false;
        }
        return ret;
    }

    private void setDateFromLocal() {
        // Datum setzen, Format stimmt da ja schon!
        String date = P2FilmlistFactory.genDate(audioListNew.metaData);
        P2LoadConst.dateStoredAudiolist.set(date);
    }
}
