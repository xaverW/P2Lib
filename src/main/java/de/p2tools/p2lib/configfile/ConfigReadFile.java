/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.net.P2UrlConnectionFactory;
import de.p2tools.p2lib.tools.net.PUrlTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConfigReadFile {

    private ConfigReadFile() {
    }

    public static boolean readConfig(ConfigFile configFile) {
        // todo-> kann zu unterschiedlichen Versionsständen kommen!
        if (readConfiguration(configFile)) {
            P2Log.sysLog("Config laden OK, geladen von: " + configFile.getFilePath());
            return true;
        }

        if (!configFile.isBackup()) {
            P2Log.sysLog("Config laden hat nicht geklappt: " + configFile.getFilePath());
            P2Log.sysLog("Es soll kein Backup geladen werden." + configFile.getFilePath());
            return false;
        }

        ArrayList<Path> pathList = new ConfigBackupFile(Path.of(configFile.getFilePath())).
                loadBackup(configFile.getBackupHeader(), configFile.getBackupText());
        if (pathList == null) {
            // dann gibts keine Backups
            P2Log.sysLog("Es gibt keine Backups.");
            return false;
        }

        for (Path p : pathList) {
            configFile.clearConfigFile();
            configFile.setFilePath(p.toString());
            if (readConfiguration(configFile)) {
                P2Log.sysLog("Config aus Backup geladen: " + p.toFile().getAbsolutePath());
                return true;
            }
        }
        P2Log.sysLog("Config-Backup laden hat nicht geklappt: " + configFile.getFilePath());
        return false;
    }

    private static boolean readConfiguration(ConfigFile configFile) {
        P2Duration.counterStart("readConfiguration");
        boolean ret;

        if (configFile.getIsr() != null) {
            //dann hammer schon einen Stream
            try {
                ret = new ConfigRead(configFile).read(configFile.getIsr());
            } catch (Exception ex) {
                P2Log.errorLog(956301247, ex);
                ret = false;
            }

        } else if (PUrlTools.isUrl(configFile.getFilePath())) {
            //dann aus dem Web laden
            HttpURLConnection conn = null;
            try {
                // conn = (HttpURLConnection) new URL(configFile.getFilePath()).openConnection();
                conn = P2UrlConnectionFactory.getUrlConnection(configFile.getFilePath());
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
            } catch (final MalformedURLException ex) {
                P2Log.errorLog(965312078, ex);
            } catch (final Exception ex) {
                P2Log.errorLog(159487302, ex);
            }
            try (InputStream is = conn.getInputStream();
                 InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                ret = new ConfigRead(configFile).read(in);
            } catch (final Exception ex) {
                ret = false;
                P2Log.errorLog(825414789, ex);
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
                P2Log.errorLog(956301247, ex);
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
                P2Log.errorLog(454102598, ex);
            }
        }
        P2Duration.counterStop("readConfiguration");
        return ret;
    }
}
