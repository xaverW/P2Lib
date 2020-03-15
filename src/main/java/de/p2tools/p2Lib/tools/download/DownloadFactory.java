/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.tools.download;

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.stage.Stage;

import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFactory {
    private DownloadFactory() {
    }

    public static boolean downloadFile(Stage stage, String url) {
        return downloadFile(stage, url, "");
    }

    public static boolean downloadFile(Stage stage, String url, String fileName) {
        boolean ret = false; // true if started
        DownloadDialogController downloadDialogController = new DownloadDialogController(stage, url, fileName);

        if (downloadDialogController.getOk()) {
            ret = true;
            PLog.sysLog("Download wird gestartet");
            Thread download = new HttpDownload(stage,
                    url, downloadDialogController.getDestPath(), downloadDialogController.getDestName());
            download.start();

        } else {
            PLog.sysLog("Download wird nicht gestartet");
        }

        return ret;
    }

    private boolean download(String url, String destFile) {
        boolean ret = false;

        return ret;
    }

    /**
     * Return the content length of the requested Url.
     *
     * @param url                                 {@link URL} to the specified content.
     * @param userAgent
     * @param CONNECTIION_TIMEOUT_SECOND_FILESIZE
     * @return Length in bytes or -1 on error.
     */
    public static long getContentLength(final URL url, String userAgent, int CONNECTIION_TIMEOUT_SECOND_FILESIZE) {
        long ret = -1;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            if (!userAgent.isEmpty()) {
                connection.setRequestProperty("User-Agent", userAgent);
            }
            connection.setReadTimeout(1000 * CONNECTIION_TIMEOUT_SECOND_FILESIZE);
            connection.setConnectTimeout(1000 * CONNECTIION_TIMEOUT_SECOND_FILESIZE);
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                ret = connection.getContentLengthLong();
            }
        } catch (final Exception ex) {
            ret = -1;
            PLog.errorLog(202323247, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ret;
    }

}
