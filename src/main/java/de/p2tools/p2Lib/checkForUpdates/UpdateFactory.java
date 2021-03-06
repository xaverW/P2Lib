/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.checkForUpdates;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UpdateFactory {

    private static final String UPDATE_SEARCH_TITLE = "Software-Aktualisierung";
    private static final String UPDATE_ERROR_MESSAGE =
            "Es ist ein Fehler bei der Softwareaktualisierung aufgetreten." + P2LibConst.LINE_SEPARATOR
                    + "Die aktuelle Version konnte nicht ermittelt werden.";


    private UpdateFactory() {
    }


    static boolean checkVersion(ProgUpdateData progUpdateData, int progVersion,
                                ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList, IntegerProperty lastInfoNr) {
        // prüft auf neue Version oder neue Infos
        boolean newVersion = false;

        // nach neuen Infos suchen
        if (lastInfoNr != null) {
            int lInfoNr = lastInfoNr.get();
            for (ProgUpdateInfoData progUpdateInfoData : progUpdateData.getInfos()) {
                if (progUpdateInfoData.getInfoNr() > lInfoNr) {
                    lastInfoNr.setValue(progUpdateInfoData.getInfoNr());
                    newProgUpdateInfoDataList.add(progUpdateInfoData);
                }
            }
        }

        // nach regulärem Update suchen
        int version = progUpdateData.getProgVersion();
        if (version > progVersion) {
            // dann gibts eine neue Version
            newVersion = true;
            PLog.sysLog("gibt eine neue Version: " + progUpdateData.getProgVersion());
        }

        return newVersion;
    }

    static boolean checkVersionNotShown(ProgUpdateData progUpdateData, int progVersion, ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
                                        IntegerProperty lastVersion) {
        // prüft auf neue Version und noch nicht angezeigt
        boolean showNewVersion = false;

        int version = progUpdateData.getProgVersion();
        if (lastVersion != null &&
                (!newProgUpdateInfoDataList.isEmpty() ||
                        version > progVersion && version > lastVersion.get())) {
            showNewVersion = true;
        }

        return showNewVersion;
    }

    static boolean checkBeta(ProgUpdateData progUpdateData, int progVersion, int progBuild) {
        // prüft auf neue BETA-Version
        boolean newVersion = false;

        int version = progUpdateData.getProgVersion();
        int build = progUpdateData.getProgBuildNo();

        // gibts eine neue Beta?
        if ((version > 0 && build >= 0) &&
                (version > progVersion || version == progVersion && build > progBuild)) {
            // sonst hats eine neue Version gegeben und gibt noch kein beta oder
            // die beta <= aktuelle Programmversion
            newVersion = true;
        }

        if (newVersion) {
            // dann ist eine neue beta
            PLog.sysLog("gibt eine neue beta");
            PLog.sysLog("  Version: " + version);
            PLog.sysLog("    build: " + build);
        }

        return newVersion;
    }

    static boolean checkVersionBetaNotShown(ProgUpdateData progUpdateData, int progVersion, int progBuild,
                                            IntegerProperty lastVersion, IntegerProperty lastBuildNo) {
        // prüft ob neue BETA-Version noch nicht gemeldet
        boolean showNewVersion = false;

        int version = progUpdateData.getProgVersion();
        int build = progUpdateData.getProgBuildNo();

        // gibts eine neue nicht gemeldete Beta?
        if (version > 0 && build >= 0 &&
                lastVersion != null && lastBuildNo != null &&
                (version > progVersion && version > lastVersion.get()
                        || version == progVersion && build > progBuild && build > lastBuildNo.get())) {
            // sonst hats eine neue Version gegeben und gibt noch kein beta oder
            // die beta <= aktuelle Programmversion oder schon angezeigt
            showNewVersion = true;
        }

        return showNewVersion;
    }

    static boolean retrieveInfos(Stage stage, ProgUpdateData progUpdateData, String url, boolean showError) {
        // prüft of die Suche nach Updateinfos geklappt hat

        if (progUpdateData == null || url.isEmpty()) {
            return false;
        }

        if (!RetrieveProgUpdateData.retrieveProgramInformation(progUpdateData, url)) {
            progUpdateData = null;
        }

        if (progUpdateData == null || progUpdateData.getProgVersion() < 0) {
            // wenn Version < 0 hat was nicht geklappt
            PLog.errorLog(978451203, "Das Suchen nach einem Programmupdate hat nicht geklappt!");

            if (showError) {
                // dann konnte die "Version" im xml nicht geparst werden
                Platform.runLater(() -> new PAlert().showErrorAlert(stage, "Fehler", UPDATE_SEARCH_TITLE, UPDATE_ERROR_MESSAGE));
            }
            return false;
        }

        return true;
    }
}
