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

import de.p2tools.p2Lib.mtFilm.film.Filmlist;
import de.p2tools.p2Lib.mtFilm.readWriteFilmlist.ReadFilmlist;
import de.p2tools.p2Lib.mtFilm.tools.LoadFactoryConst;

import java.util.List;

enum STATE {
    COMPLETE, DIFF
}

public class ImportNewFilmlistFromServer {

    public ImportNewFilmlistFromServer() {
    }

    // #########################################################
    // Filmliste importieren, URL automatisch wählen
    // #########################################################
    public boolean importFilmListFromWebAuto(List<String> logList, Filmlist filmlist, Filmlist filmListDiff) {
        STATE state;
        boolean ret;
        if (filmlist.isTooOldForDiff()) {
            // dann eine komplette Liste laden
            state = STATE.COMPLETE;
            filmlist.clear();
            logList.add("komplette Filmliste laden");
            ret = loadList(logList, filmlist, state);
        } else {
            // nur ein Update laden
            state = STATE.DIFF;
            logList.add("Diffliste laden");
            ret = loadList(logList, filmListDiff, state);
            if (!ret || filmListDiff.isEmpty()) {
                // wenn diff, dann nochmal mit einer kompletten Liste versuchen
                state = STATE.COMPLETE;
                filmlist.clear();
                filmListDiff.clear();
                logList.add("Diffliste war leer, komplette Filmliste laden");
                ret = loadList(logList, filmlist, state);
            }
        }
        if (!ret) {
            logList.add("Es konnten keine Filme geladen werden!");
        }
        return ret;
    }

    private boolean loadList(List<String> logList, Filmlist list, STATE state) {
        String updateUrl = state == STATE.COMPLETE ? LoadFactoryConst.FILMLIST_URL_AKT : LoadFactoryConst.FILMLIST_URL_DIFF;
        new ReadFilmlist().readFilmlistWebOrLocal(logList, list, updateUrl);

        if (LoadFactoryConst.loadFilmlist.isStop()) {
            // wenn abgebrochen wurde, nicht weitermachen
            return false;
        }
        boolean ret = !list.isEmpty();
        return ret;
    }
}
