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

import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.log.P2Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class FoundAllFiles {

    static LocalDate maxFoundDate = LocalDate.EPOCH;

    private FoundAllFiles() {
    }

    static void found(FoundSearchDataDTO foundSearchDataDTO) {
        // hier wird gesucht und das Datum "letzte Suche" gesetzt: Datum vom aktuellstem File
        maxFoundDate = LocalDate.EPOCH; // zurücksetzen
        try (InputStreamReader in = new InputStreamReader(
                FoundFactory.connectToServer(foundSearchDataDTO.getSearchUrlDownload()), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(in)) {

            foundSearchDataDTO.setFoundNewInfo(false);
            foundSearchDataDTO.setFoundNewVersion(false);
            foundSearchDataDTO.setFoundNewBeta(false);
            foundSearchDataDTO.setFoundNewDaily(false);

            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (!strLine.contains("<a href=\"/download/")) {
                    continue; //nur Downloads
                }
                if (!strLine.contains(foundSearchDataDTO.getProgName())) {
                    continue; //und nur vom verwendetem Programm
                }
                try {
                    if (strLine.contains("info")) {
                        addInfo(foundSearchDataDTO, strLine);

                    } else if (strLine.contains("act")) {
                        addAct(foundSearchDataDTO, strLine);

                    } else if (strLine.contains("beta")) {
                        addBetaDaily(true, foundSearchDataDTO, strLine);

                    } else if (strLine.contains("daily")) {
                        addBetaDaily(false, foundSearchDataDTO, strLine);
                    }
                } catch (Exception ex) {
                    P2Log.errorLog(102548790, ex);
                }
            }
        } catch (final IOException ex) {
            P2Log.errorLog(201312587, ex);
        }
    }

    private static void addInfo(FoundSearchDataDTO foundSearchDataDTO, String strLine) {
        // <p><a href="/download/mtplayer/info/MTPlayer-Info__2023.03.07.txt">MTPlayer-Info__2023.03.07.txt</a></p>

        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");
        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");
        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchDataDTO.getSearchUrl() + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);
            foundFile.setFileDate(fileName.
                    substring(fileName.indexOf("__") + "__".length(), fileName.lastIndexOf(".")));

            if (checkInfo(foundSearchDataDTO, foundFile)) {
                //Infos sind vorhanden
                //-> die noch nicht angezeigt wurde ODER
                //-> soll immer angezeigt werden, alle!
                P2Log.debugLog("Infofile gefunden: " + foundFile.getFileName() + "  --  " + foundFile.getFileUrl());
                foundSearchDataDTO.setFoundNewInfo(true);
                foundSearchDataDTO.setNewInfoDate(foundFile.getFileDate());
                foundFile.setFileText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                foundSearchDataDTO.getFoundFileListInfo().add(foundFile);
            }
            setLastFoundDate(foundSearchDataDTO, foundFile); // und erst danach LastSearchDate setzen
        }
    }

    private static void addAct(FoundSearchDataDTO foundSearchDataDTO, String strLine) {
        // BETA haben schon die V-Nummer!!
        //<p><a href="/download/p2info/act/P2Radio-3__2021.07.14.zip">P2Radio-3__2021.07.14.zip</a></p>
        //<p><a href="/download/p2info/act/P2Radio-3__Linux+Java__2021.07.14.zip">P2Radio-3__Linux+Java__2021.07.14.zip</a></p>

        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");
        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");
        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchDataDTO.getSearchUrl() + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);

            //Version ermitteln
            if (fileName.contains("-") && fileName.contains("__")) {
                foundFile.setFileVersion(fileName.substring(fileName.indexOf("-") + "-".length(),
                        fileName.indexOf("__")));
            }

            if (fileName.contains("__")) {
                //P2Radio-3__Linux+Java__2021.07.14.zip
                //P2Radio-3__2021.07.14.zip
                foundFile.setFileDate(fileName.substring(fileName.lastIndexOf("__") + "__".length(),
                        fileName.lastIndexOf(".")));
            }

            if (fileName.endsWith(".txt")) {
                // Infofile zum Download
                P2Log.debugLog("Infofile laden: " + foundFile.getFileName() + "  --  " + foundFile.getFileUrl());
                foundSearchDataDTO.setNewVersionText(FoundFactory.getInfoFile(foundFile.getFileUrl()));

            } else {
                // act-File
                if (checkFile(foundSearchDataDTO, foundFile)) {
                    //ist eine neue Version
                    //-> die noch nicht angezeigt wurde ODER
                    //-> soll immer angezeigt werden
                    P2Log.debugLog("Act gefunden: " + foundFile.getFileName() + "  --  " + foundFile.getFileUrl());
                    foundSearchDataDTO.setNewVersionNo(foundFile.getFileVersion());
                    foundSearchDataDTO.setFoundNewVersion(true);
                    foundSearchDataDTO.setNewVersionDate(foundFile.getFileDate());
                    foundSearchDataDTO.getFoundFileListAct().add(foundFile);
                }
                setLastFoundDate(foundSearchDataDTO, foundFile); // und erst danach LastSearchDate setzen
            }
        }
    }

    private static void addBetaDaily(boolean beta, FoundSearchDataDTO foundSearchDataDTO, String strLine) {
        // <p><a href="/download/mtplayer/daily/MTPlayer-13-142__2023.05.02.txt">MTPlayer-13-142__2023.05.02.txt</a></p>
        // <p><a href="/download/mtplayer/daily/MTPlayer-13-142__2023.05.02.zip">MTPlayer-13-142__2023.05.02.zip</a></p>

        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");
        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");
        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchDataDTO.getSearchUrl() + strLine.substring(idx1, idx2));

            //MTPlayer-13-142__2023.05.02.zip
            //MTInfo-11-1__Linux+Java__2021.10.09.zip
            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);

            foundFile.setFileVersion(fileName.
                    substring(fileName.indexOf("-") + "-".length(), fileName.lastIndexOf("-")));

            String buildNo = fileName.
                    substring(fileName.lastIndexOf("-") + "-".length(), fileName.lastIndexOf("__"));
            if (buildNo.contains("__")) {
                buildNo = buildNo.substring(0, buildNo.lastIndexOf("__"));
            }
            foundFile.setFileBuildNo(buildNo);

            foundFile.setFileDate(fileName.substring(fileName.lastIndexOf("__") + "__".length(),
                    fileName.lastIndexOf(".")));

            if (fileName.endsWith(".txt")) {
                //Infofile zum Download beta/daily
                if (beta) {
                    foundSearchDataDTO.setNewBetaText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                } else {
                    foundSearchDataDTO.setNewDailyText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                }

            } else {
                // Programmdatei beta/daily
                if (checkFile(foundSearchDataDTO, foundFile)) {
                    // ist eine neue Version
                    // -> die noch nicht angezeigt wurde ODER
                    // -> soll immer angezeigt werden
                    if (beta) {
                        // beta
                        P2Log.debugLog("Beta gefunden: " + foundFile.getFileName() + "  --  " + foundFile.getFileUrl());
                        foundSearchDataDTO.setFoundNewBeta(true);
                        foundSearchDataDTO.setNewBetaVersion(foundFile.getFileVersion());
                        foundSearchDataDTO.setNewBetaBuildNo(foundFile.getFileBuildNo());
                        foundSearchDataDTO.setNewBetaDate(foundFile.getFileDate());
                        foundSearchDataDTO.getFoundFileListBeta().add(foundFile);

                    } else {
                        // daily
                        P2Log.debugLog("Daily gefunden: " + foundFile.getFileName() + "  --  " + foundFile.getFileUrl());
                        foundSearchDataDTO.setFoundNewDaily(true);
                        foundSearchDataDTO.setNewDailyVersion(foundFile.getFileVersion());
                        foundSearchDataDTO.setNewDailyBuild(foundFile.getFileBuildNo());
                        foundSearchDataDTO.setNewDailyDate(foundFile.getFileDate());
                        foundSearchDataDTO.getFoundFileListDaily().add(foundFile);
                    }
                }
                setLastFoundDate(foundSearchDataDTO, foundFile); // und erst danach LastSearchDate setzen
            }
        }
    }

    private static void setLastFoundDate(FoundSearchDataDTO foundSearchDataDTO, FoundFile foundFile) {
        // LocalDate lDate = P2LDateFactory.fromStringR(foundSearchDataDTO.getLastSearchDate());
        LocalDate ld = P2LDateFactory.fromStringR(foundFile.getFileDate());
        if (ld.isAfter(maxFoundDate)) {
            maxFoundDate = ld;
        }
    }

    private static boolean checkInfo(FoundSearchDataDTO foundSearchDataDTO, FoundFile foundFile) {
        // Info-File prüfen, obs angezeigt werden soll
        if (foundSearchDataDTO.isShowAllDownloads()) {
            // dann immer anzeigen
            return true;
        }

        return foundSearchDataDTO.isShowDialogAlways() || /* dann auch immer */
                (!foundSearchDataDTO.isShowDialogAlways() && /* sonst nur, wenn vorhanden */
                        FoundFactory.isNewFound(foundSearchDataDTO.getLastFoundDate(), foundFile.getFileDate()));
    }

    private static boolean checkFile(FoundSearchDataDTO foundSearchDataDTO, FoundFile foundFile) {
        // Act/Beta/Daily-File prüfen, obs angezeigt werden soll
        if (foundSearchDataDTO.isShowAllDownloads()) {
            // dann immer anzeigen
            return true;
        }

        boolean ret = (foundSearchDataDTO.isShowDialogAlways() && /* immer, wenn was vorhanden ist */
                FoundFactory.isNewFound(foundSearchDataDTO.getProgBuildDate(), foundFile.getFileDate())) ||
                (!foundSearchDataDTO.isShowDialogAlways() && /* dann nur, wenn noch nicht angezeigt */
                        FoundFactory.isNewFound(foundSearchDataDTO.getLastFoundDate(), foundFile.getFileDate()));

        if (ret) {
//            // dann gibts eins das noch nicht angezeigt wurde, noch version/release prüfen -> warum???
//            try {
//                int actVersion = Integer.parseInt(foundSearchDataDTO.getProgVersion());
//                int fileVersion = Integer.parseInt(foundFile.getFileVersion());
//
//                if (foundFile.getFileBuildNo().isEmpty()) {
//                    // dann ist ein act
//                    if (actVersion >= fileVersion) {
//                        // dann ist es gleich oder aktueller
//                        ret = false;
//                    }
//
//                } else {
//                    // beta/daily
//                    int actBuild = Integer.parseInt(foundSearchDataDTO.getProgBuildNo());
//                    int fileBuild = Integer.parseInt(foundFile.getFileBuildNo());
//                    if (actVersion > fileVersion) {
//                        // dann ist es aktueller
//                        ret = false;
//                    }
//
//                    if (actVersion == fileVersion &&
//                            actBuild >= fileBuild) {
//                        // dann ist es gleich oder aktueller
//                        ret = false;
//                    }
//                }
//            } catch (Exception ignore) {
//                ret = true;
//            }
        }

        return ret;
    }
}
