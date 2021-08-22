/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.checkForActInfos;

import de.p2tools.p2Lib.tools.log.PLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FoundAllFiles {

    private FoundAllFiles() {
    }

    public static void found(FoundSearchData foundSearchData) {
        try (InputStreamReader in = new InputStreamReader(
                FoundFactory.connectToServer(foundSearchData.getSearchUrlDownload()), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(in)) {

            foundSearchData.setFoundNewInfo(false);
            foundSearchData.setFoundNewVersion(false);
            foundSearchData.setFoundNewBeta(false);
            foundSearchData.setFoundNewDaily(false);

            ArrayList<String> log = new ArrayList<>();
            log.add("=====================================");
            log.add("Programmversionen auf aktuelles build-date heben");
            log.add("Aktuelle Version: " + foundSearchData.getLastActDate());
            if (foundSearchData.getLastActDate().isEmpty() ||
                    FoundFactory.isNewFound(foundSearchData.getLastActDate(), foundSearchData.getProgBuildDate(), log)) {
                foundSearchData.setLastActDate(foundSearchData.getProgBuildDate());
            }

            log.add("Beta-Version: " + foundSearchData.getLastBetaDate());
            if (foundSearchData.getLastBetaDate().isEmpty() ||
                    FoundFactory.isNewFound(foundSearchData.getLastBetaDate(), foundSearchData.getProgBuildDate(), log)) {
                foundSearchData.setLastBetaDate(foundSearchData.getProgBuildDate());
            }

            log.add("Daily: " + foundSearchData.getLastDailyDate());
            if (foundSearchData.getLastDailyDate().isEmpty() ||
                    FoundFactory.isNewFound(foundSearchData.getLastDailyDate(), foundSearchData.getProgBuildDate(), log)) {
                foundSearchData.setLastDailyDate(foundSearchData.getProgBuildDate());
            }
            PLog.sysLog(log);

            String strLine;
            while ((strLine = br.readLine()) != null) {
                //<p><a href="/download/mtplayer/beta/MTPlayer-9-208__2021.02.16.zip">MTPlayer-9-208__2021.02.16.zip</a></p>
                if (!strLine.contains("<a href=\"/download/")) {
                    continue; //nur Downloads
                }
                if (!strLine.contains(foundSearchData.getProgName())) {
                    continue; //und nur vom verwendetem Programm
                }

                if (strLine.contains("info")) {
                    //<p><a href="/download/info/MTPlayer__2021.07.18.txt">MTPlayer__2021.07.18.txt</a>,</p>
                    addInfo(foundSearchData, strLine);

                } else if (strLine.contains("act")) {
                    //<p><a href="/download/act/P2Radio-3__Linux+Java.zip">P2Radio-3__Linux+Java.zip</a>,</p>
                    addAct(foundSearchData, strLine);

                } else if (strLine.contains("beta")) {
                    //<p><a href="/download/beta/MTPlayer-10__2021.07.13.zip">MTPlayer-10__2021.07.13.zip</a>,</p>
                    addBeta(true, foundSearchData, strLine);

                } else if (strLine.contains("daily")) {
                    //<p><a href="/download/daily/P2Radio-2__2021.07.11.zip">P2Radio-2__2021.07.11.zip</a></p>
                    addBeta(false, foundSearchData, strLine);
                }
                System.out.println(strLine.trim());
            }
        } catch (final IOException ex) {
            PLog.errorLog(885692213, ex);
        }
    }

    private static void addInfo(FoundSearchData foundSearchData, String strLine) {
        //<p><a href="/download/p2radio/info/P2Radio__2021.07.20.txt">P2Radio__2021.07.20.txt</a></p>
        //<p><a href="/download/p2radio/info/P2Radio__2021.07.20_1.txt">P2Radio__2021.07.20_1.txt</a></p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchData.getSearchUrl() + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);
            foundFile.setFileDate(fileName.
                    substring(fileName.indexOf("__") + "__".length(), fileName.lastIndexOf(".")));

            if (foundSearchData.isShowAllways() ||
                    (!foundSearchData.isShowAllways() &&
                            FoundFactory.isNewFound(foundSearchData.getLastInfoDate(), foundFile.getFileDate()))) {
                //Infos sind vorhanden
                //-> die noch nicht angezeigt wurde ODER
                //-> soll immer angezeigt werden, alle!

                foundSearchData.setFoundNewInfo(true);
                foundSearchData.setNewInfoDate(foundFile.getFileDate());
                foundFile.setFileText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                foundSearchData.getFoundFileListInfo().add(foundFile);
                System.out.println("\n" + "addInfo: \n" + foundFile.getFileUrl() + "\n" +
                        fileName + "\n" + foundFile.getFileDate());
            }
        }
    }

    private static void addAct(FoundSearchData foundSearchData, String strLine) {
        //<p><a href="/download/p2radio/act/P2Radio-3__2021.07.14.zip">P2Radio-3__2021.07.14.zip</a></p>
        //<p><a href="/download/p2radio/act/P2Radio-3__Linux+Java__2021.07.14.zip">P2Radio-3__Linux+Java__2021.07.14.zip</a></p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchData.getSearchUrl() + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);

            //Version ermitteln
            String newVersionNo = "";
            if (fileName.contains("-") && fileName.contains("__")) {
                newVersionNo = fileName.substring(fileName.indexOf("-") + "-".length(),
                        fileName.indexOf("__"));
            }

            if (fileName.contains("__")) {
                //P2Radio-3__Linux+Java__2021.07.14.zip
                //P2Radio-3__2021.07.14.zip
                foundFile.setFileDate(fileName.substring(fileName.lastIndexOf("__") + "__".length(),
                        fileName.lastIndexOf(".")));
            }

            if (fileName.endsWith(".txt")) {
                //Infofile
                foundSearchData.setNewVersionText(FoundFactory.getInfoFile(foundFile.getFileUrl()));

            } else if ((foundSearchData.isShowAllways() &&
                    FoundFactory.isNewFound(foundSearchData.getProgBuildDate(), foundFile.getFileDate())) ||
                    (!foundSearchData.isShowAllways() &&
                            FoundFactory.isNewFound(foundSearchData.getLastActDate(), foundFile.getFileDate()))) {
                //ist eine neue Version
                //-> die noch nicht angezeigt wurde ODER
                //-> soll immer angezeigt werden
                foundSearchData.setNewVersionNo(newVersionNo);
                foundSearchData.setFoundNewVersion(true);
                foundSearchData.setNewVersionDate(foundFile.getFileDate());
                foundSearchData.getFoundFileListAct().add(foundFile);
                System.out.println("\n" + "addAct: \n" + foundFile.getFileUrl() + "\n" +
                        fileName + "\n" + foundFile.getFileDate());
            }
        }
    }

    private static void addBeta(boolean beta, FoundSearchData foundSearchData, String strLine) {
        //<p><a href="/download/p2radio/beta/P2Radio-2__2021.07.10.zip">P2Radio-2__2021.07.10.zip</a></p>
        //<p><a href="/download/p2radio/beta/P2Radio-2__Linux+Java__2021.07.10.zip">P2Radio-2__Linux+Java__2021.07.10.zip</a></p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(foundSearchData.getSearchUrl() + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);
            foundFile.setFileDate(fileName.
                    substring(fileName.lastIndexOf("__") + "__".length(), fileName.lastIndexOf(".")));

            if (fileName.endsWith(".txt")) {
                //Infofile
                if (beta) {
                    foundSearchData.setNewBetaText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                } else {
                    foundSearchData.setNewDailyText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                }

            } else {
                //Programmdatei
                if (beta) {
                    //beta
                    if ((foundSearchData.isShowAllways() &&
                            FoundFactory.isNewFound(foundSearchData.getProgBuildDate(), foundFile.getFileDate())) ||
                            (!foundSearchData.isShowAllways() &&
                                    FoundFactory.isNewFound(foundSearchData.getLastBetaDate(), foundFile.getFileDate()))) {
                        //ist eine neue Version
                        //-> die noch nicht angezeigt wurde ODER
                        //-> soll immer angezeigt werden

                        foundSearchData.setFoundNewBeta(true);
                        foundSearchData.setNewBetaDate(foundFile.getFileDate());
                        foundSearchData.getFoundFileListBeta().add(foundFile);
                        System.out.println("\n" + "addBeta: \n" + foundFile.getFileUrl() + "\n" +
                                fileName + "\n" + foundFile.getFileDate());
                    }

                } else {
                    //daily
                    if ((foundSearchData.isShowAllways() &&
                            FoundFactory.isNewFound(foundSearchData.getProgBuildDate(), foundFile.getFileDate())) ||
                            (!foundSearchData.isShowAllways() &&
                                    FoundFactory.isNewFound(foundSearchData.getLastDailyDate(), foundFile.getFileDate()))) {
                        //ist eine neue Version
                        //-> die noch nicht angezeigt wurde ODER
                        //-> soll immer angezeigt werden

                        foundSearchData.setFoundNewDaily(true);
                        foundSearchData.setNewDailyDate(foundFile.getFileDate());
                        foundSearchData.getFoundFileListDaily().add(foundFile);
                        System.out.println("\n" + "addDaily: \n" + foundFile.getFileUrl() + "\n" +
                                fileName + "\n" + foundFile.getFileDate());
                    }
                }
            }
        }
    }
}
