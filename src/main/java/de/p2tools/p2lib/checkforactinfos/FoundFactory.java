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


package de.p2tools.p2lib.checkforactinfos;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.net.P2UrlConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FoundFactory {

    public static void downloadFile(FoundSearchDataDTO foundSearchDataDTO, final String url) {
        final FoundDownloadDialogController downloadDialogController =
                new FoundDownloadDialogController(foundSearchDataDTO, url, foundSearchDataDTO.downloadDirProperty());

        if (downloadDialogController.getOk()) {
            P2Log.sysLog("Download wird gestartet");

            final Thread download = new FoundHttpDownload(foundSearchDataDTO.getStage(), url,
                    downloadDialogController.getDestPath(), downloadDialogController.getDestName());
            try {
                //verhindert das Aufpoppen des startenden Dialogs etwas
                Thread.sleep(500);
            } catch (final Exception ignore) {
            }
            download.start();

        } else {
            P2Log.sysLog("Download wird nicht gestartet");
        }
    }

    public static boolean isNewFound(String old, String newValue) {
        return isNewFound(old, newValue, null);
    }

    public static boolean isNewFound(String old, String newValue, ArrayList<String> log) {
        if (newValue.compareTo(old) <= 0) {
            //dann wars schon mal da oder gibt nix
            if (log != null) {
                log.add("  old: " + old + "  isNewFound: " + newValue);
                log.add("  -> gibt nichts oder schon mal angezeigt");
            }
            return false;

        } else {
            if (log != null) {
                log.add("  old: " + old + "  isNewFound: " + newValue);
            }
            return true;
        }
    }

    public static String getInfoFile(String url) {
        String ret = "";
        try (InputStreamReader in = new InputStreamReader(
                connectToServer(url), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(in)) {

            String strLine;
            while ((strLine = br.readLine()) != null) {
                ret = ret + P2LibConst.LINE_SEPARATOR + strLine;
            }

        } catch (final IOException ex) {
            P2Log.errorLog(951254670, ex);
        }
        return ret;
    }

    public static InputStream connectToServer(String searchUrl) throws IOException {
        final int TIMEOUT = 10_000; // timeout ms
        // final HttpURLConnection conn = (HttpURLConnection) new URL(searchUrl).openConnection();
        final HttpURLConnection conn = P2UrlConnectionFactory.getUrlConnection(searchUrl);
        conn.setRequestProperty("User-Agent", P2LibConst.userAgent);
        conn.setReadTimeout(TIMEOUT);
        conn.setConnectTimeout(TIMEOUT);

        return conn.getInputStream();
    }
}
