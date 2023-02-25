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

import de.p2tools.p2lib.tools.log.PLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ConfigWriteFile {
    private ConfigWriteFile() {
    }

    /**
     * write config zip-file, all configs in one zip-file, named by configFileZip
     *
     * @return
     */
    public static boolean writeConfigFileZip(Path path, ArrayList<ConfigFile> cFileList) {
        boolean ret = true;
        try {
            Files.createDirectories(path.getParent());
            new ConfigBackupFile(path).backupConfigFile();

            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path.toFile()));
            for (ConfigFile cf : cFileList) {
                ZipEntry zipEntry = new ZipEntry(Path.of(cf.getFilePath()).getFileName().toString());
                zipOut.putNextEntry(zipEntry);

                ConfigWrite configWrite = new ConfigWrite(cf);
                if (!configWrite.write(zipOut)) {
                    ret = false;
                    break;
                }
                zipOut.closeEntry();
            }
            zipOut.close();
        } catch (Exception ex) {
            PLog.errorLog(784512589, ex);
            ret = false;
        }
        return ret;
    }

    /**
     * write configs direct to the file, named in the config
     *
     * @return
     */
    public static boolean writeConfigFile(ConfigFile cFile) {
        ArrayList<ConfigFile> list = new ArrayList<>();
        list.add(cFile);
        return writeConfigFile(list);
    }

    public static boolean writeConfigFile(ArrayList<ConfigFile> cFileList) {
        boolean ret = true;
        try {
            for (ConfigFile cf : cFileList) {
                Files.createDirectories(Path.of(cf.getFilePath()).getParent());
                if (cf.isBackup()) {
                    new ConfigBackupFile(Path.of(cf.getFilePath())).backupConfigFile();
                }

                ConfigWrite configWrite = new ConfigWrite(cf);
                OutputStream outputStream = Files.newOutputStream(Path.of(cf.getFilePath()));
                if (!configWrite.write(outputStream)) {
                    ret = false;
                }
            }
        } catch (IOException ex) {
            PLog.errorLog(602035789, ex);
            ret = false;
        }
        return ret;
    }
}
