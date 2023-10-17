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

import javafx.application.Platform;

public class FoundAll {

    private FoundAll() {
    }

    public static void foundAll(FoundSearchData foundSearchData) {
        FoundAllFiles.found(foundSearchData);
        if (foundSearchData.isShowAlways() ||

                foundSearchData.isFoundNewInfo() && !foundSearchData.getFoundFileListInfo().isEmpty() ||
                foundSearchData.isFoundNewVersion() && !foundSearchData.getFoundFileListAct().isEmpty() ||

                //searchAct ist immer an, sonst wird das gar nicht aufgerufen aber BETA/DALY nicht
                foundSearchData.isSearchBeta() && foundSearchData.isFoundNewBeta() && !foundSearchData.getFoundFileListBeta().isEmpty() ||
                foundSearchData.isSearchBeta() && foundSearchData.isSearchDaily() && foundSearchData.isFoundNewDaily() && !foundSearchData.getFoundFileListDaily().isEmpty()) {

            Platform.runLater(() -> {
                runInfoAlert(foundSearchData);
            });
        }
    }

    private static void runInfoAlert(FoundSearchData foundSearchData) {
        // die Infos über Updates anzeigen
        InfoAlert infoAlert = new InfoAlert(foundSearchData);
        infoAlert.showInfoAlert("");

        // und merken was schon angezeigt wurde
        if (foundSearchData.isFoundNewVersion() && !foundSearchData.searchActAgainProperty().getValue()) {
            //dann die angezeigte neue Version merken
            foundSearchData.lastActDateProperty().setValue(foundSearchData.getNewVersionDate());

        } else if (foundSearchData.isFoundNewVersion() && foundSearchData.searchActAgainProperty().getValue()) {
            //dann die neue Version nochmal anzeigen und lastAct wieder auf das build-date setzen
            foundSearchData.lastActDateProperty().setValue(foundSearchData.getProgBuildDate());
        }

        if (foundSearchData.isFoundNewInfo()) {
            //dann die angezeigte neue Version merken
            foundSearchData.lastInfoDateProperty().setValue(foundSearchData.getNewInfoDate());
        }

        if (foundSearchData.isFoundNewBeta()) {
            //dann die angezeigte neue Version merken
            foundSearchData.lastBetaDateProperty().setValue(foundSearchData.getNewBetaDate());
        }

        if (foundSearchData.isFoundNewDaily()) {
            //dann die angezeigte neue Version merken
            foundSearchData.lastDailyDateProperty().setValue(foundSearchData.getNewDailyDate());
        }

    }
}
