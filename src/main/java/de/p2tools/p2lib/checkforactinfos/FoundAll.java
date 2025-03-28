/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2lib.checkforactinfos;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;

import java.util.ArrayList;

public class FoundAll {

    private FoundAll() {
    }

    public static void foundAll(FoundSearchDataDTO foundSearchDataDTO) {
        // ist der Start der Suche

        ArrayList<String> log = new ArrayList<>();
        log.add("=====================================");
        log.add("Update-Suche: " + foundSearchDataDTO.searchUrlDownload);
        log.add("Letzte Suche war: " + foundSearchDataDTO.getLastFoundDate());
        P2Log.sysLog(log);

        try {
            FoundAllFiles.found(foundSearchDataDTO);
        } catch (Exception ex) {
            P2Log.errorLog(956201210, ex, "URL: " + foundSearchDataDTO.getSearchUrlDownload());
            Platform.runLater(() -> {
                if (foundSearchDataDTO.isShowDialogAlways()) {
                    P2Alert.showErrorAlert("Update-Suche", "Die Suche nach einem Programmupdate hat nicht geklappt.");
                }
            });
            return;
        }

        // dann merken bis wann, alles angezeigt wurde
        foundSearchDataDTO.setLastFoundDate(P2LDateFactory.toStringR(FoundAllFiles.maxFoundDate));

        if (foundSearchDataDTO.isShowDialogAlways() ||

                // info, act
                foundSearchDataDTO.isFoundNewInfo() && !foundSearchDataDTO.getFoundFileListInfo().isEmpty() ||
                foundSearchDataDTO.isFoundNewVersion() && !foundSearchDataDTO.getFoundFileListAct().isEmpty() ||

                // beta ist nicht immer angeschaltet
                foundSearchDataDTO.isSearchBeta() && foundSearchDataDTO.isFoundNewBeta() &&
                        !foundSearchDataDTO.getFoundFileListBeta().isEmpty() ||

                // daily ist nicht immer angeschaltet, muss auch beta angeschaltet sein
                foundSearchDataDTO.isSearchBeta() &&
                        foundSearchDataDTO.isSearchDaily() &&
                        foundSearchDataDTO.isFoundNewDaily() && !foundSearchDataDTO.getFoundFileListDaily().isEmpty()) {

            Platform.runLater(() -> showInfoAlert(foundSearchDataDTO));

        } else {
            log.clear();
            log.add("gibt kein Update");
            log.add("=====================================");
            P2Log.sysLog(log);
        }
    }

    private static void showInfoAlert(FoundSearchDataDTO foundSearchDataDTO) {
        // die Infos über Updates anzeigen
        new InfoAlert(foundSearchDataDTO);

        // und merken was schon angezeigt wurde
        if (foundSearchDataDTO.isSearchActAgain()) {
            // dann wieder auf das build-date setzen, um alles danach wieder anzuzeigen
            foundSearchDataDTO.setLastFoundDate(foundSearchDataDTO.getProgBuildDate());
        }
    }
}
