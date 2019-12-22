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
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SearchProgInfo {

    private final String UPDATE_SEARCH_TITLE = "Software-Aktualisierung";
    private final String UPDATE_ERROR_MESSAGE =
            "Es ist ein Fehler bei der Softwareaktualisierung aufgetreten." + P2LibConst.LINE_SEPARATOR
                    + "Die aktuelle Version konnte nicht ermittelt werden.";


    private final Stage stage;
    private boolean newVersion;

    public SearchProgInfo() {
        this.stage = P2LibConst.primaryStage;
    }

    public SearchProgInfo(Stage stage) {
        this.stage = stage;
    }

    public boolean checkUpdate(String searchUrl, int progVersion,
                               IntegerProperty lastVersion, IntegerProperty lastInfoNo,
                               boolean showAlwaysInfo, boolean showError) {

        return checkVersion(searchUrl, progVersion, lastVersion, lastInfoNo,
                null, false, showAlwaysInfo, showError);
    }

    public boolean checkUpdate(String searchUrl, int progVersion,
                               IntegerProperty lastVersion, IntegerProperty lastInfoNo,
                               BooleanProperty searchForUpdateInfos,
                               boolean showUpdate,
                               boolean showAlwaysInfo, boolean showError) {

        // return neue Version oder neue Infos
        return checkVersion(searchUrl, progVersion, lastVersion, lastInfoNo,
                searchForUpdateInfos, showUpdate, showAlwaysInfo, showError);
    }

    public boolean checkBetaUpdate(String searchUrl, int progVersion, int progBuild,
                                   IntegerProperty lastVersion,
                                   IntegerProperty lastBuildNo,
                                   BooleanProperty searchForUpdateInfos, boolean showAlwaysInfo, boolean showError) {

        // return neue Version oder neue Infos
        return checkBeta(searchUrl, progVersion, progBuild, lastVersion, lastBuildNo,
                searchForUpdateInfos, showAlwaysInfo, showError);
    }

    private boolean retrieveInfos(ProgInfo progInfo, String url, boolean showAlwaysInfo, boolean showError) {
        // prüft of die Suche nach Updateinfos geklappt hat

        if (!RetrieveProgInfo.retrieveProgramInformation(progInfo, url)) {
            progInfo = null;
        }
        if (progInfo == null || progInfo.getProgVersion() < 0) {
            // wenn Version < 0 hat was nicht geklappt
            PLog.errorLog(978451203, "Das Suchen nach einem Programmupdate hat nicht geklappt!");

            if (showAlwaysInfo || showError) {
                // dann konnte die "Version" im xml nicht geparst werden
                Platform.runLater(() -> new PAlert().showErrorAlert(stage, "Fehler", UPDATE_SEARCH_TITLE, UPDATE_ERROR_MESSAGE));
            }
            return false;
        }

        return true;
    }

    private boolean checkVersion(String searchUrl, int progVersion,
                                 IntegerProperty lastVersion, IntegerProperty lastInfoNr,
                                 BooleanProperty searchForUpdateInfos,
                                 boolean showUpdate,
                                 boolean showAlwaysInfo, boolean showError) {
        // prüft auf neue Version, anzeigen: wenn true
        // showAllwaysInfo-> dann wird die Info immer angezeigt
        PLog.sysLog("check update");

        ProgInfo progInfo = new ProgInfo();
        if (!retrieveInfos(progInfo, searchUrl, showAlwaysInfo, showError)) {
            return false;
        }

        // nach neuen Infos suchen
        boolean newInfo = false;
        ArrayList<Infos> newInfosList = new ArrayList<>(5);
        if (lastInfoNr != null) {
            int lInfoNr = lastInfoNr.get();
            for (Infos infos : progInfo.getInfos()) {
                if (infos.getInfoNr() > lInfoNr) {
                    newInfo = true;
                    lastInfoNr.setValue(infos.getInfoNr());
                    newInfosList.add(infos);
                }
            }
        }

        // nach regulärem Update suchen
        boolean newVersion = progInfo.getProgVersion() > progVersion;
        if (newVersion) {
            // dann ist eine neue Version
            PLog.sysLog("gibt eine neue Version: " + progInfo.getProgVersion());
        }

        if (newVersion && progInfo.getProgVersion() > lastVersion.get() || newInfo || showAlwaysInfo) {
            Platform.runLater(() -> {
                BooleanProperty shUp = new SimpleBooleanProperty(true);
                new InfoUpdateAlert(stage, progInfo, newInfosList, newVersion, searchForUpdateInfos, showUpdate ? shUp : null).
                        showInfoAlert("Programminfos", (newVersion ? "Neue Version verfügbar" : "Infos"));
                if (showUpdate && !shUp.get() && newVersion) {
                    // dann ists eine neue Version
                    lastVersion.set(progInfo.getProgVersion());
                }
            });
        }

        return newVersion;
    }

    private boolean checkBeta(String searchUrl, int progVersion, int progBuild,
                              IntegerProperty lastVersion, IntegerProperty lastBuildNo,
                              BooleanProperty searchForUpdateInfos,
                              boolean showAlwaysInfo, boolean showError) {


        // prüft auf neue beta-Version, anzeigen: wenn true
        // showAllwaysInfo-> dann wird die Info immer angezeigt

        PLog.sysLog("check update beta");
        newVersion = true;
        String url = searchUrl;
        ProgInfo progInfo = new ProgInfo();

        if (!retrieveInfos(progInfo, url, showAlwaysInfo, showError)) {
            return false;
        }
        int version = progInfo.getProgVersion();
        int build = progInfo.getProgBuildNo();

        // gibts eine Beta?
        if (version < 0 || build < 0) {
            // dann hats eine neue Version gegeben und gibt noch kein beta
            newVersion = false;
        }

        // mit Programmversion vergleichen
        if (version < progVersion || version == progVersion && build <= progBuild) {
            // dann ist die beta <= aktuelle Programmversion
            newVersion = false;
        }

        // mit vorher schon gemeldeten vergleichen
        if (version < lastVersion.get() || version == lastVersion.get() && build <= lastBuildNo.get()) {
            // dann wurde schon mal gemeldet
            newVersion = false;
        }

        if (newVersion) {
            // dann ist eine neue beta
            PLog.sysLog("gibt eine neue beta");
            PLog.sysLog("  Version: " + version);
            PLog.sysLog("    build: " + build);
            lastVersion.set(version);
            lastBuildNo.set(build);
        }

        if (newVersion || showAlwaysInfo) {
            if (version > progVersion || version == progVersion && build > progBuild) {
                // dann ist die beta aktueller alse die momentane Programmversion
                // wurde abe evtl. schon mal gemeldet und desswegen noch ein check
                newVersion = true;
            }
            Platform.runLater(() -> new InfoBetaUpdateAlert(stage, progInfo, newVersion, searchForUpdateInfos).showInfoAlert(
                    "Programminfos", (newVersion ? "Neue beta Version verfügbar" : "Infos")));
        }

        return newVersion;
    }

}
