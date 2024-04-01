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


package de.p2tools.p2lib.tools.log;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.guitools.P2Open;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class P2Logger {

    private static final Logger LOGGER = Logger.getLogger(P2Logger.class.getName());
    private static Handler fileHandler = null;
    private static String handlerDir = "";

    static {
        LOGGER.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new P2Formatter());

        LOGGER.setLevel(Level.ALL);
    }

    public static void LogInfo(String info) {
        LOGGER.info(info);
    }

    public static void LogDebug(String info) {
        LOGGER.log(P2Level.DEBUG, info);
    }

    public static void LogDuration(String info) {
        LOGGER.log(P2Level.DURATION, info);
    }

    public static void LogExtToolMsg(String info) {
        LOGGER.log(P2Level.EXT_TOOL_MSG, info);
    }

    public static void LogWarning(String info) {
        LOGGER.warning(info);
    }

    public static void LogSevere(String info) {
        LOGGER.log(Level.SEVERE, info);
    }

    public static void LogSevere(String info, Exception ex) {
        LOGGER.log(Level.SEVERE, info, ex);
    }

    public static void LogSevere(Exception ex) {
        LOGGER.log(Level.SEVERE, "", ex);
    }

    public static void removeFileHandler() {
        if (fileHandler != null) {
            P2Log.sysLog("kein Logfile anlegen und handler schließen");
            fileHandler.close();
            LOGGER.removeHandler(fileHandler);
            fileHandler = null;
        }
    }

    public static void openLogFile() {
        if (handlerDir.isEmpty()) {
            return;
        }

        // wenn logfile existiert, dann das öffnen, ansonsten den Ordner
        Path path = Paths.get(handlerDir, P2LibConst.logfile_0);
        if (path.toFile().exists()) {
            P2Open.openFile(path.toString());
        } else {
            P2Open.openDir(handlerDir);
        }

    }

    public static void setFileHandler(String path) {
        setFileHandler(path, true);
    }

    public static void setFileHandler(String path, boolean withGui) {
        String logDir = path.isEmpty() ? P2LibConst.logdir : path;

        if (handlerDir.equals(logDir) && fileHandler != null) {
            // dann stimmts schon
            return;
        }

        removeFileHandler();
        handlerDir = logDir;
        P2Log.sysLog("Logfile anlegen: " + handlerDir);

        try {
            File dir = new File(handlerDir);
            dir.mkdirs();

            fileHandler = new FileHandler(handlerDir + File.separator + P2LibConst.logfile,
                    5_000_000, 5, false);

            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new P2Formatter());
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
            if (withGui) {
                Platform.runLater(() -> PAlert.showErrorAlert("Logfile anlegen", "Das Logfile kann icht angelegt werden, " +
                        "bitte Pfad zum Logfile prüfen."));
            }
        }
    }
}
