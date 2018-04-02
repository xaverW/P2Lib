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

package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PAlertFileChosser;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class POpen {


    // todo erweitern mit eigener Auswahl der Programme

    public static void openDestDir(String ordner, StringProperty prog) {
        openDestDir(ordner);
    }

    public static void openDestDir(String ordner) {
        File directory;

        if (ordner.isEmpty()) {
            return;
        }
        if (!ordner.endsWith(File.separator)) {
            ordner += File.separator;
        }

        if (new File(ordner).exists()) {
            directory = new File(ordner);
        } else {
            directory = new File(ordner).getParentFile();
        }

        new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    final Desktop d = Desktop.getDesktop();
                    if (d.isSupported(Desktop.Action.OPEN)) {
                        d.open(directory);
                    }
                }
            } catch (Exception ex) {
                Platform.runLater(() -> afterPlay(TEXT.DIR));
            }
        }).start();

    }

    public static void playStoredFilm(String datei, StringProperty prog) {
        playStoredFilm(datei);
    }

    public static void playStoredFilm(String datei) {

        File filmFile;
        if (datei.isEmpty()) {
            return;
        }
        filmFile = new File(datei);

        if (!filmFile.exists()) {
            new PAlertFileChosser().showErrorAlert("Fehler", "Kein Film", "Film existiert noch nicht!");
            return;
        }

        // den Systemeigenen Player starten
        new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    final Desktop d = Desktop.getDesktop();
                    if (d.isSupported(Desktop.Action.OPEN)) {
                        d.open(filmFile);
                    }
                }
            } catch (Exception ex) {
                Platform.runLater(() -> afterPlay(TEXT.FILE));
            }
        }).start();

    }

    public static void openURL(String url, StringProperty prog) {
        openURL(url);
    }

    public static void openURL(String url) {

        if (url.isEmpty()) {
            return;
        }

        // den Systemeigenen Player starten
        new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    final Desktop d = Desktop.getDesktop();
                    if (d.isSupported(Desktop.Action.BROWSE)) {
                        d.browse(new URI(url));
                    }
                }
            } catch (Exception ex) {
                Platform.runLater(() -> afterPlay(TEXT.URL));
            }
        }).start();

    }

    enum TEXT {FILE, DIR, URL}

    private static void afterPlay(TEXT t) {
        String header, cont;

        switch (t) {
            default:
            case FILE:
                header = "Kein Videoplayer";
                cont = "Ein Videoplayer zum Abspielen wird nicht gefunden.";
                break;
            case DIR:
                header = "Kein Dateimanager";
                cont = "Der Dateimanager zum Anzeigen des Speicherordners wird nicht gefunden.";
                break;
            case URL:
                header = "Kein Browser";
                cont = "Der Browser zum Anzeigen der URL wird nicht gefunden.";
                break;
        }

        PAlert.showErrorAlert(header, cont);
    }


