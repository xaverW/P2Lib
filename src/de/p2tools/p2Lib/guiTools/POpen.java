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
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class POpen {

    public static void openDir(String path, StringProperty prog) {
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
                Platform.runLater(() -> afterPlay(TEXT.DIR, prog));
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
                    Platform.runLater(() -> afterPlay(TEXT.DIR, prog));
                }
            });
            th.setName("openDir");
            th.start();

        }

    }

    public static void openDir(String path) {
        openDir(path, null);
    }

    public static void playStoredFilm(String file, StringProperty prog) {

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
                Platform.runLater(() -> afterPlay(TEXT.FILM, prog));
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
                    Platform.runLater(() -> afterPlay(TEXT.FILM, prog));
                }
            });
            th.setName("playStoredFilm");
            th.start();

        }

    }

    public static void playStoredFilm(String file) {
        playStoredFilm(file, null);
    }

    public static void openURL(String url) {
        openURL(url, null);
    }

    public static void openURL(String url, StringProperty prog) {
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
                afterPlay(TEXT.URL, prog);
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
                    Platform.runLater(() -> afterPlay(TEXT.URL, prog));
                }
            });
            th.setName("openURL");
            th.start();
        }

    }

    enum TEXT {FILM, DIR, URL}

    private static void afterPlay(TEXT t, StringProperty stringProperty) {
        if (stringProperty == null) {
            afterPlay(t);
            return;
        }

        String title, header, cont;
        String program;

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

        program = PAlertFileChosser.showAlertFileChooser(title, header, cont,
                false, PConst.primaryStage, null);

        if (!program.isEmpty()) {
            stringProperty.setValue(program);
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
