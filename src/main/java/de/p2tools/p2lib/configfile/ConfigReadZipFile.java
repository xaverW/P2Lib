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

import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConfigReadZipFile {
    private ConfigReadZipFile() {
    }

    public static boolean readConfig(Path zipFilePath, ArrayList<ConfigFile> cFileList) {
        //ist ein lokales ZIP-File
        PDuration.counterStart("readConfiguration");
        boolean ret;

        if (!Files.exists(zipFilePath)) {
            return false;
        }

        try {
            ZipFile zipFile = new ZipFile(zipFilePath.toFile());
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ret = true;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream stream = zipFile.getInputStream(entry);
                InputStreamReader in = new InputStreamReader(stream, StandardCharsets.UTF_8);

                for (ConfigFile cf : cFileList) {
                    String cfFileName = Path.of(cf.getFilePath()).getFileName().toString();
                    String zipFileName = entry.getName();
                    if (!cfFileName.equals(zipFileName)) {
                        //todo check
                        continue;
                    }

                    ret = new ConfigRead(cf).read(in);
                    if (!ret) {
                        //dann abbrechen
                        break;
                    }
                    cFileList.remove(cf);
                    break;
                }
            }
        } catch (IOException ex) {
            PLog.errorLog(956301247, ex);
            ret = false;
        }

        PDuration.counterStop("readConfiguration");
        return ret;
    }
}
