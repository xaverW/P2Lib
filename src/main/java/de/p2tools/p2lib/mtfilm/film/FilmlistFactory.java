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

package de.p2tools.p2lib.mtfilm.film;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.ListProperty;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SimpleTimeZone;

@SuppressWarnings("serial")
public class FilmlistFactory {
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static int countDouble = 0;

    private FilmlistFactory() {
    }

    public static String genDate(String[] metaData) {
        // Tag, Zeit in lokaler Zeit wann die Filmliste erstellt wurde
        // in der Form "dd.MM.yyyy, HH:mm"
        String ret;
        String date;
        final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

        if (metaData[FilmlistXml.FILMLIST_DATE_GMT_NR].isEmpty()) {
            // noch eine alte Filmliste
            return metaData[FilmlistXml.FILMLIST_DATE_NR];

        } else {
            date = metaData[FilmlistXml.FILMLIST_DATE_GMT_NR];
            //sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            Date filmDate = null;
            try {
                filmDate = sdfUtc.parse(date);
            } catch (final ParseException ignored) {
            }

            if (filmDate == null) {
                ret = metaData[FilmlistXml.FILMLIST_DATE_GMT_NR];

            } else {
                final FastDateFormat formatter = FastDateFormat.getInstance(DATE_TIME_FORMAT);
                ret = formatter.format(filmDate);
            }
        }

        return ret;
    }

    public static void updateList(ListProperty<? extends FilmData> filmList, ListProperty<? extends FilmData> addList,
                                  boolean index /* Vergleich über Index, sonst nur URL */, boolean replace) {

        // in eine vorhandene Liste soll eine andere Filmliste einsortiert werden
        // es werden nur Filme die noch nicht vorhanden sind, einsortiert
        // "ersetzen": true: dann werden gleiche (index/URL) in der Liste durch neue ersetzt
        final HashSet<String> hash = new HashSet<>(addList.size() + 1, 0.75F);

        if (replace) {
            addList.forEach((FilmData f) -> addHash(f, hash, index));

            final Iterator<? extends FilmData> it = filmList.iterator();
            while (it.hasNext()) {
                final FilmData f = it.next();
                if (f.arr[FilmDataXml.FILM_CHANNEL].equals(LoadFactoryConst.KIKA)) {
                    // beim KIKA ändern sich die URLs laufend
                    if (hash.contains(f.arr[FilmDataXml.FILM_THEME] + f.arr[FilmDataXml.FILM_TITLE])) {
                        it.remove();
                    }
                } else if (index) {
                    if (hash.contains(f.getIndex())) {
                        it.remove();
                    }
                } else if (hash.contains(f.getUrlForHash())) {
                    it.remove();
                }
            }

            addList.forEach((FilmData f) -> addInit(filmList, f));
        } else {
            // ==============================================
            filmList.forEach(f -> addHash(f, hash, index));

            for (final FilmData f : addList) {
                if (f.arr[FilmDataXml.FILM_CHANNEL].equals(LoadFactoryConst.KIKA)) {
                    if (!hash.contains(f.arr[FilmDataXml.FILM_THEME] + f.arr[FilmDataXml.FILM_TITLE])) {
                        addInit(filmList, f);
                    }
                } else if (index) {
                    if (!hash.contains(f.getIndex())) {
                        addInit(filmList, f);
                    }
                } else if (!hash.contains(f.getUrlForHash())) {
                    addInit(filmList, f);
                }
            }
        }
        hash.clear();
    }

    private static boolean addInit(ListProperty<? extends FilmData> list, Object film) {
//        film.init(); todo

        if (film instanceof FilmData) {
            return ((ListProperty<FilmData>) list).add((FilmData) film);
        }
        return false;
    }

    private static void addHash(FilmData f, HashSet<String> hash, boolean index) {
        if (f.arr[FilmDataXml.FILM_CHANNEL].equals(LoadFactoryConst.KIKA)) {
            // beim KIKA ändern sich die URLs laufend
            hash.add(f.arr[FilmDataXml.FILM_THEME] + f.arr[FilmDataXml.FILM_TITLE]);
        } else if (index) {
            hash.add(f.getIndex());
        } else {
            hash.add(f.getUrlForHash());
        }
    }

    public static int markFilms(ListProperty<? extends FilmData> filmList) {
        // läuft direkt nach dem Laden der Filmliste!
        // doppelte Filme (URL), Geo, InFuture markieren
        // viele Filme sind bei mehreren Sendern vorhanden

        final HashSet<String> urlHashSet = new HashSet<>(filmList.size(), 0.75F);

        // todo exception parallel?? Unterschied ~10ms (bei Gesamt: 110ms)
        PDuration.counterStart("markFilms");
        try {
            countDouble = 0;
            filmList.forEach((FilmData f) -> {

                f.setGeoBlocked();
                f.setInFuture();

                if (!urlHashSet.add(f.getUrl())) {
                    ++countDouble;
                    f.setDoubleUrl(true);
                }

            });

        } catch (Exception ex) {
            P2Log.errorLog(951024789, ex);
        }
        PDuration.counterStop("markFilms");

        urlHashSet.clear();
        return countDouble;
    }


    /**
     * Get the age of the film list.
     *
     * @return Age in seconds.
     */
    public static int getAge(String strDate) {
        int ret = P2LibConst.NUMBER_NULL;
        final Date now = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
//        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

        if (!strDate.isEmpty()) {
            Date date = getDate(strDate, sdfUtc);
            if (date != null) {
                ret = Math.round((now.getTime() - date.getTime()) / (1000));
                if (ret < 0) {
                    ret = P2LibConst.NUMBER_NULL;
                }
            }
        }
        return ret;
    }

