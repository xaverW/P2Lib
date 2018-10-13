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

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PAlertFileChosser;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class POpen {

    public static void openFile(String path) {
        openFile(PConst.primaryStage, path);
    }

    public static void openFile(Stage stage, String fileStr) {
        File file;

        if (fileStr.isEmpty()) {
            return;
        }
        if (!new File(fileStr).exists()) {
            return;
        }

        file = new File(fileStr);
        Thread th = new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    final Desktop d = Desktop.getDesktop();
                    if (d.isSupported(Desktop.Action.OPEN)) {
                        d.open(file);
                    }
                }
            } catch (Exception ex) {
                new PAlert().showErrorAlert(stage,
                        "Fehler beim öffnen der Datei", "Kann die Datei nicht öffnen!");
            }
        });
        th.setName("openFile");
        th.start();
    }

    public static void openDir(String path) {
        openDir(PConst.primaryStage, path, null, null);
    }

    public static void openDir(Stage primaryStage, String path) {
        openDir(primaryStage, path, null, null);
    }

    public static void openDir(String path, StringProperty prog, ImageView getProgIcon) {
        openDir(PConst.primaryStage, path, prog, getProgIcon);
    }

    public static void openDir(Stage stage, String path, StringProperty prog, ImageView getProgIcon) {
        File directory;

        if (path.isEmpty()) {
            return;
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }

        if (new File(path).exists()) {
            directory = new File(path);
        } else {
            directory = new File(path).getParentFile();
        }

        if (prog != null && !prog.getValueSafe().isEmpty()) {
            try {
                final String program = prog.getValueSafe();
                final String[] arrProgCallArray = {program, directory.getAbsolutePath()};
                Runtime.getRuntime().exec(arrProgCallArray);
            } catch (final Exception ex) {
                Platform.runLater(() -> afterPlay(stage, TEXT.DIR, prog, directory.getAbsolutePath(), getProgIcon));
            }


        } else {
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        final Desktop d = Desktop.getDesktop();
                        if (d.isSupported(Desktop.Action.OPEN)) {
                            d.open(directory);
                        }
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.DIR, prog, directory.getAbsolutePath(), getProgIcon));
                }
            });
            th.setName("openDir");
            th.start();

        }

    }

    public static void playStoredFilm(String file) {
        playStoredFilm(PConst.primaryStage, file, null, null);
    }

    public static void playStoredFilm(Stage primaryStage, String file) {
        playStoredFilm(primaryStage, file, null, null);
    }

    public static void playStoredFilm(String file, StringProperty prog, ImageView getProgIcon) {
        playStoredFilm(PConst.primaryStage, file, prog, getProgIcon);
    }

    public static void playStoredFilm(Stage stage, String file, StringProperty prog, ImageView getProgIcon) {

        File filmFile;
        if (file.isEmpty()) {
            return;
        }
        filmFile = new File(file);

        if (!filmFile.exists()) {
            new PAlertFileChosser().showErrorAlert("Fehler", "Kein Film", "Film existiert noch nicht!");
            return;
        }

        if (prog != null && !prog.getValueSafe().isEmpty()) {
            // dann mit dem vorgegebenen Player starten
            try {
                final String program = prog.getValueSafe();
                final String[] cmd = {program, filmFile.getAbsolutePath()};
                Runtime.getRuntime().exec(cmd);
            } catch (final Exception ex) {
                Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file, getProgIcon));
            }


        } else {
            // den Systemeigenen Player starten
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        final Desktop d = Desktop.getDesktop();
                        if (d.isSupported(Desktop.Action.OPEN)) {
                            d.open(filmFile);
                        }
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file, getProgIcon));
                }
            });
            th.setName("playStoredFilm");
            th.start();

        }

    }

    public static void openURL(String url) {
        openURL(PConst.primaryStage, url);
    }

    public static void openURL(Stage primaryStage, String url) {
        openURL(primaryStage, url, null, null);
    }

    public static void openURL(String url, StringProperty prog, ImageView getProgIcon) {
        openURL(PConst.primaryStage, url, prog, getProgIcon);
    }

    public static void openURL(Stage stage, String url, StringProperty prog, ImageView getProgIcon) {
        if (url.isEmpty()) {
            return;
        }

        if (prog != null && !prog.getValueSafe().isEmpty()) {
            // dann mit dem vorgegebenen Player starten
            try {
                final String program = prog.getValueSafe();
                final String[] cmd = {program, url};
                Runtime.getRuntime().exec(cmd);
            } catch (final Exception ex) {
                afterPlay(stage, TEXT.URL, prog, url, getProgIcon);
            }

        } else {
            // den Systemeigenen Player starten
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        final Desktop d = Desktop.getDesktop();
                        if (d.isSupported(Desktop.Action.BROWSE)) {
                            d.browse(new URI(url));
                        }
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.URL, prog, url, getProgIcon));
                }
            });
            th.setName("openURL");
            th.start();
        }

    }

    enum TEXT {FILM, DIR, URL}

    private static void afterPlay(Stage stage, TEXT t, StringProperty stringProperty, String fileUrl, ImageView getProgIcon) {
        if (stringProperty == null) {
            afterPlay(t);
            return;
        }

        String title, header, cont;
        String program = "";
        boolean ok;

        switch (t) {
            default:
            case FILM:
                title = "Kein Videoplayer";
                header = "Videoplayer auswählen";
                cont = "Der Videoplayer \"" + stringProperty.getValueSafe() + "\" zum Abspielen wird nicht gefunden.";
                break;
            case DIR:
                title = "Kein Dateimanager";
                header = "Dateimanager auswählen";
                cont = "Der Dateimanager \"" + stringProperty.getValueSafe() + "\" zum Anzeigen des Speicherordners wird nicht gefunden.";
                break;
            case URL:
                title = "Kein Browser";
                header = "Browser auswählen";
                cont = "Der Browser \"" + stringProperty.getValueSafe() + "\" zum Anzeigen der URL wird nicht gefunden.";
                break;
        }


        try {
            program = PAlertFileChosser.showAlertFileChooser(stage, title, header,
                    cont, false, getProgIcon);

            if (!program.isEmpty()) {
                final String[] cmd = {program, fileUrl};
                Runtime.getRuntime().exec(cmd);
                stringProperty.set(program);
                ok = true;
            } else {
                // abgebrochen
                ok = true;
            }

        } catch (final Exception eex) {
            ok = false;
            PLog.errorLog(912030654, eex, new String[]{"Kann nicht öffnen,", "Programm: " + program,
                    "File/Url: " + fileUrl});
        }

        if (!ok) {
            stringProperty.set("");
            new PAlert().showErrorAlert("Fehler beim öffnen des Programms", "Kann das Programm nicht öffnen!");
        }

    }

    private static void afterPlay(TEXT t) {
        String header, cont;

        switch (t) {
            default:
            case FILM:
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
}
