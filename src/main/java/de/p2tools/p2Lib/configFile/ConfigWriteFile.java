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

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ConfigWriteFile {
    public static final int MAX_COPY_BACKUP_FILE = 5; // Maximum number of backup files to be stored.
    private final ArrayList<ConfigFile> cFileList;

    public ConfigWriteFile() {
        this.cFileList = new ArrayList<>();
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
        new ConfigBackupFile(configFileZip).backupConfigFile();

        try (FileOutputStream fout = new FileOutputStream(configFileZip.toFile());
             ZipOutputStream zipOut = new ZipOutputStream(fout)) {

            for (ConfigFile cf : cFileList) {
                ZipEntry zipEntry = new ZipEntry(Path.of(cf.getFilePath()).getFileName().toString());
                zipOut.putNextEntry(zipEntry);

                ConfigWrite configWrite = new ConfigWrite(configFileZip, cf.getXmlStart(), cf.getpDataList(), cf.getpData());
                if (!configWrite.write(zipOut)) {
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
            if (cf.isBackup()) {
                new ConfigBackupFile(Path.of(cf.getFilePath())).backupConfigFile();
            }
            ConfigWrite configWrite = new ConfigWrite(Path.of(cf.getFilePath()), cf.getXmlStart(), cf.getpDataList(), cf.getpData());
            if (!configWrite.write()) {
                ret = false;
            }
        }

        return ret;
    }
}
