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


package de.p2tools.p2lib.mediathek.download;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.net.P2UrlConnectionFactory;
import de.p2tools.p2lib.tools.net.PUrlTools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class P2Subtitle {
    private static final int TIMEOUT = 10_000;
    private static final String SUFFIX_TTML = "ttml";
    private static final String SUFFIX_SRT = "srt";
    private static final String SRT_FILETYPE = ".srt";
    private static final String SUFFIX_VTT = "vtt";

    private static String getSubtitleStr(String urlSubtitle, String fileNameWithoutSuffix) {
        String suffix = PUrlTools.getSuffixFromUrl(urlSubtitle);
        if (!suffix.endsWith(SUFFIX_SRT) && !suffix.endsWith(SUFFIX_VTT)) {
            suffix = SUFFIX_TTML;
        }

        return fileNameWithoutSuffix + '.' + suffix;
    }

    public static Path getSubtitlePath(String urlSubtitle, String fileNameWithoutSuffix) {
        Path path;
        try {
            path = Paths.get(getSubtitleStr(urlSubtitle, fileNameWithoutSuffix));
        } catch (Exception ex) {
            path = null;
            P2Log.errorLog(951245412, "SubtitlePath");
        }
        return path;
    }

    public static Path getSrtPath(String fileNameWithoutSuffix) {
        return Paths.get(fileNameWithoutSuffix + SRT_FILETYPE);
    }

    private InputStream getContentDecoder(final String encoding, InputStream in) throws IOException {
        if (encoding != null) {
            InputStream out = null;
            switch (encoding.toLowerCase()) {
                case "gzip":
                    out = new GZIPInputStream(in);
                    break;
                case "deflate":
                    out = new InflaterInputStream(in, new Inflater(true));
                    break;
            }
            return out;
        } else
            return in;
    }

    private void setupConnection(HttpURLConnection conn, String userAgent) {
        conn.setRequestProperty("User-Agent", userAgent);
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setReadTimeout(TIMEOUT);
        conn.setConnectTimeout(TIMEOUT);
    }

    private void downloadContent(InputStream in, String strSubtitelFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(strSubtitelFile)) {
            final byte[] buffer = new byte[64 * 1024];
            int n;
            while ((n = in.read(buffer)) != -1) {
                fos.write(buffer, 0, n);
            }
            P2Log.sysLog(new String[]{"Untertitel", "  geschrieben"});
        }
    }

    private void writeSrt(String strSubtitelFile, String fileNameWithoutSuffix) {
        final Path p = Paths.get(strSubtitelFile);
        final TimedTextMarkupLanguageParser ttmlp = new TimedTextMarkupLanguageParser();
        if (ttmlp.parse(p) || ttmlp.parseXmlFlash(p)) {
            Path srt = getSrtPath(fileNameWithoutSuffix);
            ttmlp.toSrt(srt);
        }
        ttmlp.cleanup();
    }

    public void writeSubtitle(String urlSubtitle, String fileNameWithoutSuffix, String destPath, String userAgent) {
        InputStream in = null;

        if (urlSubtitle.isEmpty()) {
            return;
        }

        try {
            P2Log.sysLog(new String[]{"Untertitel: ", urlSubtitle,
                    "schreiben nach: ", destPath});

            Files.createDirectories(Paths.get(destPath));
            // final HttpURLConnection conn = (HttpURLConnection) new URL(urlSubtitle).openConnection();
            final HttpURLConnection conn = P2UrlConnectionFactory.getUrlConnection(urlSubtitle);
            setupConnection(conn, userAgent);

            if ((conn.getResponseCode()) < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = getContentDecoder(conn.getContentEncoding(), conn.getInputStream());
                final String strSubtitelFile = getSubtitleStr(urlSubtitle, fileNameWithoutSuffix);
                downloadContent(in, strSubtitelFile);
                if (!strSubtitelFile.endsWith('.' + SUFFIX_SRT) && !strSubtitelFile.endsWith("." + SUFFIX_VTT)) {
                    writeSrt(strSubtitelFile, fileNameWithoutSuffix);
                }

            } else {
                // keine Verbindung
                P2Log.errorLog(752301248, "url: " + urlSubtitle);
            }

        } catch (final Exception ignored) {
            P2Log.errorLog(461203210, ignored, "SubtitelUrl: " + urlSubtitle);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final Exception ignored) {
            }
        }
    }
}
