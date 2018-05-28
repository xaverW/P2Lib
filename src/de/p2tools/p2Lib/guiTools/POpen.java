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

    public static void openDestDir(String path, StringProperty prog) {
        openDestDir(path);
    }

    public static void openDestDir(String path) {
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

        Thread th = new Thread(() -> {
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
        });
        th.setName("openDestDir");
        th.start();

    }

    public static void playStoredFilm(String file, StringProperty prog) {
        playStoredFilm(file);
    }

    public static void playStoredFilm(String file) {

        File filmFile;
        if (file.isEmpty()) {
            return;
        }
        filmFile = new File(file);

        if (!filmFile.exists()) {
            new PAlertFileChosser().showErrorAlert("Fehler", "Kein Film", "Film existiert noch nicht!");
            return;
        }

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
                Platform.runLater(() -> afterPlay(TEXT.FILE));
            }
        });
        th.setName("playStoredFilm");
        th.start();

    }

    public static void openURL(String url, StringProperty prog) {
        openURL(url);
    }

    public static void openURL(String url) {

        if (url.isEmpty()) {
            return;
        }

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
                Platform.runLater(() -> afterPlay(TEXT.URL));
            }
        });
        th.setName("openURL");
        th.start();

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

}
