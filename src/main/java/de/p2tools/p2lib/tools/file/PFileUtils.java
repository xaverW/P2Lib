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

package de.p2tools.p2lib.tools.file;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDialogFileChooser;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PFileUtils {
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

    public static String getParentPath(String path) {
        try {
            File f = new File(path);
            f = f.getParentFile();
            return f.getAbsolutePath();
        } catch (Exception ex) {
            PLog.errorLog(310245789, ex, path);
            new PAlert().showErrorAlert("Elternverzeichnis", "Das Elternverzeichnis von:" + P2LibConst.LINE_SEPARATOR +
                    path + P2LibConst.LINE_SEPARATORx2 +
                    "kann nicht gefunden werden.");
        }
        return "";
    }

    public static String addsPath(String path1, String path2) {
        //!!!!! bei Win/Linux kommen unterschiedliche Strings raus!!!
        // \de\p2tools\filerunner\icon\P2.png
        // /de/p2tools/filerunner/icon/P2.png
        String ret = "";
        try {
//            ret = FilenameUtils.concat(path1, path2);
//            ret = Paths.get(path1, path2).toString();

            ret = Paths.get(path1).resolve(path2).toString();
            if (ret.isEmpty()) {
                PLog.errorLog(283946015, path1 + " - " + path2);
            }
        } catch (Exception ex) {
            PLog.errorLog(375341950, ex, path1 + " - " + path2);
        }
        return ret;
    }

    public static boolean fileExist(String file) {
        if (file == null || file.isEmpty() || !new File(file).exists()) {
            return false;
        }
        return true;
    }

    public static boolean fileExist(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public static boolean fileIsDirectoryExist(String file) {
        if (!fileExist(file)) {
            return false;
        }

        if (!new File(file).isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean movePath(String from, String to) {
        if (from.isEmpty()) {
            new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Quellverzeichnis:" + P2LibConst.LINE_SEPARATOR +
                    from + P2LibConst.LINE_SEPARATORx2 +
                    "ist kein Verzeichnis");
            return false;
        }

        if (to.isEmpty()) {
            new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Zielverzeichnis:" + P2LibConst.LINE_SEPARATOR +
                    to + P2LibConst.LINE_SEPARATORx2 +
                    "ist kein Verzeichnis");
            return false;
        }

        try {
            Path src = Paths.get(from);
            Path dest = Paths.get(to);

            if (dest.toFile().exists() && !dest.toFile().isDirectory()) {
                new PAlert().showErrorAlert("Verzeichnis verschieben", "Das Zielverzeichnis:" + P2LibConst.LINE_SEPARATOR +
                        to + P2LibConst.LINE_SEPARATORx2 +
                        "ist kein Verzeichnis");
                return false;
            }

            if (dest.toFile().exists() && dest.toFile().isDirectory() && dest.toFile().list().length > 0) {
                new PAlert().showErrorAlert("Verzeichnis verschieben",
                        "Das Zielverzeichnis:" + P2LibConst.LINE_SEPARATOR + to + P2LibConst.LINE_SEPARATORx2 +
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

    public static boolean copyFile(Path from, Path dest) {
        return copyFile(from, dest, true);
    }

    public static boolean copyFile(Path from, Path dest, boolean ask) {
        try {

            if (from.toString().isEmpty()) {
                new PAlert().showErrorAlert("Datei kopieren", "Es wurde keine Quelldatei angegeben.");
                return false;
            }

            if (dest.toString().isEmpty()) {
                new PAlert().showErrorAlert("Datei kopieren", "Es wurde keine Zieldatei angegeben.");
                return false;
            }

            if (!from.toFile().exists()) {
                new PAlert().showErrorAlert("Datei kopieren", "Die Quelldatei:" + P2LibConst.LINE_SEPARATOR +
                        from.toString() + P2LibConst.LINE_SEPARATORx2 +
                        "existiert nicht.");
                return false;
            }

            if (dest.toFile().isDirectory()) {
                new PAlert().showErrorAlert("Datei kopieren", "Die Zieldatei:" + P2LibConst.LINE_SEPARATOR +
                        dest.toString() + P2LibConst.LINE_SEPARATORx2 +
                        "ist ein Verzeichnis.");
                return false;
            }

            if (ask && dest.toFile().exists()) {
                PAlert.BUTTON button = PAlert.showAlert_yes_no("Hinweis", "Datei kopieren",
                        "Die Zieldatei existiert bereits:" + P2LibConst.LINE_SEPARATOR +
                                dest.toString() +
                                P2LibConst.LINE_SEPARATORx2 +
                                "Soll die Datei überschrieben werden?");
                if (!button.equals(PAlert.BUTTON.YES)) {
                    return false;
                }
            }

            if (dest.toFile().exists()) {
                dest.toFile().delete();
            }

            org.apache.commons.io.FileUtils.copyFile(from.toFile(), dest.toFile());
        } catch (IOException ex) {
            PLog.errorLog(978451203, "copy file: " + from.toString() + " to " + dest.toString());
            return false;
        }
        return true;
    }

    public static boolean checkFileToCreate(Stage stage, Path pathToCheck) {

        if (pathToCheck.toString().isEmpty()) {
            new PAlert().showErrorAlert(stage, "Datei anlegen", "Es wurde keine Datei angegeben.");
            return false;
        }

        if (pathToCheck.toFile().exists()) {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(stage, "Hinweis", "Datei anlegen",
                    "Die Datei exisiert bereits:" + P2LibConst.LINE_SEPARATOR +
                            pathToCheck.toString() +
                            P2LibConst.LINE_SEPARATORx2 +
                            "Soll die Datei überschrieben werden?");
            if (!button.equals(PAlert.BUTTON.YES)) {
                return false;
            }

            if (!pathToCheck.toFile().delete()) {
                new PAlert().showErrorAlert(stage, "Datei löschen", "Die Datei: \n" +
                        pathToCheck.toFile() + "\n" +
                        "kann nicht gelöscht werden.");
                return false;
            }
            if (pathToCheck.toFile().exists()) {
                // dann konnte die Datei nicht gelöscht werden
                PAlert.showErrorAlert(stage, "Fehler",
                        "Zieldatei existiert bereits!",
                        "Die Zieldatei kann nicht überschrieben werden.");
                return false;
            }
        }

        return true;
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

    public static String getFileNameSuffix(String path) {
        // Suffix eines Pfad/Dateinamen extrahieren
        // FILENAME.SUFF
        String suff = "";
        try {
            suff = FilenameUtils.getExtension(path);
            if (suff == null || suff.isEmpty()) {
                return "";
            }
        } catch (Exception ignore) {
            return "";
        }

        return suff;
    }

    public static String getFileName(String path) {
        // Dateiname eines Pfad/Dateinamen extrahieren
        // /PATH/FILENAME.SUFF
        String name = "";
        try {
            name = FilenameUtils.getName(path);
            if (name == null || name.isEmpty()) {
                return "";
            }
        } catch (Exception ignore) {
            return "";
        }

        return name;
    }

    public static String removeFileNameSuffix(String path) {
        // Suffix einer Pfad/Dateinamen extrahieren
        // FILENAME.SUFF

        String noSuff;
        try {
            noSuff = FilenameUtils.removeExtension(path);
            if (noSuff == null || noSuff.isEmpty()) {
                return "";
            }
        } catch (Exception ignore) {
            return "";
        }

        return noSuff;
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
                new PDialogFileChooser().showErrorAlert("Datei löschen", "", "Die Datei existiert nicht!");
                return false;
            }

            if (new PDialogFileChooser().showAlertOkCancel("Datei Löschen?", "",
                    "Die Datei löschen:" + P2LibConst.LINE_SEPARATORx2 + strFile)) {

                // und jetzt die Datei löschen
                PLog.sysLog(new String[]{"Datei löschen: ", file.getAbsolutePath()});
                if (!file.delete()) {
                    throw new Exception();
                }
                ret = true;
            }
        } catch (Exception ex) {
            ret = false;
            new PDialogFileChooser().showErrorAlert("Datei löschen",
                    "Konnte die Datei nicht löschen!", "Fehler beim löschen von:" + P2LibConst.LINE_SEPARATORx2 +
                            strFile);
            PLog.errorLog(987451206, "Fehler beim löschen: " + strFile);
        }
        return ret;
    }

    public static boolean deleteFileNoMsg(String strFile) {
        boolean ret = false;
        try {
            File file = new File(strFile);
            if (!file.exists()) {
                new PDialogFileChooser().showErrorAlert("Datei löschen", "", "Die Datei existiert nicht!");
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
            new PDialogFileChooser().showErrorAlert("Datei löschen",
                    "Konnte die Datei nicht löschen!", "Fehler beim löschen von:" + P2LibConst.LINE_SEPARATORx2 +
                            strFile);
            PLog.errorLog(302015478, "Fehler beim löschen: " + strFile);
        }
        return ret;
    }

}
