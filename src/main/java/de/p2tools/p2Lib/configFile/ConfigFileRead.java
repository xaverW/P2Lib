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


package de.p2tools.p2Lib.configFile;

import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigFileRead {

    public ConfigFileRead() {
    }

    public boolean readConfig(ConfigFile configFile) {
        boolean ret;
        ret = readFile(configFile);
        if (ret) {
            PLog.sysLog("Config geladen");
        } else {
            PLog.sysLog("Config laden hat nicht geklappt");
        }
        return ret;
    }

    private boolean readFile(ConfigFile configFile) {
        // todo-> kann zu unterschiedlichen Versionsständen kommen!
        if (ConfigGetStream.readConfiguration(configFile)) {
            PLog.sysLog("Config geladen: " + configFile.getFilePath());
            return true;
        }

        if (!configFile.isBackup()) {
            return false;
        }

        ArrayList<Path> pathList = new BackupConfigFile(ConfigFileWrite.MAX_COPY_BACKUP_FILE, Path.of(configFile.getFilePath())).
                loadBackup(configFile.getBackupHeader(), configFile.getBackupText());
        if (pathList == null) {
            // dann gibts keine Backups
            return false;
        }

        for (Path p : pathList) {
            configFile.setFilePath(p.toString());
            if (ConfigGetStream.readConfiguration(configFile)) {
                PLog.sysLog("Config aus Backup geladen: " + p.toFile().getAbsolutePath());
                return true;
            }
        }
        return false;
    }
}
