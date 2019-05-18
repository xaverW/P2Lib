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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ReadWriteConfigFile {
    public static final int MAX_COPY_BACKUPFILE = 5; // Maximum number of backup files to be stored.

    private final ArrayList<ConfigFile> cFileList;
    private int maxCopyBackupfile = MAX_COPY_BACKUPFILE;


    public ReadWriteConfigFile() {
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
     * write config zip-file, all configs in one zip-file, named by configFileZip
     *
     * @return
     */
    public boolean writeConfigFileZip(Path configFileZip) {
        boolean ret = true;
        new BackupConfigFile(maxCopyBackupfile, configFileZip).configCopy();

        try (FileOutputStream fout = new FileOutputStream(configFileZip.toFile());
             ZipOutputStream zipOut = new ZipOutputStream(fout)) {

            for (ConfigFile cf : cFileList) {
                ZipEntry zipEntry = new ZipEntry(cf.getConfigFile().getFileName().toString());
                zipOut.putNextEntry(zipEntry);

                SaveConfig saveConfig = new SaveConfig(cf.getXmlStart(), configFileZip, cf.getpDataList(), cf.getpData());
                if (!saveConfig.write(zipOut)) {
                    ret = false;
                }
            }

        } catch (Exception ex) {
            ret = false;
        }

        return ret;
    }

    /**
     * write configs direct to the file, named in the config
     *
     * @return
     */
    public boolean writeConfigFile() {
        boolean ret = true;

        for (ConfigFile cf : cFileList) {

            new BackupConfigFile(maxCopyBackupfile, cf.getConfigFile()).configCopy();
            SaveConfig saveConfig = new SaveConfig(cf.getXmlStart(), cf.getConfigFile(), cf.getpDataList(), cf.getpData());
            if (!saveConfig.write()) {
                ret = false;
            }
        }

        return ret;
    }


    /**
     * @return
     */
    public boolean readConfigFileZip(Path configFileZip) {
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

                    ret = readFileZip(cf, stream);
                    cFileList.remove(cf);
                    break;
                }
            }

        } catch (IOException ex) {
            PLog.errorLog(987451203, ex, "readConfigFileZip");
        }

        return ret;
    }

    private boolean readFileZip(ConfigFile cf, InputStream stream) {
        boolean ret = true;

        if (new LoadConfig(cf.getConfigFile(), cf.getpDataList(), cf.getpData()).readConfiguration(stream)) {
            PLog.sysLog("Config geladen: " + cf.getConfigFile().toString());

        } else if (new BackupConfigFile(maxCopyBackupfile, cf.getConfigFile()).loadBackup(cf.getpDataList(), cf.getpData())) {
            PLog.sysLog("Config-Backup geladen: " + cf.getConfigFile().toString());

        } else {
            ret = false;
        }

        return ret;
    }

    public boolean readConfigFile() {
        boolean ret = true;

        for (ConfigFile cf : cFileList) {

            if (new LoadConfig(cf.getConfigFile(), cf.getpDataList(), cf.getpData()).readConfiguration()) {
                PLog.sysLog("Config geladen: " + cf.getConfigFile().toString());
            } else if (new BackupConfigFile(maxCopyBackupfile, cf.getConfigFile()).loadBackup(cf.getpDataList(), cf.getpData())) {
                PLog.sysLog("Config-Backup geladen: " + cf.getConfigFile().toString());
            } else {
                ret = false;
            }

        }

        return ret;
    }
}
