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
    private ProgInfo progInfo = new ProgInfo();
    private ProgInfo progInfoBeta = new ProgInfo();

    public SearchProgUpdate() {
        this.stage = P2LibConst.primaryStage;
    }

    public SearchProgUpdate(Stage stage) {
        this.stage = stage;
    }

    public boolean checkProgVersion(String searchUrl, int progVersion,
                                    IntegerProperty lastVersion, IntegerProperty lastInfoNo,
                                    BooleanProperty searchForUpdateInfo) {

        return checkAllUpdates(searchUrl, "", progVersion, 0,
                lastVersion, lastInfoNo, null, null,
                searchForUpdateInfo, null,
                false);
    }

    public boolean checkProgVersionBeta(String searchUrl, String searchUrlBeta, int progVersion, int progBuild,
                                        IntegerProperty lastVersion, IntegerProperty lastInfoNo,
                                        IntegerProperty lastVersionBeta, IntegerProperty lastBuildNoBeta,
                                        BooleanProperty searchForUpdateInfo, BooleanProperty searchForUpdateInfoBeta) {

        return checkAllUpdates(searchUrl, searchUrlBeta, progVersion, progBuild,
                lastVersion, lastInfoNo, lastVersionBeta, lastBuildNoBeta,
                searchForUpdateInfo, searchForUpdateInfoBeta,
                false);
    }

    public boolean checkAll(String searchUrl, String searchUrlBeta,
                            int progVersion, int progBuild) {

        return checkAllUpdates(searchUrl, searchUrlBeta, progVersion, progBuild,
                null, null, null, null, null, null,
                true);
    }


    public boolean checkAllUpdates(String searchUrl, String searchUrlBeta,
                                   int progVersion, int progBuild,
                                   IntegerProperty lastVersion, IntegerProperty lastInfoNo,
                                   IntegerProperty lastVersionBeta, IntegerProperty lastBuildNoBeta,
                                   BooleanProperty searchForUpdateInfo, BooleanProperty searchForUpdateBetaInfo,
                                   boolean showInfoAlways) {

        boolean showUpdate = false;
        boolean showUpdateBeta = false;
        ArrayList<Infos> newInfosList = new ArrayList<>(5);

        // Programmupdate suchen
        if (searchUrl.isEmpty() || !UpdateFactory.retrieveInfos(stage, progInfo, searchUrl, showInfoAlways)) {
            progInfo = null;
        } else {
            newVersion = UpdateFactory.checkVersion(progInfo, newInfosList, progVersion, lastInfoNo);
            showUpdate = UpdateFactory.checkVersionNotShown(progInfo, progVersion, newInfosList, lastVersion);
            if (showUpdate && lastVersion != null) {
                // dann wurde was noch nicht gemeldet
                lastVersion.set(progInfo.getProgVersion());
            }
        }

        // BETA Update suchen
        if (searchUrlBeta.isEmpty() || !UpdateFactory.retrieveInfos(stage, progInfoBeta, searchUrlBeta, false)) {
            progInfoBeta = null;
        } else {
            newVersionBeta = UpdateFactory.checkBeta(progInfoBeta, progVersion, progBuild);
            showUpdateBeta = UpdateFactory.checkVersionBetaNotShown(progInfoBeta, progVersion, progBuild, lastVersionBeta, lastBuildNoBeta);
            if (showUpdateBeta && lastVersionBeta != null && lastBuildNoBeta != null) {
                // dann wurde was noch nicht gemeldet
                lastVersionBeta.set(progInfoBeta.getProgVersion());
                lastBuildNoBeta.set(progInfoBeta.getProgBuildNo());
            }
        }
        if (!newVersionBeta) {
            // dann wird  nichts angezeigt
            progInfoBeta = null;
        }

        if (progInfo == null && progInfoBeta == null) {
            // dann hat was nicht geklappt
            return false;
        }

        // Infos anzeigen
        if (showInfoAlways || showUpdate || showUpdateBeta) {
            showAlert(progVersion, progBuild, lastVersion, newInfosList,
                    lastVersionBeta, lastBuildNoBeta,
                    searchForUpdateInfo, searchForUpdateBetaInfo,
                    showInfoAlways);
        }

        return (newVersion || newVersionBeta) ? true : false;
    }

    private void showAlert(int progVersion, int progBuild, IntegerProperty lastVersion, ArrayList<Infos> newInfosList,
                           IntegerProperty lastVersionBeta, IntegerProperty lastBuildNoBeta,
                           BooleanProperty searchForUpdateInfo, BooleanProperty searchForUpdateBetaInfo,
                           boolean showInfoAlways) {

        Platform.runLater(() -> {

            BooleanProperty showAgain = new SimpleBooleanProperty(false);
            BooleanProperty showBetaAgain = new SimpleBooleanProperty(false);

            new InfoAlert(stage, progInfo, progInfoBeta, newInfosList, newVersion,
                    searchForUpdateInfo, searchForUpdateBetaInfo,
                    newVersion && !showInfoAlways ? showAgain : null,
                    newVersionBeta && !showInfoAlways ? showBetaAgain : null)
                    .showInfoAlert((newVersion ? "Neue Version verfügbar" : "Infos"));


            if (newVersion && showAgain.get()) {
                // dann wieder anzeigen
                if (lastVersion != null) {
                    lastVersion.set(progVersion);
                }
            }
            if (newVersionBeta && showBetaAgain.get()) {
                // dann wieder anzeigen
                if (lastVersionBeta != null && lastBuildNoBeta != null) {
                    lastVersionBeta.set(progVersion);
                    lastBuildNoBeta.set(progBuild);
                }
            }

        });

    }
}
