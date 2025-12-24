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

package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.P2DialogFileChooser;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class P2Open {

    public static void openFile(String path) {
        openFile(P2LibConst.primaryStage, path);
    }

    public static void openFile(Stage stage, String fileStr) {
        File file;

        if (fileStr.isEmpty()) {
            P2Alert.showErrorAlert(stage,
                    "Fehler beim öffnen der Datei", "Der Pfad der Datei ist leer!");
            return;
        }
        if (!new File(fileStr).exists()) {
            P2Alert.showErrorAlert(stage,
                    "Fehler beim öffnen der Datei", "Der Pfad der Datei:\n" +
                            fileStr +
                            "\nexistiert nicht!");
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
                P2Alert.showErrorAlert(stage,
                        "Fehler beim öffnen der Datei", "Kann die Datei nicht öffnen!");
            }
        });
        th.setName("openFile");
        th.start();
    }

    public static void openDir(String path) {
        openDir(P2LibConst.primaryStage, path, null, null);
    }

    public static void openDir(Stage primaryStage, String path) {
        openDir(primaryStage, path, null, null);
    }

    public static void openDir(String path, StringProperty prog, Node getProgIcon) {
        openDir(P2LibConst.primaryStage, path, prog, getProgIcon);
    }

    public static void openDir(Stage stage, String path, StringProperty prog, Node getProgIcon) { // todo icon
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
                Platform.runLater(() -> afterPlay(stage, TEXT.DIR, prog, directory.getAbsolutePath()));
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
                    Platform.runLater(() -> afterPlay(stage, TEXT.DIR, prog, directory.getAbsolutePath()));
                }
            });
            th.setName("openDir");
            th.start();

        }

    }

    public static void playStoredFilm(String file) {
        playStoredFilm(P2LibConst.primaryStage, file, null, null);
    }

    public static void playStoredFilm(Stage primaryStage, String file) {
        playStoredFilm(primaryStage, file, null, null);
    }

    public static void playStoredFilm(String file, StringProperty prog, Node getProgIcon) {
        playStoredFilm(P2LibConst.primaryStage, file, prog, getProgIcon);
    }

    public static void playStoredFilm(Stage stage, String file, StringProperty prog, Node getProgIcon) { // todo Icon
        if (file.isEmpty()) {
            return;
        }

        File filmFile;
        filmFile = new File(file);
        if (!filmFile.exists()) {
            new P2DialogFileChooser().showErrorAlert("Fehler", "Kein Film", "Film existiert noch nicht!");
            return;
        }

        if (prog != null && !prog.getValueSafe().isEmpty()) {
            // dann mit dem vorgegebenen Player starten
            try {
                final String program = prog.getValueSafe();
                final String[] cmd = {program, filmFile.getAbsolutePath()};
                Runtime.getRuntime().exec(cmd);
            } catch (final Exception ex) {
                Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file));
            }

        } else {
            // den Systemeigenen Player starten
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        final Desktop d = Desktop.getDesktop();
                        if (d.isSupported(Desktop.Action.OPEN)) {
                            d.open(new File(file));
                        }
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file));
                }
            });
            th.setName("playStoredFilm");
            th.start();
        }
    }

    public static void playStoredFilm(String[] arrProgCallArray, StringProperty prog, String file, Node getProgIcon) {
        playStoredFilm(P2LibConst.primaryStage, arrProgCallArray, prog, file, getProgIcon);
    }

    private static void playStoredFilm(Stage stage, String[] arrProgCallArray, StringProperty prog, String file, Node getProgIcon) {
        if (file.isEmpty()) {
            return;
        }

        if (arrProgCallArray.length != 0) {
            // dann mit dem vorgegebenen Player starten
            try {
                Runtime.getRuntime().exec(arrProgCallArray);
            } catch (final Exception ex) {
                Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file));
            }

        } else {
            // den Systemeigenen Player starten
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        final Desktop d = Desktop.getDesktop();
                        if (d.isSupported(Desktop.Action.OPEN)) {
                            d.open(new File(file));
                        }
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.FILM, prog, file));
                }
            });
            th.setName("playStoredFilm");
            th.start();
        }
    }

    public static void openURL(String url) {
        openURL(P2LibConst.primaryStage, url);
    }

    public static void openURL(Stage primaryStage, String url) {
        openURL(primaryStage, url, null);
    }

    public static void openURL(String url, StringProperty prog, Node getProgIcon) {
        openURL(P2LibConst.primaryStage, url, prog);
    }

    public static void openURL(Stage stage, String url, StringProperty prog) {
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
                afterPlay(stage, TEXT.URL, prog, url);
            }

        } else {
            // den Systemeigenen Player starten
            Thread th = new Thread(() -> {
                try {
                    if (Desktop.isDesktopSupported() &&
                            Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                        Desktop.getDesktop().browse(new URI(url));

                    } else {
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("xdg-open " + url);
                    }

                } catch (Exception ex) {
                    Platform.runLater(() -> afterPlay(stage, TEXT.URL, prog, url));
                }
            });

            th.setName("openURL");
            th.start();
        }

    }

    public static void openExternProgram(Stage stage, StringProperty prog) {
        if (prog != null && !prog.getValueSafe().isEmpty()) {
            try {
                final String program = prog.getValueSafe();
                Runtime.getRuntime().exec(program);
            } catch (final Exception ex) {
                Platform.runLater(() -> afterPlay(stage, TEXT.EXTERN, prog, ""));
            }
        }
    }

    private static void afterPlay(Stage stage, TEXT t, StringProperty stringProperty, String fileUrl) {
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
            case EXTERN:
                title = "Kein Programm";
                header = "Externes Programm auswählen";
                cont = "Das externe Programm \"" + stringProperty.getValueSafe() + "\" wird nicht gefunden.";
                break;
        }

        try {
            program = P2DialogFileChooser.showFileChooser(stage, title, header,
                    cont, false);

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
            P2Log.errorLog(912030654, eex, new String[]{"Kann nicht öffnen,", "Programm: " + program,
                    "File/Url: " + fileUrl});
        }

        if (!ok) {
            stringProperty.set("");
            new P2Alert().showErrorAlert("Fehler beim öffnen des Programms", "Kann das Programm nicht öffnen!");
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
            case EXTERN:
                header = "Kein externes Programm";
                cont = "Das externe Programm das gestartet werden soll, wird nicht gefunden";
                break;
        }

        P2Alert.showErrorAlert(header, cont);
    }

    enum TEXT {FILM, DIR, URL, EXTERN}
}
