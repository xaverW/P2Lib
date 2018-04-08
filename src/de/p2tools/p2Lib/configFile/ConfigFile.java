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

import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigFile {
    public static final int MAX_COPY_BACKUPFILE = 5; // Maximum number of backup files to be stored.

    private final Path configFile;
    private final ArrayList<PData> pData;
    private final ArrayList<PDataList> pDataList;
    private final String xmlStart;
    private int maxCopyBackupfile = MAX_COPY_BACKUPFILE;


    public ConfigFile(String xmlStart, Path configFile) {
        this.xmlStart = xmlStart;
        this.configFile = configFile;
        this.pDataList = new ArrayList<>();
        this.pData = new ArrayList<>();
    }

    public int getMaxCopyBackupfile() {
        return maxCopyBackupfile;
    }

    public void setMaxCopyBackupfile(int maxCopyBackupfile) {
        this.maxCopyBackupfile = maxCopyBackupfile;
    }

    public void addConfigs(PDataList configsData) {
        pDataList.add(configsData);
    }

    public void addConfigs(PData pData) {
        this.pData.add(pData);
    }

    public boolean writeConfigFile() {
        boolean ret = false;
        new BackupConfigFile(maxCopyBackupfile, configFile).konfigCopy();

        SaveConfigFile saveConfigFile = new SaveConfigFile(xmlStart, configFile, pDataList, pData);
        saveConfigFile.write();
        return ret;
    }

    public boolean readConfigFile(ArrayList<PDataList> pDataList, ArrayList<PData> pData) {

        if (new LoadConfigFile(configFile, pDataList, pData).readConfiguration()) {
            PLog.sysLog("Config geladen");
            return true;

        } else if (new BackupConfigFile(maxCopyBackupfile, configFile).loadBackup(pDataList, pData)) {
            PLog.sysLog("Config-Backup geladen");
            return true;
        }

        return false;
    }
}