    /**
     * Get the age of the film list.
     *
     * @return Age in seconds.
     */
    public static int getAge(String[] metaData) {
        int ret = 0;
        final Date now = new Date(System.currentTimeMillis());
        final Date filmDate = getAgeAsDate(metaData);
        if (filmDate != null) {
            ret = Math.round((now.getTime() - filmDate.getTime()) / (1000));
            if (ret < 0) {
                ret = 0;
            }
        }
        return ret;
    }

    /**
     * Get the age of the film list.
     *
     * @return Age as a {@link Date} object.
     */
    public static Date getAgeAsDate(String[] metaData) {
        final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

        if (!metaData[FilmlistXml.FILMLIST_DATE_GMT_NR].isEmpty()) {
            final String date = metaData[FilmlistXml.FILMLIST_DATE_GMT_NR];
            return getDate(date, sdfUtc);

        } else {
            final String date = metaData[FilmlistXml.FILMLIST_DATE_NR];
            return getDate(date, sdf);
        }
    }

    /**
     * Get the age of the film list.
     *
     * @return Age as a {@link java.util.Date} object.
     */
    public static String getAgeAsStringDate(String[] metaData) {
        String ret = "";
        Date date = FilmlistFactory.getAgeAsDate(metaData);
        final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        if (date != null) {
            try {
                ret = sdf.format(date);
            } catch (Exception ignore) {
                ret = "";
            }
        }
        return ret;
    }


    private static Date getDate(String date, SimpleDateFormat df) {
        if (date.isEmpty()) {
            // dann ist die Filmliste noch nicht geladen
            return null;
        }

        Date filmDate = null;
        try {
            filmDate = df.parse(date);
        } catch (final Exception ignored) {
        }

        return filmDate;
    }

    /**
     * Check if available Filmlist is older than a specified value.
     *
     * @return true if too old or if the list is empty.
     */
    public static boolean isTooOldOrEmpty(ListProperty<? extends FilmData> filmList, String[] metaData) {
        if (LoadFactoryConst.debug) {
            //im Debugmodus nie automatisch laden
            return false;
        }
        if (filmList.isEmpty()) {
            return true;
        }
        return isOlderThan(metaData, LoadFactoryConst.ALTER_FILMLISTE_SEKUNDEN_FUER_AUTOUPDATE);
    }

    /**
     * Check if available Filmlist is older than a specified value.
     *
     * @return true if too old or if the list is empty.
     */
    public static boolean isTooOld(String strAge) {
        if (LoadFactoryConst.debug) {
            //im Debugmodus nie automatisch laden
            return false;
        }
        if (strAge.isEmpty()) {
            //dann ist das Alter nicht gesetzt
            P2Log.sysLog("Die Filmliste hat kein Alter gespeichert -> Neue laden");
            return true;
        }
        int age = getAge(strAge);
        if (age == P2LibConst.NUMBER_NULL) {
            //dann ist das Alter nicht gesetzt
            P2Log.sysLog("Die Filmliste hat kein Alter gespeichert -> Neue laden");
            return true;
        }
        return isOlderThan(age, LoadFactoryConst.ALTER_FILMLISTE_SEKUNDEN_FUER_AUTOUPDATE);
    }

    /**
     * Check if Filmlist is too old for using a diff list.
     *
     * @return true if empty or too old.
     */
    public static boolean isTooOldForDiffOrEmpty(ListProperty<? extends FilmData> filmList, String[] metaData) {
        if (filmList.isEmpty()) {
            return true;
        }
        try {
            final String dateMaxDiff_str = new SimpleDateFormat("yyyy.MM.dd__").format(new Date()) +
                    LoadFactoryConst.TIME_MAX_AGE_FOR_DIFF;//2023.02.10__09:00:00
            final Date dateMaxDiff = new SimpleDateFormat("yyyy.MM.dd__HH:mm:ss").parse(dateMaxDiff_str);

            final Date dateFilmlist = getAgeAsDate(metaData);

            if (dateFilmlist != null) {
                return dateFilmlist.getTime() < dateMaxDiff.getTime();//vor: 2023.02.10__09:00:00
            }
        } catch (final Exception ignored) {
        }

        return true;
    }

    public static boolean isTooOldForDiff(String strDate) {
        if (LoadFactoryConst.debug) {
            //im Debugmodus nie automatisch laden
            return false;
        }
        if (strDate.isEmpty()) {
            //dann ist das Alter nicht gesetzt
            P2Log.sysLog("Die Filmliste hat kein Alter gespeichert -> Neue laden");
            return true;
        }

        try {
            final String dateMaxDiff_str = new SimpleDateFormat("yyyy.MM.dd__").format(new Date()) +
                    LoadFactoryConst.TIME_MAX_AGE_FOR_DIFF;//2023.02.10__09:00:00
            final Date dateMaxDiff = new SimpleDateFormat("yyyy.MM.dd__HH:mm:ss").parse(dateMaxDiff_str);

            final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
//            sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            Date date = getDate(strDate, sdfUtc);

            if (date != null) {
                return date.getTime() < dateMaxDiff.getTime();
            }
        } catch (final Exception ignored) {
        }

        return true;
    }


    /**
     * Check if list is older than specified parameter.
     *
     * @param second The age in seconds.
     * @return true if older.
     */
    public static boolean isOlderThan(String[] metaData, int second) {
        final int ret = getAge(metaData);
        if (ret != 0) {
            P2Log.sysLog("Die Filmliste ist " + ret / 60 + " Minuten alt");
        }
        return ret > second;
    }

    /**
     * Check if list is older than specified parameter.
     *
     * @param second The age in seconds.
     * @return true if older.
     */
    public static boolean isOlderThan(int age, int second) {
        if (age != 0) {
            P2Log.sysLog("Die Filmliste ist " + age / 60 + " Minuten alt");
        }
        return age > second;
    }
}
