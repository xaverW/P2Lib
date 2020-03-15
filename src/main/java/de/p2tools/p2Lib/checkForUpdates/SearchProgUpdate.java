/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.checkForUpdates;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SearchProgUpdate {

    private final Stage stage;
    private boolean newVersion = false;
    private boolean newVersionBeta = false;
    private ProgUpdateData progUpdateData = new ProgUpdateData();
    private ProgUpdateData progUpdateDataBeta = new ProgUpdateData();

    public SearchProgUpdate() {
        this.stage = P2LibConst.primaryStage;
    }

    public SearchProgUpdate(Stage stage) {
        this.stage = stage;
    }

    public boolean checkAllUpdates(UpdateSearchData updateSearchData,
                                   UpdateSearchData updateSearchDataBeta,
                                   boolean showInfoAlways) {

        boolean showUpdate = false;
        boolean showUpdateBeta = false;
        int progVersion = 0;
        int progBuildNo = 0;
        ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList = new ArrayList<>(5);

        // Programmupdate suchen
        if (updateSearchData == null ||
                !UpdateFactory.retrieveInfos(stage, progUpdateData, updateSearchData.getUpdateUrl(), showInfoAlways)) {
            progUpdateData = null;

        } else {
            PLog.sysLog("check update");
            progVersion = updateSearchData.getProgVersionNo();
            progBuildNo = updateSearchData.getProgBuildNo();

            newVersion = UpdateFactory.checkVersion(progUpdateData, progVersion,
                    newProgUpdateInfoDataList, updateSearchData.updateInfoNoShownProperty());
            showUpdate = UpdateFactory.checkVersionNotShown(progUpdateData, progVersion,
                    newProgUpdateInfoDataList, updateSearchData.updateVersionShownProperty());

            if (showUpdate && updateSearchData.updateVersionShownProperty() != null) {
                // dann wurde was noch nicht gemeldet
                updateSearchData.updateVersionShownProperty().set(progUpdateData.getProgVersion());
            }
        }

        // BETA Update suchen
        if (updateSearchDataBeta == null ||
                !UpdateFactory.retrieveInfos(stage, progUpdateDataBeta, updateSearchDataBeta.getUpdateUrl(), false)) {
            progUpdateDataBeta = null;

        } else {
            PLog.sysLog("check update beta");
            progVersion = updateSearchDataBeta.getProgVersionNo();
            progBuildNo = updateSearchDataBeta.getProgBuildNo();

            newVersionBeta = UpdateFactory.checkBeta(progUpdateDataBeta, progVersion, progBuildNo);
            showUpdateBeta = UpdateFactory.checkVersionBetaNotShown(progUpdateDataBeta, progVersion, progBuildNo,
                    updateSearchDataBeta.updateVersionShownProperty(), updateSearchDataBeta.updateBuildNoShownProperty());

            if (showUpdateBeta && updateSearchDataBeta.updateVersionShownProperty() != null &&
                    updateSearchDataBeta.updateBuildNoShownProperty() != null) {
                // dann wurde was noch nicht gemeldet
                updateSearchDataBeta.updateVersionShownProperty().set(progUpdateDataBeta.getProgVersion());
                updateSearchDataBeta.updateBuildNoShownProperty().set(progUpdateDataBeta.getProgBuildNo());
            }
        }

//        if (!newVersionBeta) {
//            // dann wird  nichts angezeigt
//            progInfoBeta = null;
//        }

        if (progUpdateData == null && progUpdateDataBeta == null) {
            // dann hat was nicht geklappt
            return false;
        }

        // Infos anzeigen
        if (showInfoAlways || showUpdate || showUpdateBeta) {
            showAlert(progVersion, progBuildNo,
                    updateSearchData != null ? updateSearchData.updateVersionShownProperty() : null,
                    newProgUpdateInfoDataList,
                    updateSearchDataBeta != null ? updateSearchDataBeta.updateVersionShownProperty() : null,
                    updateSearchDataBeta != null ? updateSearchDataBeta.updateBuildNoShownProperty() : null,
                    updateSearchData != null ? updateSearchData.searchUpdateProperty() : null,
                    updateSearchDataBeta != null ? updateSearchDataBeta.searchUpdateProperty() : null,
                    showInfoAlways);
        }

        return newVersion || newVersionBeta;
    }

    private void showAlert(int progVersion, int progBuild,
                           IntegerProperty lastVersionShown,
                           ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
                           IntegerProperty lastVersionShownBeta,
                           IntegerProperty lastBuildNoShownBeta,
                           BooleanProperty searchForUpdateInfo,
                           BooleanProperty searchForUpdateBetaInfo,
                           boolean showInfoAlways) {

        Platform.runLater(() -> {

            BooleanProperty showAgain = new SimpleBooleanProperty(false);
            BooleanProperty showBetaAgain = new SimpleBooleanProperty(false);

            new InfoAlert(stage, progUpdateData, progUpdateDataBeta, newProgUpdateInfoDataList,
                    newVersion, newVersionBeta,
                    searchForUpdateInfo, searchForUpdateBetaInfo,
                    newVersion && !showInfoAlways ? showAgain : null,
                    newVersionBeta && !showInfoAlways ? showBetaAgain : null)
                    .showInfoAlert((newVersion ? "Neue Version verfügbar" : "Infos"));


            if (newVersion && showAgain.get()) {
                // dann wieder anzeigen
                if (lastVersionShown != null) {
                    lastVersionShown.set(progVersion);
                }
            }
            if (newVersionBeta && showBetaAgain.get()) {
                // dann wieder anzeigen
                if (lastVersionShownBeta != null && lastBuildNoShownBeta != null) {
                    lastVersionShownBeta.set(progVersion);
                    lastBuildNoShownBeta.set(progBuild);
                }
            }

        });

    }
}