//    public static void openDestDir(String ordner) {
//        File directory;
//
//        if (ordner.isEmpty()) {
//            return;
//        }
//        if (!ordner.endsWith(File.separator)) {
//            ordner += File.separator;
//        }
//
//        if (new File(ordner).exists()) {
//            directory = new File(ordner);
//        } else {
//            directory = new File(ordner).getParentFile();
//        }
//
//
//        if (!ProgConfig.SYSTEM_PROG_OPEN_DIR.get().isEmpty()) {
//            Exception exception;
//            try {
//                final String programm = ProgConfig.SYSTEM_PROG_OPEN_DIR.get();
//                final String[] arrProgCallArray = {programm, directory.getAbsolutePath()};
//                Runtime.getRuntime().exec(arrProgCallArray);
//            } catch (final Exception ex) {
//                afterPlay(directory.getAbsolutePath(), TEXT.DIR, ex);
//            }
//
//
//        } else {
//            new Thread(() -> {
//                try {
//                    if (Desktop.isDesktopSupported()) {
//                        final Desktop d = Desktop.getDesktop();
//                        if (d.isSupported(Desktop.Action.OPEN)) {
//                            d.open(directory);
//                        }
//                    }
//                } catch (Exception ex) {
//                    Platform.runLater(() -> afterPlay(directory.getAbsolutePath(), TEXT.DIR, ex));
//                }
//            }).start();
//
//        }
//    }
//
//    public static void playStoredFilm(String datei) {
//
//        File filmFile;
//        if (datei.isEmpty()) {
//            return;
//        }
//        filmFile = new File(datei);
//
//        if (!filmFile.exists()) {
//            new PAlertFileChosser().showErrorAlert("Fehler", "Kein Film", "Film existiert noch nicht!");
//            return;
//        }
//
//
//        if (!ProgConfig.SYSTEM_PROG_PLAY_FILE.get().isEmpty()) {
//            // dann mit dem vorgegebenen Player starten
//            try {
//                final String programm = ProgConfig.SYSTEM_PROG_PLAY_FILE.get();
//                final String[] cmd = {programm, filmFile.getAbsolutePath()};
//                Runtime.getRuntime().exec(cmd);
//            } catch (final Exception ex) {
//                afterPlay(filmFile.getAbsolutePath(), TEXT.FILE, ex);
//            }
//
//
//        } else {
//            // den Systemeigenen Player starten
//            new Thread(() -> {
//                try {
//                    if (Desktop.isDesktopSupported()) {
//                        final Desktop d = Desktop.getDesktop();
//                        if (d.isSupported(Desktop.Action.OPEN)) {
//                            d.open(filmFile);
//                        }
//                    }
//                } catch (Exception ex) {
//                    Platform.runLater(() -> afterPlay(filmFile.getAbsolutePath(), TEXT.FILE, ex));
//                }
//            }).start();
//
//        }
//    }
//
//    public static void openURL(String url) {
//
//        if (url.isEmpty()) {
//            return;
//        }
//
//
//        if (!ProgConfig.SYSTEM_PROG_OPEN_URL.get().isEmpty()) {
//            // dann mit dem vorgegebenen Player starten
//            try {
//                final String programm = ProgConfig.SYSTEM_PROG_OPEN_URL.get();
//                final String[] cmd = {programm, url};
//                Runtime.getRuntime().exec(cmd);
//            } catch (final Exception ex) {
//                afterPlay(url, TEXT.URL, ex);
//            }
//
//
//        } else {
//            // den Systemeigenen Player starten
//            new Thread(() -> {
//                try {
//                    if (Desktop.isDesktopSupported()) {
//                        final Desktop d = Desktop.getDesktop();
//                        if (d.isSupported(Desktop.Action.BROWSE)) {
//                            d.browse(new URI(url));
//                        }
//                    }
//                } catch (Exception ex) {
//                    Platform.runLater(() -> afterPlay(url, TEXT.URL, ex));
//                }
//            }).start();
//
//        }
//    }
//
//    enum TEXT {FILE, DIR, URL}
//
//    private static void afterPlay(String directory, TEXT t, Exception exception) {
//        String programm = "";
//        boolean ok;
//        String title, header, cont;
//        StringProperty conf;
//
//        switch (t) {
//            default:
//            case FILE:
//                title = "Kein Videoplayer";
//                header = "Videoplayer auswählen";
//                cont = "Ein Videoplayer zum Abspielen wird nicht gefunden. Videoplayer selbst auswählen.";
//                conf = ProgConfig.SYSTEM_PROG_PLAY_FILE;
//                break;
//            case DIR:
//                title = "Kein Dateimanager";
//                header = "Dateimanager auswählen";
//                cont = "Der Dateimanager zum Anzeigen des Speicherordners wird nicht gefunden.\n" +
//                        "Dateimanager selbst auswählen.";
//                conf = ProgConfig.SYSTEM_PROG_OPEN_DIR;
//                break;
//            case URL:
//                title = "Kein Browser";
//                header = "Browser auswählen";
//                cont = "Der Browser zum Anzeigen der URL wird nicht gefunden.\n" +
//                        "Browser selbst auswählen.";
//                conf = ProgConfig.SYSTEM_PROG_OPEN_URL;
//                break;
//        }
//
//
//        try {
//            programm = new PAlertFileChosser().showAlertFileCooser(title, header, cont, false,
//                    ProgData.getInstance().primaryStage, new Icons().ICON_BUTTON_FILE_OPEN);
//            if (!programm.isEmpty()) {
//                final String[] cmd = {programm, directory};
//                Runtime.getRuntime().exec(cmd);
//                conf.set(programm);
//                ok = true;
//            } else {
//                // abgebrochen
//                ok = true;
//            }
//
//        } catch (final Exception eex) {
//            ok = false;
//            Log.errorLog(959632369, eex, new String[]{"Kann nicht öffnen,", "Programm: " + programm,
//                    "File/Url: " + directory});
//        }
//
//        if (!ok) {
//            conf.set("");
//            new PAlertFileChosser().showErrorAlert("Fehler", "", "Kann das Programm nicht öffnen!");
//        }
//    }


}
