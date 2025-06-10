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

package de.p2tools.p2lib.mediathek.filmdata;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.mediathek.film.FilmDate;
import de.p2tools.p2lib.mediathek.film.FilmFactory;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.tools.log.P2Log;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FilmData extends FilmDataProps implements P2Data {

    public String FILM_CHANNEL_STR = "";
    public String FILM_THEME_STR = "";
    public String FILM_TITLE_STR = "";

    public static final String RESOLUTION_ASK = "ask";
    public static final String RESOLUTION_NORMAL = "normal";
    public static final String RESOLUTION_HD = "hd";
    public static final String RESOLUTION_SMALL = "small";
    public static final int FILM_TIME_EMPTY = -1;
    public static final String GEO_DE = "DE";
    public static final String GEO_FR = "FR";
    public static final String GEO_AT = "AT";
    public static final String GEO_CH = "CH";
    public static final String GEO_EU = "EU";
    public static final String GEO_WELT = "WELT";


    public FilmData() {
    }

    @Override
    public String getTag() {
        return "";
    }

    @Override
    public String getComment() {
        return "";
    }

    @Override
    public Config[] getConfigsArr() {
        return null;
    }

    public void init() {
        setLive(arr[FILM_THEME].equals(FilmFactory.THEME_LIVE));
        setHd(!arr[FILM_URL_HD].isEmpty());
        setSmall(!arr[FILM_URL_SMALL].isEmpty());
        setUt(!arr[FILM_URL_SUBTITLE].isEmpty());
        preserveMemory();

        // ================================
        // Dateigröße
        filmSize.setFilmSize(this);

        // ================================
        // Filmdauer
        setFilmLength();

        // ================================
        // Datum
        setDatum();

        //=================================
        // Filmzeit
        setFilmTime();
    }

    private void setFilmTime() {
        if (!arr[FILM_TIME].isEmpty()) {
            try {
                if (arr[FILM_TIME].length() == 5) {
                    arr[FILM_TIME] = arr[FILM_TIME] + ":00";
                }
                LocalTime time = LocalTime.parse(arr[FILM_TIME], DateTimeFormatter.ofPattern("HH:mm:ss"));
                setFilmTime(time.toSecondOfDay());
            } catch (Exception ignore) {
                arr[FILM_TIME] = "";
                setFilmTime(FILM_TIME_EMPTY);
            }
        } else {
            setFilmTime(FILM_TIME_EMPTY);
        }
    }

    public void setLowerCase() {
        FILM_CHANNEL_STR = getChannel().toLowerCase();
        FILM_THEME_STR = getTheme().toLowerCase();
        FILM_TITLE_STR = getTitle().toLowerCase();
    }

    public void clearLowerCase() {
        FILM_CHANNEL_STR = "";
        FILM_THEME_STR = "";
        FILM_TITLE_STR = "";
    }

    public String getUrlForResolution(String resolution) {
        if (resolution.equals(RESOLUTION_SMALL)) {
            return getUrlNormalSmall();
        }
        if (resolution.equals(RESOLUTION_HD)) {
            return getUrlNormalHd();
        }
        return arr[FILM_URL];
    }

    public String getIndex() {
        // liefert einen eindeutigen Index für die Filmliste (update der Filmliste mit Diff-Liste)
        // URL beim KiKa und ORF ändern sich laufend!
        return (arr[FILM_CHANNEL] + arr[FILM_THEME]).toLowerCase() + getUrlForHash();
    }


    public String getUrlForHash() {
        // liefert die URL zum VERGLEICHEN!!
        String url = "";
        if (arr[FILM_CHANNEL].equals(P2LoadConst.ORF)) {
            final String uurl = arr[FILM_URL];
            try {
                final String online = "/online/";
                url = uurl.substring(uurl.indexOf(online) + online.length());
                if (!url.contains("/")) {
                    P2Log.errorLog(915230478, "Url: " + uurl);
                    return "";
                }
                url = url.substring(url.indexOf('/') + 1);
                if (!url.contains("/")) {
                    P2Log.errorLog(915230478, "Url: " + uurl);
                    return "";
                }
                url = url.substring(url.indexOf('/') + 1);
                if (url.isEmpty()) {
                    P2Log.errorLog(915230478, "Url: " + uurl);
                    return "";
                }
            } catch (final Exception ex) {
                P2Log.errorLog(915230478, ex, "Url: " + uurl);
            }
            return P2LoadConst.ORF + "----" + url;
        } else {
            return arr[FILM_URL];
        }

    }

    private void preserveMemory() {
        // ================================
        // Speicher sparen
        if (arr[FILM_SIZE].length() < 3) { //todo brauchts das überhaupt??
            arr[FILM_SIZE] = arr[FILM_SIZE].intern();
        }
        if (arr[FILM_URL_SMALL].length() < 15) {
            arr[FILM_URL_SMALL] = arr[FILM_URL_SMALL].intern();
        }

        arr[FILM_DATE] = arr[FILM_DATE].intern();
        arr[FILM_TIME] = arr[FILM_TIME].intern();
    }

    private String fillString(int anz, String s) {
        while (s.length() < anz) {
            s = '0' + s;
        }
        return s;
    }

    private void setFilmLength() {
        long durSecond;
        try {
            if (!arr[FILM_DURATION].contains(":") && !arr[FILM_DURATION].isEmpty()) {
                // nur als Übergang bis die Liste umgestellt ist
                durSecond = Long.parseLong(arr[FILM_DURATION]);
                setDur(durSecond);
                if (durSecond > 0) {
                    final long hours = durSecond / 3600;
                    durSecond = durSecond - (hours * 3600);
                    final long min = durSecond / 60;
                    durSecond = durSecond - (min * 60);
                    final long seconds = durSecond;
                    arr[FILM_DURATION] = fillString(2, String.valueOf(hours)) + ':'
                            + fillString(2, String.valueOf(min))
                            + ':'
                            + fillString(2, String.valueOf(seconds));
                } else {
                    arr[FILM_DURATION] = "";
                }

            } else {
                durSecond = 0;
                if (!arr[FILM_DURATION].isEmpty()) {
                    final String[] parts = arr[FILM_DURATION].split(":");
                    long power = 1;
                    for (int i = parts.length - 1; i >= 0; i--) {
                        durSecond += Long.parseLong(parts[i]) * power;
                        power *= 60;
                    }
                }
                setDur(durSecond);
            }

        } catch (final Exception ex) {
            setDur(0);
            P2Log.errorLog(468912049, "Dauer: " + arr[FILM_DURATION]);
        }
    }

    private void setDur(long durSecond) {
        if (durSecond <= 0) {
            setDurationMinute(0);
            return;
        }

        int d = (int) (durSecond / 60);
        if (d <= 0) {
            d = 1;
        }

        setDurationMinute(d);
    }

    private void setDatum() {
        filmDate.setTime(0);

        if (!arr[FILM_DATE].isEmpty()) {
            // nur dann gibts ein Datum
            try {
                if (arr[FILM_DATE_LONG].isEmpty()) {
                    if (arr[FILM_TIME].isEmpty()) {
                        filmDate = new FilmDate(sdf_date.parse(arr[FILM_DATE]).getTime());
                    } else {
                        filmDate = new FilmDate(sdf_date_time.parse(arr[FILM_DATE] + arr[FILM_TIME]).getTime());
                    }
                    arr[FILM_DATE_LONG] = String.valueOf(filmDate.getTime() / 1000);
                } else {
                    final long l = Long.parseLong(arr[FILM_DATE_LONG]);
                    filmDate = new FilmDate(l * 1000 /* sind SEKUNDEN!! */);
                }
            } catch (final Exception ex) {
                P2Log.errorLog(915236701, ex, new String[]{"Datum: " + arr[FILM_DATE], "Zeit: " + arr[FILM_TIME]});
                filmDate = new FilmDate(0);
                arr[FILM_DATE] = "";
                arr[FILM_TIME] = "";
            }
        }
    }


    private String getUrlNormalSmall() {
        // liefert die kleine normale URL
        if (!arr[FILM_URL_SMALL].isEmpty()) {
            try {
                final int i = Integer.parseInt(arr[FILM_URL_SMALL].substring(0, arr[FILM_URL_SMALL].indexOf('|')));
                return arr[FILM_URL].substring(0, i)
                        + arr[FILM_URL_SMALL].substring(arr[FILM_URL_SMALL].indexOf('|') + 1);
            } catch (final Exception ignored) {
            }
        }
        return arr[FILM_URL];
    }

    private String getUrlNormalHd() {
        // liefert die HD normale URL
        if (!arr[FILM_URL_HD].isEmpty()) {
            try {
                final int i = Integer.parseInt(arr[FILM_URL_HD].substring(0, arr[FILM_URL_HD].indexOf('|')));
                return arr[FILM_URL].substring(0, i)
                        + arr[FILM_URL_HD].substring(arr[FILM_URL_HD].indexOf('|') + 1);
            } catch (final Exception ignored) {
            }
        }
        return arr[FILM_URL];
    }

    public FilmData getCopy() {
        final FilmData ret = new FilmData();
        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
        ret.filmDate = filmDate;
        ret.no = no;
        ret.filmSize = filmSize;
        ret.setDurationMinute(getDurationMinute());
        ret.setHd(isHd());
        ret.setSmall(isSmall());
        ret.setUt(isUt());
        return ret;
    }
}
