/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.tools;

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PAlertFileChosser;
import de.p2tools.p2Lib.tools.log.PLog;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private final static int WIN_MAX_PATH_LENGTH = 250;
    private final static int X_MAX_NAME_LENGTH = 255;

    /**
     * Return the path to the user´s home directory.
     *
     * @return String to the user´s home directory.
     */
    public static String getHomePath() {
        return System.getProperty("user.home");
    }

    public static int countFilesInDirectory(String directory) {
        File dir = new File(directory);
        return countFilesInDirectory(dir);
    }

    public static int countFilesInDirectory(File directory) {
        int count = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                count++;
            }
            if (file.isDirectory()) {
                count += countFilesInDirectory(file);
            }
        }
        return count;
    }

    public static String addsPath(String path1, String path2) {
        final String ret = concatPaths(path1, path2);
        if (ret.isEmpty()) {
            PLog.errorLog(283946015, path1 + " - " + path2);
        }
        return ret;
    }

    public static boolean movePath(String from, String to) {
        if (from.isEmpty()) {
            new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Quellverzeichnis:" + PConst.LINE_SEPARATOR +
                    to + PConst.LINE_SEPARATORx2 +
                    "ist kein Verzeichnis");
            return false;
        }

        if (to.isEmpty()) {
            new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Zielverzeichnis:" + PConst.LINE_SEPARATOR +
                    to + PConst.LINE_SEPARATORx2 +
                    "ist kein Verzeichnis");
            return false;
        }

        try {
            Path src = Paths.get(from);
            Path dest = Paths.get(to);

            if (dest.toFile().exists() && !dest.toFile().isDirectory()) {
                new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Zielverzeichnis:" + PConst.LINE_SEPARATOR +
                        to + PConst.LINE_SEPARATORx2 +
                        "ist kein Verzeichnis");
                return false;
            }

            if (dest.toFile().exists() && dest.toFile().isDirectory() && dest.toFile().list().length > 0) {
                new PAlert().showErrorAlert("Verzeichnis verschieben",
                        "Das Zielverzeichnis:" + PConst.LINE_SEPARATOR + to + PConst.LINE_SEPARATORx2 +
                                "existiert bereits und ist nicht leer!");
                return false;
            }

            if (dest.toFile().exists() && dest.toFile().isDirectory() && dest.toFile().list().length == 0) {
                dest.toFile().delete();
            }

            org.apache.commons.io.FileUtils.moveDirectory(src.toFile(), dest.toFile());
        } catch (IOException ex) {
            PLog.errorLog(645121047, "move path: " + from + " to " + to);
            return false;
        }
        return true;
    }

    public static String concatPaths(String path1, String path2) {
        String ret;

        if (path1 == null || path2 == null) {
            return "";
        }
        if (path1.isEmpty() || path2.isEmpty()) {
            return path1 + path2;
        }

        if (path1.endsWith(File.separator)) {
            ret = path1.substring(0, path1.length() - 1);
        } else {
            ret = path1;
        }
        if (path2.charAt(0) == File.separatorChar) {
            ret += path2;
        } else {
            ret += File.separator + path2;
        }
        return ret;
    }

    public static String[] checkLengthPath(String[] pathName) {
        if (SystemUtils.IS_OS_WINDOWS) {
            // in Win dürfen die Pfade nicht länger als 260 Zeichen haben (für die Infodatei kommen noch
            // ".txt" dazu)
            if ((pathName[0].length() + 10) > WIN_MAX_PATH_LENGTH) {
                // es sollen für den Dateinamen mind. 10 Zeichen bleiben
                PLog.errorLog(102036598, "Pfad zu lang: " + pathName[0]);
                pathName[0] = getHomePath();
            }
            if ((pathName[0].length() + pathName[1].length()) > WIN_MAX_PATH_LENGTH) {
                PLog.errorLog(902367369, "Name zu lang: " + pathName[0]);
                final int maxNameL = WIN_MAX_PATH_LENGTH - pathName[0].length();
                pathName[1] = cutName(pathName[1], maxNameL);
            }
        } else // für X-Systeme
            if ((pathName[1].length()) > X_MAX_NAME_LENGTH) {
                PLog.errorLog(823012012, "Name zu lang: " + pathName[1]);
                pathName[1] = cutName(pathName[1], X_MAX_NAME_LENGTH);
            }
        return pathName;
    }

    public static String cutName(String name, int length) {
        if (name.length() > length) {
            name = name.substring(0, length - 4) + name.substring(name.length() - 4);
        }
        return name;
    }

    public static boolean istUrl(String fileUrl) {
        return fileUrl.startsWith("http") || fileUrl.startsWith("www");
    }

    public static String getFileName(String path) {
        // Dateinamen einer URL extrahieren
        String ret = "";
        if (path != null) {
            if (!path.isEmpty()) {
                ret = path.substring(path.lastIndexOf('/') + 1);
            }
        }
        if (ret.contains("?")) {
            ret = ret.substring(0, ret.indexOf('?'));
        }
        if (ret.contains("&")) {
            ret = ret.substring(0, ret.indexOf('&'));
        }
        if (ret.isEmpty()) {
            PLog.errorLog(395019631, path);
        }
        return ret;
    }

    public static String getHash(String path) {
        // Hash eines Dateinamens zB. 1433245578
        int h = path.hashCode(); // kann auch negativ sein
        h = Math.abs(h);
        String hh = h + "";
        while (hh.length() < 10) {
            hh = '0' + hh;
        }
        return hh;
    }

    public static String getFileNameWithoutSuffix(String path) {
        // Suffix einer URL extrahieren
        // "http://ios-ondemand.swr.de/i/swr-fernsehen/bw-extra/20130202/601676.,m,s,l,.mp4.csmil/index_2_av.m3u8?e=b471643725c47acd"
        // FILENAME.SUFF
        String ret = "";
        if (path != null) {
            if (!path.isEmpty() && path.contains(".")) {
                ret = path.substring(0, path.lastIndexOf('.'));
            }
        }
        if (ret.isEmpty()) {
            ret = path;
            PLog.errorLog(945123647, path);
        }
        return ret;
    }

    public static String getFileNameSuffix(String path) {
        // Suffix einer Pfad/Dateinamen extrahieren
        // FILENAME.SUFF
        String ret = "";
        if (path != null) {
            if (!path.isEmpty() && path.contains(".")) {
                ret = path.substring(path.lastIndexOf('.') + 1);
            }
        }
        if (ret.isEmpty()) {
            ret = path;
            PLog.errorLog(802103647, path);
        }
        return ret;
    }

    public static String getPath(String path) {
        // Pfad aus Pfad/File extrahieren
        String ret = "";
        if (path != null) {
            if (!path.isEmpty() && path.contains(File.separator)) {
                ret = path.substring(0, path.lastIndexOf(File.separator));
            }
        }
        if (ret.isEmpty()) {
            ret = path;
            PLog.errorLog(915201236, path);
        }
        return ret;
    }

    public static String getNextFileName(String path, String selName, String suff) {
        String ret = "";

        Path dir = Paths.get(path);
        if (!Files.exists(dir)) {
            return selName;
        }

        String name = getName(selName, suff);
        Path baseDirectoryPath = Paths.get(path, name);
        int nr = 1;

        while (Files.exists(baseDirectoryPath)) {
            name = getName(selName + "_" + nr++, suff);
            baseDirectoryPath = Paths.get(path, name);
        }

        ret = baseDirectoryPath.getFileName().toString();
        return ret;
    }

    private static String getName(String name, String suff) {
        return name + "." + suff;
    }

    /**
     * Get the free disk space for a selected path.
     *
     * @return Free disk space in bytes.
     */
    public static long getFreeDiskSpace(final String strPath) {
        long usableSpace = 0;
        if (!strPath.isEmpty()) {
            try {
                Path path = Paths.get(strPath);
                if (!Files.exists(path)) {
                    path = path.getParent();
                }
                final FileStore fileStore = Files.getFileStore(path);
                usableSpace = fileStore.getUsableSpace();
            } catch (final Exception ignore) {
            }
        }
        return usableSpace;
    }

    public static boolean deleteFile(String strFile) {
        boolean ret = false;
        try {
            File file = new File(strFile);
            if (!file.exists()) {
                new PAlertFileChosser().showErrorAlert("Datei löschen", "", "Die Datei existiert nicht!");
                return false;
            }

            if (new PAlertFileChosser().showAlert("Datei Löschen?", "", "Die Datei löschen:" + PConst.LINE_SEPARATORx2 + strFile)) {

                // und jetzt die Datei löschen
                PLog.sysLog(new String[]{"Datei löschen: ", file.getAbsolutePath()});
                if (!file.delete()) {
                    throw new Exception();
                }
                ret = true;
            }
        } catch (Exception ex) {
            ret = false;
            new PAlertFileChosser().showErrorAlert("Datei löschen",
                    "Konnte die Datei nicht löschen!", "Fehler beim löschen von:" + PConst.LINE_SEPARATORx2 +
                            strFile);
            PLog.errorLog(912036547, "Fehler beim löschen: " + strFile);
        }
        return ret;
    }

    public static boolean deleteFileNoMsg(String strFile) {
        boolean ret = false;
        try {
            File file = new File(strFile);
            if (!file.exists()) {
                new PAlertFileChosser().showErrorAlert("Datei löschen", "", "Die Datei existiert nicht!");
                return false;
            }

            // und jetzt die Datei löschen
            PLog.sysLog(new String[]{"Datei löschen: ", file.getAbsolutePath()});
            if (!file.delete()) {
                throw new Exception();
            }
            ret = true;

        } catch (Exception ex) {
            ret = false;
            new PAlertFileChosser().showErrorAlert("Datei löschen",
                    "Konnte die Datei nicht löschen!", "Fehler beim löschen von:" + PConst.LINE_SEPARATORx2 +
                            strFile);
            PLog.errorLog(912036547, "Fehler beim löschen: " + strFile);
        }
        return ret;
    }

}
