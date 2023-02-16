/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.net.PUrlTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConfigGetStream {

    static boolean readConfiguration(ConfigFile configFile) {
        PDuration.counterStart("readConfiguration");
        boolean ret;

        if (configFile.getIsr() != null) {
            //dann hammer schon einen Stream
            try {
                ret = new ConfigRead(configFile).read(configFile.getIsr());
            } catch (Exception ex) {
                PLog.errorLog(956301247, ex);
                ret = false;
            }

        } else if (PUrlTools.isUrl(configFile.getFilePath())) {
            //dann aus dem Web laden
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(configFile.getFilePath()).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
            } catch (final MalformedURLException ex) {
                PLog.errorLog(965312078, ex);
            } catch (final Exception ex) {
                PLog.errorLog(159487302, ex);
            }
            try (InputStream is = conn.getInputStream();
                 InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                ret = new ConfigRead(configFile).read(in);
            } catch (final Exception ex) {
                ret = false;
                PLog.errorLog(825414789, ex);
            }

        } else if (configFile.getFilePath().endsWith(".zip")) {
            //dann ists ein lokales ZIP-File
            if (!Files.exists(Path.of(configFile.getFilePath()))) {
                return false;
            }
            try {
                ZipFile zipFile = new ZipFile(configFile.getFilePath());
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                ret = false;
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    InputStream stream = zipFile.getInputStream(entry);
                    InputStreamReader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    ret = new ConfigRead(configFile).read(in);
                    if (!ret) {
                        //dann abbrechen
                        break;
                    }
                }
            } catch (IOException ex) {
                PLog.errorLog(956301247, ex);
                ret = false;
            }

        } else {
            //dann ein lokales File laden
            if (!Files.exists(Path.of(configFile.getFilePath()))) {
                return false;
            }
            try (InputStream is = Files.newInputStream(Path.of(configFile.getFilePath()));
                 InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                ret = new ConfigRead(configFile).read(in);
            } catch (final Exception ex) {
                ret = false;
                PLog.errorLog(454102598, ex);
            }
        }
        PDuration.counterStop("readConfiguration");
        return ret;
    }
}
