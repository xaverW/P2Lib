/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.configfile;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.PException;
import de.p2tools.p2lib.tools.log.PLog;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsDirectory {

    /**
     * Returns the path to the config file
     *
     * @return Path object to p2tools.xml file
     */
    public static Path getSettingsFile(String configPath, String stdConfigPath, String configFile) {
        return getSettingsDirectory(configPath, stdConfigPath).resolve(configFile);
    }

    /**
     * Returns the location of the settings directory. If it does not exists, creates one.
     *
     * @return Path to the settings directory
     * @throws IllegalStateException Will be thrown if settings directory don't exist and if there is
     *                               an error on creating it.
     */
    public static Path getSettingsDirectory(String configDir, String stdDir) throws IllegalStateException {
        Path baseDirectoryPath;

        if (configDir == null || configDir.isEmpty()) {

            if (SystemUtils.IS_OS_WINDOWS) {
                baseDirectoryPath = Paths.get(System.getProperty("user.home"), stdDir);

            } else {
                //x-systeme
                baseDirectoryPath = Paths.get(System.getProperty("user.home"), "." + stdDir);
            }

        } else {
            //use the path, getting from the user
            baseDirectoryPath = Paths.get(configDir);
        }

        makeDirs(baseDirectoryPath);
        return baseDirectoryPath;
    }

    private static void makeDirs(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                if (SystemUtils.IS_OS_WINDOWS) {
                    try {
                        //set hidden attribute on windows
                        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
                    } catch (IOException ex) {
                        PLog.errorLog(795412365, ex);
                    }
                }

            } catch (final IOException ioException) {
                PLog.errorLog(912030306, ioException, "Ordner anlegen: " + path);
                PAlert.showErrorAlert("Ordner anlegen", " Der Ordner " + path +
                        " konnte nicht angelegt werden." + P2LibConst.LINE_SEPARATOR +
                        "Bitte prüfen Sie die Dateirechte.");

                throw new PException(" Der Ordner " + path +
                        " konnte nicht angelegt werden." + P2LibConst.LINE_SEPARATOR +
                        "Bitte prüfen Sie die Dateirechte.", ioException);
            }
        }
    }
}
