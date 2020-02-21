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


    static boolean checkVersion(ProgInfo progInfo, ArrayList<Infos> newInfosList,
                                int progVersion, IntegerProperty lastInfoNr) {
        // prüft auf neue Version oder neue Infos
        boolean newVersion = false;

        // nach neuen Infos suchen
        if (lastInfoNr != null) {
            int lInfoNr = lastInfoNr.get();
            for (Infos infos : progInfo.getInfos()) {
                if (infos.getInfoNr() > lInfoNr) {
                    lastInfoNr.setValue(infos.getInfoNr());
                    newInfosList.add(infos);
                }
            }
        }

        // nach regulärem Update suchen
        int version = progInfo.getProgVersion();
        if (version > progVersion) {
            // dann gibts eine neue Version
            newVersion = true;
            PLog.sysLog("gibt eine neue Version: " + progInfo.getProgVersion());
        }

        return newVersion;
    }

    static boolean checkVersionNotShown(ProgInfo progInfo, int progVersion, ArrayList<Infos> newInfosList,
                                        IntegerProperty lastVersion) {
        // prüft auf neue Version und noch nicht angezeigt
        boolean showNewVersion = false;

        int version = progInfo.getProgVersion();
        if (lastVersion != null &&
                (!newInfosList.isEmpty() ||
                        version > progVersion && version > lastVersion.get())) {
            showNewVersion = true;
        }

        return showNewVersion;
    }

    static boolean checkBeta(ProgInfo progInfo, int progVersion, int progBuild) {
        // prüft auf neue BETA-Version
        boolean newVersion = false;

        int version = progInfo.getProgVersion();
        int build = progInfo.getProgBuildNo();

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

    static boolean checkVersionBetaNotShown(ProgInfo progInfo, int progVersion, int progBuild,
                                            IntegerProperty lastVersion, IntegerProperty lastBuildNo) {
        // prüft ob neue BETA-Version noch nicht gemeldet
        boolean showNewVersion = false;

        int version = progInfo.getProgVersion();
        int build = progInfo.getProgBuildNo();

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

    static boolean retrieveInfos(Stage stage, ProgInfo progInfo, String url, boolean showError) {
        // prüft of die Suche nach Updateinfos geklappt hat

        if (progInfo == null || url.isEmpty()) {
            return false;
        }

        if (!RetrieveProgInfo.retrieveProgramInformation(progInfo, url)) {
            progInfo = null;
        }

        if (progInfo == null || progInfo.getProgVersion() < 0) {
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
