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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ReadConfigFile {

    private final ArrayList<ConfigFile> cFileList;
    private int maxCopyBackupfile = WriteConfigFile.MAX_COPY_BACKUPFILE;


    public ReadConfigFile() {
        this.cFileList = new ArrayList<>();
    }

    public int getMaxCopyBackupfile() {
        return maxCopyBackupfile;
    }

    public void setMaxCopyBackupfile(int maxCopyBackupfile) {
        this.maxCopyBackupfile = maxCopyBackupfile;
    }

    public void addConfigFile(ConfigFile configFile) {
        cFileList.add(configFile);
    }


    /**
     * read the configfile
     *
     * @param configFileZip
     * @return
     */
    public boolean readConfigFileZip(Path configFileZip) {
        return readConfigFileZip(configFileZip, "", "");

    }

    /**
     * read the configfile and show the text if config is faulty
     *
     * @param configFileZip
     * @param backupText
     * @return
     */
    public boolean readConfigFileZip(Path configFileZip, String backupHeader, String backupText) {
        if (readFileZip(configFileZip)) {
            return true;
        }


        ArrayList<Path> pathList = new BackupConfigFile(maxCopyBackupfile, configFileZip).loadBackup(backupHeader, backupText);
        if (pathList == null) {
            // dann gibts keine Backups
            return false;
        }

        for (Path p : pathList) {
            if (readFileZip(p)) {
                return true;
            }
        }

        return false;
    }

    private boolean readFileZip(Path configFileZip) {
        boolean ret = true;

        try {
            ZipFile zipFile = new ZipFile(configFileZip.toFile());
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream stream = zipFile.getInputStream(entry);

                for (ConfigFile cf : cFileList) {

                    String cfFileName = cf.getConfigFile().getFileName() + "";
                    String zipFileName = entry.getName();
                    if (!cfFileName.equals(zipFileName)) {
                        continue;
                    }

                    if (!new LoadConfig(cf.getConfigFile(), cf.getpDataList(), cf.getpData()).readConfiguration(stream)) {
                        ret = false;
                    }
                    cFileList.remove(cf);
                    break;
                }
            }

        } catch (IOException ex) {
            PLog.errorLog(821354180, ex, "readConfigFileZip");
            ret = false;
        }

        return ret;
    }


    public boolean readConfigFile() {
        return readConfigFile(true, "", "");
    }

    public boolean readConfigFile(boolean loadBackup) {
        return readConfigFile(loadBackup, "", "");
    }

    public boolean readConfigFile(String backupHeader, String backupText) {
        boolean ret = true;

        for (ConfigFile cf : cFileList) {
            if (!readFile(true, cf, backupHeader, backupText)) {
                ret = false;
            }
        }

        return ret;
    }

    public boolean readConfigFile(boolean loadBackup, String backupHeader, String backupText) {
        boolean ret = true;

        for (ConfigFile cf : cFileList) {
            if (!readFile(loadBackup, cf, backupHeader, backupText)) {
                ret = false;
            }
        }

        return ret;
    }

    private boolean readFile(boolean loadBackup, ConfigFile cf, String backupHeader, String backupText) {
        // todo-> kann zu unterschiedlichen Versionsständen kommen!
        if (new LoadConfig(cf.getConfigFile(), cf.getpDataList(), cf.getpData()).readConfiguration()) {
            PLog.sysLog("Config geladen: " + cf.getConfigFile().toString());
            return true;
        }

        if (!loadBackup) {
            return false;
        }

        ArrayList<Path> pathList = new BackupConfigFile(maxCopyBackupfile, cf.getConfigFile()).
                loadBackup(backupHeader, backupText);
        if (pathList == null) {
            // dann gibts keine Backups
            return false;
        }

        for (Path p : pathList) {
            if (new LoadConfig(p, cf.getpDataList(), cf.getpData()).readConfiguration()) {
                PLog.sysLog("Config aus Backup geladen: " + cf.getConfigFile().toString());
                return true;
            }
        }

        return false;
    }
}
