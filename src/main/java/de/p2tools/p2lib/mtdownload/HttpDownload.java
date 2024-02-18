/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.guitools.pnotification.P2Notification;
import de.p2tools.p2lib.tools.file.P2FileSize;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2lib.tools.net.P2UrlConnectionFactory;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class HttpDownload extends Thread {
    public static final int DEFAULT_BUFFER_SIZE = 4 * 1024; // default byte buffer size
    public static int downloadRunning = 0;
    private final int DOWNLOAD_MAX_RESTART_HTTP = 4;
    private final Stage stage;
    private long downloaded = 0;
    private String responseCode;
    private String exMessage;
    private String url;
    private String destDir;
    private String destName;
    private String destDirFile;
    private String userAgent = "";
    private long fileSize = 0;
    private boolean error = false;
    private DownloadProgressDialog downloadProgressDialog = null;


    public HttpDownload(Stage stage, String url, String destDir, String destName) {
        super();
        this.stage = stage;
        this.url = url;
        this.destDir = destDir;
        this.destName = destName;
        this.destDirFile = P2FileUtils.addsPath(destDir, destName);
        setName("DOWNLOAD FILE THREAD: " + destName);
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public synchronized void run() {
        PLog.sysLog("Download von: " + url + P2LibConst.LINE_SEPARATOR + "nach: " + destDirFile);
        ++downloadRunning;

        try {
            Files.createDirectories(Paths.get(destDir));
        } catch (final IOException ignored) {
        }

        int restartCount = 0;
        boolean restart = true;
        HttpURLConnection conn = null;

        while (restart) {
            restart = false;

            conn = startDownload(conn);
            if (conn == null) {
                if (restartCount < DOWNLOAD_MAX_RESTART_HTTP) {
                    restart = true;
                }
                continue;
            }

            try (InputStream inputStream = conn.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(destDirFile), (downloaded != 0))) {
                downloadContent(inputStream, fileOutputStream);

            } catch (final Exception ex) {
                if ((ex instanceof IOException) &&
                        restartCount < DOWNLOAD_MAX_RESTART_HTTP) {

                    if (ex instanceof java.net.SocketTimeoutException) {
                        // Timeout Fehlermeldung
                        final ArrayList<String> text = new ArrayList<>();
                        text.add("Timeout, Restarts: " + restartCount);
                        text.add("Ziel: " + destDirFile);
                        text.add("URL: " + url);
                        PLog.sysLog(text.toArray(new String[text.size()]));
                    }

                    restartCount++;
                    restart = true;
                } else {
                    // dann weiß der Geier!
                    exMessage = ex.getMessage();
                    PLog.errorLog(974512037, ex, "Fehler");
                    error = true;
                }
            }
        }

        closeConn(conn);
        deleteIfEmptyOrError(new File(destDirFile));

        if (error) {
            // dann hat der Download nicht geklappt
            Platform.runLater(() ->
                    PAlert.showErrorAlert(stage, "Download fehlgeschlagen",
                            "Download von: " + destDirFile + P2LibConst.LINE_SEPARATORx2 +
                                    "Der Download hat nicht geklappt." +
                                    (exMessage != null ? (P2LibConst.LINE_SEPARATORx2 + exMessage) : "")
                    ));

        } else {
            P2Notification.addNotification("Download",
                    "Der Download ist abgeschlossen" + P2LibConst.LINE_SEPARATOR +
                            "und war erfolgreich.", P2Notification.STATE.SUCCESS);
        }
        --downloadRunning;
    }

    private HttpURLConnection startDownload(HttpURLConnection conn) {
        closeConn(conn);

        try {
            final URL url = new URL(this.url);
            fileSize = DownloadFactory.getContentLength(url, false);
            // conn = (HttpURLConnection) url.openConnection();
            conn = P2UrlConnectionFactory.getUrlConnection(url);
            // 250 Sekunden, wie bei Firefox
            int CONECTION_TIMEOUT_SECOND_DOWNLOAD = 250;
            conn.setConnectTimeout(1000 * CONECTION_TIMEOUT_SECOND_DOWNLOAD);
            conn.setReadTimeout(1000 * CONECTION_TIMEOUT_SECOND_DOWNLOAD);

            setupHttpConnection(conn);
            conn.connect();
            final int httpResponseCode = conn.getResponseCode();
            if (httpResponseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                // Range passt nicht, also neue Verbindung versuchen...
                if (httpResponseCode == 416) {
                    conn.disconnect();
                    // Get a new connection and reset download param...
                    // conn = (HttpURLConnection) url.openConnection();
                    conn = P2UrlConnectionFactory.getUrlConnection(url);
                    downloaded = 0;
                    setupHttpConnection(conn);
                    conn.connect();
                    // hier war es dann nun wirklich...
                    if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                        closeConn(conn);
                    }

                } else {
                    // ==================================
                    // dann wars das
                    responseCode = "Responsecode: " + conn.getResponseCode() + P2LibConst.LINE_SEPARATOR + conn.getResponseMessage();
                    PLog.errorLog(989895658, "HTTP-Fehler: " + conn.getResponseCode() + ' ' + conn.getResponseMessage());
                    closeConn(conn);
                }
            }

        } catch (IOException ex) {
            closeConn(conn);
            conn = null;
        }

        return conn;
    }

    private void downloadContent(InputStream inputStream, FileOutputStream fileOutputStream) throws Exception {
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        double p, pp = 0;
        int len;
        long date1 = 0;
        long date2 = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            //wenn der Download zu schnell ist, kommt der Dialog erst danach und beendet sich nicht mehr :)
            downloadProgressDialog = new DownloadProgressDialog(stage, destName, getText());
            countDownLatch.countDown();
        });
        countDownLatch.await();

        while ((len = inputStream.read(buffer)) != -1) {
            date1 = new Date().getTime();
            if (downloadProgressDialog != null && downloadProgressDialog.isCanceled()) {
                error = true;
                break;
            }

            downloaded += len;
            fileOutputStream.write(buffer, 0, len);

            if (downloaded > 0 && date1 - date2 > 500 && downloadProgressDialog != null) {
                date2 = new Date().getTime();

                p = 1.0 * downloaded / fileSize;
                long l = (long) (p * 1000);
                p = 1.0 * l / 1000;
                if (p != pp) {
                    // Fortschritt anzeigen
                    pp = p;
                    downloadProgressDialog.setProgress(p, getText());
                }
            }
        }

        if (downloadProgressDialog != null) {
            downloadProgressDialog.close();
        }

        if (check()) {
            // Anzeige ändern - fertig
        } else {
            // Anzeige ändern - bei Fehler fehlt der Eintrag
        }
    }

    private String getText() {
        return P2FileSize.convertToStr(downloaded) + " von " + P2FileSize.convertToStr(fileSize);
    }

    /**
     * Setup the HTTP connection common settings
     *
     * @param conn The active connection.
     */
    private void setupHttpConnection(HttpURLConnection conn) {
        conn.setRequestProperty("Range", "bytes=" + downloaded + '-');
        if (!userAgent.isEmpty()) {
            conn.setRequestProperty("User-Agent", userAgent);
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }

    private boolean check() {
        // prüfen ob der Download geklappt hat und die Datei existiert
        boolean ret = false;

        final File file = new File(destDirFile);
        if (!file.exists()) {
            PLog.errorLog(951203473, "Download fehlgeschlagen: Datei existiert nicht: " + destDirFile);
        } else {
            ret = true;
        }

        return ret;
    }

    private void closeConn(HttpURLConnection conn) {
        try {
            if (conn != null)
                conn.disconnect();
        } catch (Exception ignored) {
        }
    }

    private void deleteIfEmptyOrError(File file) {
        try {
            if (file.exists() && (error || file.length() == 0)) {
                error = true;
                // zum Wiederstarten/Aufräumen die leer/zu kleine Datei löschen, alles auf Anfang
                PLog.sysLog(new String[]{"Fehler oder leere Datei: löschen", file.getAbsolutePath()});
                if (!file.delete()) {
                    throw new Exception();
                }
            }
        } catch (final Exception ex) {
            PLog.errorLog(316704568, "Fehler beim löschen" + file.getAbsolutePath());
        }
    }
}
