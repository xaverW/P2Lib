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


package de.p2tools.p2Lib.tools.log;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.tools.log.logLevel.AddInfoLevel;
import de.p2tools.p2Lib.tools.log.logLevel.DurLevel;
import de.p2tools.p2Lib.tools.log.logLevel.ExtToolMsgLevel;
import de.p2tools.p2Lib.tools.log.logLevel.UsrMsgLevel;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class PLogger {

    private static final Logger LOGGER = Logger.getLogger(PLogger.class.getName());
    private static Handler fileHandler = null;
    private static String handlerDir = "";

    static {
        LOGGER.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new PFormatter());

        LOGGER.setLevel(Level.ALL);
    }

    public static void removeFileHandler() {
        if (fileHandler != null) {
            PLog.sysLog("kein Logfile anlegen und handler schließen");
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
            POpen.openFile(path.toString());
        } else {
            POpen.openDir(handlerDir);
        }

    }

    public static void setFileHandler(String path) {
        String logDir = path.isEmpty() ? P2LibConst.logdir : path;

        if (handlerDir.equals(logDir) && fileHandler != null) {
            // dann stimmts schon
            return;
        }

        removeFileHandler();
        handlerDir = logDir;
        PLog.sysLog("Logfile anlegen: " + handlerDir);

        try {
            File dir = new File(handlerDir);
            dir.mkdirs();

            fileHandler = new FileHandler(handlerDir + File.separator + P2LibConst.logfile,
                    5_000_000, 5, false);

            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new PFormatter());
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
            Platform.runLater(() -> PAlert.showErrorAlert("Logfile anlegen", "Das Logfile kann icht angelegt werden, " +
                    "bitte Pfad zum Logfile prüfen."));
        }

    }

    public static void LogConfig(String info) {
        LOGGER.log(Level.CONFIG, info);
    }

    public static void LogInfo(String info) {
        LOGGER.info(info);
    }

    public static void LogAddInfo(String info) {
        LOGGER.log(AddInfoLevel.ADD_INFO, info);
    }

    public static void LogDuration(String info) {
        LOGGER.log(DurLevel.DURATION, info);
    }

    public static void LogUserMsg(String info) {
        LOGGER.log(UsrMsgLevel.USERMSG, info);
    }

    public static void LogExtToolMsg(String info) {
        LOGGER.log(ExtToolMsgLevel.EXT_TOOL_MSG, info);
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

}
