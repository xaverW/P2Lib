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


package de.p2tools.p2lib.tools.net;

import de.p2tools.p2lib.tools.log.P2Log;

import java.net.HttpURLConnection;

public class PUrlTools {
    public static final int TIME_OUT = 10_000;

    public static boolean isUrl(String fileUrl) {
        return fileUrl.toLowerCase().startsWith("http") || fileUrl.toLowerCase().startsWith("www");
    }

    public static boolean urlExists(String url) {
        try {
            // HttpURLConnection.setFollowRedirects(true);
            // HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            HttpURLConnection con = P2UrlConnectionFactory.getUrlConnection(url);
            con.setRequestMethod("GET");
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(10_000);
            final int responseCode = con.getResponseCode();

            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getFileName(String path) {
        // Dateinamen einer URL extrahieren
        String ret = "";
        if (path != null) {
            if (!path.isEmpty()) {
                ret = path.substring(path.lastIndexOf('/') + 1);
            }
        }
        if (ret.contains("?")) {
            ret = ret.substring(0, ret.indexOf('?'));
        }
        if (ret.contains("&")) {
            ret = ret.substring(0, ret.indexOf('&'));
        }
        if (ret.isEmpty()) {
            P2Log.errorLog(395019631, path);
        }
        return ret;
    }

    public static String getSuffixFromUrl(String path) {
        // Suffix einer URL extrahieren
        // "http://ios-ondemand.swr.de/i/swr-fernsehen/bw-extra/20130202/601676.,m,s,l,.mp4.csmil/index_2_av.m3u8?e=b471643725c47acd"
        // https://api.ardmediathek.de/player-service/subtitle/ebutt/urn:ard:subtitle:9f1824d580b624fb
        String ret = "";
        if (path != null) {
            if (!path.isEmpty() && path.contains(".")) {
                ret = path.substring(path.lastIndexOf('.') + 1);
            }
        }
        if (ret.isEmpty()) {
            P2Log.errorLog(969871236, path);
        }
        if (ret.contains("?")) {
            ret = ret.substring(0, ret.indexOf('?'));
        }
        if (ret.length() > 5) {
            // dann ist was faul
            ret = "---";
            P2Log.debugLog("no Suffix for URL: " + path);
        }
        return ret;
    }

    public static String getFileNameWithoutSuffix(String path) {
        // Suffix einer URL extrahieren
        // "http://ios-ondemand.swr.de/i/swr-fernsehen/bw-extra/20130202/601676.,m,s,l,.mp4.csmil/index_2_av.m3u8?e=b471643725c47acd"
        // FILENAME.SUFF
        String ret = "";
        if (path != null) {
            if (!path.isEmpty() && path.contains(".")) {
                ret = path.substring(0, path.lastIndexOf('.'));
            }
        }
        if (ret.isEmpty()) {
            ret = path;
            P2Log.errorLog(945123647, path);
        }
        return ret;
    }

    public static String addUrl(String u1, String u2) {
        if (u1.endsWith("/")) {
            return u1 + u2;
        } else {
            return u1 + '/' + u2;
        }
    }

    public static String removeHtml(String in) {
        return in.replaceAll("\\<.*?>", "");
    }
}
