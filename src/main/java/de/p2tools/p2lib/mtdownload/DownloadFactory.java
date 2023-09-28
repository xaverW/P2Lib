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


package de.p2tools.p2lib.mtdownload;

import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFactory {
    private DownloadFactory() {
    }

    public static boolean downloadFile(final Stage stage, final String url) {
        return downloadFile(stage, url, null, "");
    }

    public static boolean downloadFile(final Stage stage, final String url, final StringProperty path, final String fileName) {
        boolean ret = false; // true if started
        final DownloadDialogController downloadDialogController = new DownloadDialogController(stage, url, path, fileName);

        if (downloadDialogController.getOk()) {
            ret = true;
            PLog.sysLog("Download wird gestartet");

            final Thread download = new HttpDownload(stage, url, downloadDialogController.getDestPath(), downloadDialogController.getDestName());
            try {
                //verhindert das Aufpoppen des startenden Dialogs etwas
                Thread.sleep(500);
            } catch (final Exception ignore) {
            }
            download.start();

        } else {
            PLog.sysLog("Download wird nicht gestartet");
        }

        return ret;
    }

    public static HttpURLConnection getConn(String urlStr, String userAgent, int timeOutSec, long downloaded, boolean sslAlways) {
        try {
            URL url = new URL(urlStr);
            return getConn(url, userAgent, timeOutSec, downloaded, sslAlways);
        } catch (Exception ex) {
            PLog.errorLog(451214789, ex, "DownloadFactory.getConn");
        }
        return null;
    }

    public static HttpURLConnection getConn(URL url, String userAgent, int timeOutSec, long downloaded, boolean sslAlways) {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * timeOutSec);
            conn.setReadTimeout(1000 * timeOutSec);

            if (sslAlways && conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                httpsConn.setHostnameVerifier(
                        // Create all-trusting host name verifier
                        (hostname, session) -> true);
            }

            conn.setRequestProperty("Range", "bytes=" + downloaded + '-');
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (Exception ex) {
            PLog.errorLog(959868547, ex, "DownloadFactory.getConn");
        }
        return conn;
    }

    public static String getContentLengthMB(String url) {
        // liefert die Dateigröße einer URL in MB!!
        // Anzeige der Größe in MiB und deshalb: Faktor 1000
        String sizeStr = "";
        try {
            long l = getContentLength(new URL(url), false);
            if (l > 1_000_000) {
                // größer als 1MiB sonst kann ich mirs sparen
                sizeStr = String.valueOf(l / 1_000_000);
            } else if (l > 0) {
                sizeStr = "1";
            }
        } catch (Exception ex) {
            PLog.errorLog(102589746, ex, "DownloadFactory.getFileSizeFromUrl");
        }
        return sizeStr;
    }

    /**
     * Return the content length of the requested Url.
     *
     * @param url {@link URL} to the specified content.
     * @return Length in bytes or -1 on error.
     */
    public static long getContentLength(final URL url) {
        return getContentLength(url, true);
    }

    public static long getContentLength(final URL url, boolean playlist) {
        final int TIMEOUT_LENGTH = 5000;
        long ret = -1;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", LoadFactoryConst.userAgent);
            connection.setReadTimeout(TIMEOUT_LENGTH);
            connection.setConnectTimeout(TIMEOUT_LENGTH);
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                ret = connection.getContentLengthLong();
            }

            // alles unter 300k sind Playlisten, ...
            if (playlist && ret < 300 * 1000) {
                ret = -1;
            }
        } catch (final Exception ex) {
            ret = -1;
            PLog.errorLog(643298301, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ret;
    }
}
