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

public class FoundAllFiles {

    public static void found(FoundSearchData foundSearchData) {
        try (InputStreamReader in = new InputStreamReader(
                FoundFactory.connectToServer(FoundFactory.SEARCH_URL_DOWNLOAD), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(in)) {

            foundSearchData.setFoundNewInfo(false);
            foundSearchData.setFoundNewVersion(false);
            foundSearchData.setFoundNewBeta(false);
            foundSearchData.setFoundNewDaily(false);
            if (foundSearchData.getLastAct().isEmpty()) {
                //das ist es mind. :)
                foundSearchData.setLastAct(foundSearchData.getProgVersion());
            }

            String strLine;
            while ((strLine = br.readLine()) != null) {
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
        //<p><a href="/download/info/MTPlayer__2021.07.18.txt">MTPlayer__2021.07.18.txt</a>,</p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(FoundFactory.SEARCH_URL + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);
            foundFile.setFileVersion(fileName.
                    substring(fileName.indexOf("__") + "__".length(), fileName.lastIndexOf(".")));

            if (FoundFactory.isNewFound(foundSearchData.getLastInfo(), foundFile.getFileVersion())) {
                foundSearchData.setFoundNewInfo(true);
                foundFile.setFileText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                foundSearchData.getFoundFileListInfo().add(foundFile);
                System.out.println("\n" + "addInfo: \n" + foundFile.getFileUrl() + "\n" +
                        fileName + "\n" + foundFile.getFileVersion());
            }
        }
    }

    private static void addAct(FoundSearchData foundSearchData, String strLine) {
        //<p><a href="/download/act/P2Radio-3__Linux+Java.zip">P2Radio-3__Linux+Java.zip</a>,</p>
        //<p><a href="/download/act/P2Radio-3.zip">P2Radio-3.zip</a>,</p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(FoundFactory.SEARCH_URL + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);

            //Version ermitteln
            if (fileName.contains("__")) {
                //P2Radio-3__Windows+Java.zip
                foundFile.setFileVersion(fileName.substring(fileName.indexOf("-") + "-".length(),
                        fileName.lastIndexOf("__")));
            } else {
                //P2Radio-4.zip
                foundFile.setFileVersion(fileName.substring(fileName.indexOf("-") + "-".length(),
                        fileName.lastIndexOf(".")));
            }

            if (fileName.endsWith(".txt")) {
                //Infofile
                foundSearchData.setNewVersionText(FoundFactory.getInfoFile(foundFile.getFileUrl()));

            } else if (FoundFactory.isNewFound(foundSearchData.getLastAct(), foundFile.getFileVersion())) {
                //ist eine neue Version
                foundSearchData.setFoundNewVersion(true);
                foundSearchData.setNewVersionNo(foundFile.getFileVersion());
                foundSearchData.getFoundFileListAct().add(foundFile);
                System.out.println("\n" + "addAct: \n" + foundFile.getFileUrl() + "\n" +
                        fileName + "\n" + foundFile.getFileVersion());
            }
        }
    }

    private static void addBeta(boolean beta, FoundSearchData foundSearchData, String strLine) {
        //<p><a href="/download/beta/MTPlayer-10__2021.07.13.zip">MTPlayer-10__2021.07.13.zip</a>,</p>
        //<p><a href="/download/daily/P2Radio-2__2021.07.11.zip">P2Radio-2__2021.07.11.zip</a></p>
        int idx1 = strLine.indexOf("href=\"");
        int idx2 = strLine.indexOf("\">");

        int idx3 = strLine.indexOf("\">");
        int idx4 = strLine.indexOf("</a>");

        if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0 && idx4 >= 0) {
            idx1 += "href=\"".length();
            idx3 += "\">".length();

            FoundFile foundFile = new FoundFile();
            foundFile.setFileUrl(FoundFactory.SEARCH_URL + strLine.substring(idx1, idx2));

            String fileName = strLine.substring(idx3, idx4);
            foundFile.setFileName(fileName);
            foundFile.setFileVersion(fileName.
                    substring(fileName.indexOf("__") + "__".length(), fileName.lastIndexOf(".")));

            if (fileName.endsWith(".txt")) {
                //Infofile
                if (beta) {
                    foundSearchData.setNewBetaText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                } else {
                    foundSearchData.setNewDailyText(FoundFactory.getInfoFile(foundFile.getFileUrl()));
                }

            } else {
                //Programmdatei
                if (beta && FoundFactory.isNewFound(foundSearchData.getLastBeta(), foundFile.getFileVersion())) {
                    //ist eine neue BETA Version
                    foundSearchData.setFoundNewBeta(true);
                    foundSearchData.setNewBetaNo(foundFile.getFileVersion());
                    foundSearchData.getFoundFileListBeta().add(foundFile);
                    System.out.println("\n" + "addBeta: \n" + foundFile.getFileUrl() + "\n" +
                            fileName + "\n" + foundFile.getFileVersion());

                } else if (FoundFactory.isNewFound(foundSearchData.getLastDaily(), foundFile.getFileVersion())) {
                    //ist eine neue DAILY Version
                    foundSearchData.setFoundNewDaily(true);
                    foundSearchData.setNewDailyNo(foundFile.getFileVersion());
                    foundSearchData.getFoundFileListDaily().add(foundFile);
                    System.out.println("\n" + "addDaily: \n" + foundFile.getFileUrl() + "\n" +
                            fileName + "\n" + foundFile.getFileVersion());
                }
            }
        }
    }
}
