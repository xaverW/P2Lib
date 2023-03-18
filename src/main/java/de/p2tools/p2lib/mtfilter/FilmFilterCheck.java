/*
 * P2Tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.mtfilter;

import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.FilmDataXml;

public class FilmFilterCheck {

    private FilmFilterCheck() {
    }

    /**
     * Abo und Blacklist prüfen
     *
     * @param sender
     * @param theme
     * @param themeTitle
     * @param title
     * @param somewhere
     * @param filmData
     * @return
     */
    public static boolean checkFilterMatch(Filter sender,
                                           Filter theme,
                                           Filter themeTitle,
                                           Filter title,
                                           Filter somewhere,
                                           FilmData filmData) {

        if (!sender.isEmpty && !checkMatchChannelSmartLowerCase(sender, filmData)) {
            return false;
        }

        if (!theme.isEmpty && !checkMatchThemeExactLowerCase(theme, filmData)) {
            return false;
        }

        if (!themeTitle.isEmpty && !checkMatchThemeTitleLowerCase(themeTitle, filmData)) {
            return false;
        }

        if (!title.isEmpty && !checkMatchTitleLowerCase(title, filmData)) {
            return false;
        }

        if (!somewhere.isEmpty && !checkMatchSomewhereLowerCase(somewhere, filmData)) {
            return false;
        }

        return true;
    }

    public static boolean checkFilterMatch(Filter sender,
                                           Filter theme,
                                           Filter themeTitle,
                                           Filter title,
                                           FilmData filmData) {

        if (!sender.isEmpty && !checkMatchChannelSmartLowerCase(sender, filmData)) {
            return false;
        }

        if (!theme.isEmpty && !checkMatchThemeExactLowerCase(theme, filmData)) {
            return false;
        }

        if (!themeTitle.isEmpty && !checkMatchThemeTitleLowerCase(themeTitle, filmData)) {
            return false;
        }

        if (!title.isEmpty && !checkMatchTitleLowerCase(title, filmData)) {
            return false;
        }

        return true;
    }

    public static boolean checkMatchChannelSmart(Filter sender, FilmData film) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(film.arr[FilmDataXml.FILM_CHANNEL])) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkMatchChannelSmartLowerCase(Filter sender, FilmData film) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equals(film.FILM_CHANNEL_STR)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkMatchThemeExact(Filter theme, FilmData film) {
        if (theme.isExact) {
            if (!theme.filter.equalsIgnoreCase(film.arr[FilmDataXml.FILM_THEME])) {
                return false;
            }
        } else {
            if (!FilterCheck.check(theme, film.arr[FilmDataXml.FILM_THEME])) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkMatchThemeExactLowerCase(Filter theme, FilmData film) {
        if (theme.isExact) {
            if (!theme.filterArr[0].equals(film.FILM_THEME_STR)) {
                //exact: dann werden auch der Kleinbuchstaben verglichen!!
                return false;
            }
        } else {
            if (!FilterCheck.checkLowerCase(theme, film.arr[FilmDataXml.FILM_THEME], film.FILM_THEME_STR)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkMatchTheme(Filter theme, FilmData film) {
        if (!FilterCheck.check(theme, film.arr[FilmDataXml.FILM_THEME])) {
            return false;
        }
        return true;
    }

    private static boolean checkMatchThemeLowerCase(Filter theme, FilmData film) {
        if (!FilterCheck.checkLowerCase(theme, film.arr[FilmDataXml.FILM_THEME], film.FILM_THEME_STR)) {
            return false;
        }
        return true;
    }

    public static boolean checkMatchThemeTitle(Filter themeTitle, FilmData film) {
        if (!FilterCheck.check(themeTitle, film.arr[FilmDataXml.FILM_THEME])
                && !FilterCheck.check(themeTitle, film.arr[FilmDataXml.FILM_TITLE])) {
            return false;
        }
        return true;
    }

    private static boolean checkMatchThemeTitleLowerCase(Filter themeTitle, FilmData film) {
        if (!FilterCheck.checkLowerCase(themeTitle, film.arr[FilmDataXml.FILM_THEME], film.FILM_THEME_STR)
                && !FilterCheck.checkLowerCase(themeTitle, film.arr[FilmDataXml.FILM_TITLE], film.FILM_TITLE_STR)) {
            return false;
        }
        return true;
    }

    public static boolean checkMatchTitle(Filter title, FilmData film) {
        if (!FilterCheck.check(title, film.arr[FilmDataXml.FILM_TITLE])) {
            return false;
        }
        return true;
    }

    private static boolean checkMatchTitleLowerCase(Filter title, FilmData film) {
        if (!FilterCheck.checkLowerCase(title, film.arr[FilmDataXml.FILM_TITLE], film.FILM_TITLE_STR)) {
            return false;
        }
        return true;
    }

    public static boolean checkMatchSomewhere(Filter somewhere, FilmData film) {
        if (!FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_DATE])
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_THEME])
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_TITLE])
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_DESCRIPTION])) {
            return false;
        }
        return true;
    }

    private static boolean checkMatchSomewhereLowerCase(Filter somewhere, FilmData film) {
        if (!FilterCheck.checkLowerCase(somewhere, film.arr[FilmDataXml.FILM_DATE], film.arr[FilmDataXml.FILM_DATE].toLowerCase())
                && !FilterCheck.checkLowerCase(somewhere, film.arr[FilmDataXml.FILM_THEME], film.FILM_THEME_STR)
                && !FilterCheck.checkLowerCase(somewhere, film.arr[FilmDataXml.FILM_TITLE], film.FILM_TITLE_STR)
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_DESCRIPTION])) {
            return false;
        }
        return true;
    }

    public static boolean checkMaxDays(int maxDays, long filmTime) {
        long days = 0;
        try {
            if (maxDays == FilterCheck.FILTER_ALL_OR_MIN) {
                days = 0;
            } else {
                final long max = 1000L * 60L * 60L * 24L * maxDays;
                days = System.currentTimeMillis() - max;
            }
        } catch (final Exception ex) {
            days = 0;
        }

        return checkDays(days, filmTime);
    }

    public static boolean checkDays(long days, long filmTime) {
        if (days == 0) {
            return true;
        }

        if (filmTime != 0 && filmTime < days) {
            return false;
        }

        return true;
    }

    public static boolean checkDays(long days, FilmData film) {
        if (days == 0) {
            return true;
        }

        final long filmTime = film.filmDate.getTime();
        if (filmTime != 0 && filmTime < days) {
            return false;
        }

        return true;
    }

    public static boolean checkMatchUrl(Filter url, FilmData film) {
        if (!FilterCheck.check(url, film.arr[FilmDataXml.FILM_WEBSITE])
                && !FilterCheck.check(url, film.arr[FilmDataXml.FILM_URL])) {
            return false;
        }
        return true;
    }

    public static boolean checkMatchLengthMin(int filterLangth, long filmLength) {
        return filterLangth == 0 || filmLength == 0 || filmLength >= filterLangth;
    }

    public static boolean checkMatchLengthMax(int filterLaenge, long filmLength) {
        return filterLaenge == FilterCheck.FILTER_DURATION_MAX_MINUTE || filmLength == 0
                || filmLength <= filterLaenge;
    }

    public static boolean checkMatchLength(int filterLeangth_minute_min, int filterLength_minute_max, long filmLength) {
        return checkMatchLengthMin(filterLeangth_minute_min, filmLength)
                && checkMatchLengthMax(filterLength_minute_max, filmLength);
    }

    public static boolean checkMatchFilmTime(int timeMin, int timeMax, boolean invert, int filmTime) {
        if (filmTime == FilmData.FILM_TIME_EMPTY) {
            return true;
        }

        boolean ret = (timeMin == 0 || filmTime >= timeMin) &&
                (timeMax == FilterCheck.FILTER_TIME_MAX_SEC || filmTime <= timeMax);

        if (invert) {
            return !ret;
        } else {
            return ret;
        }
    }

    public static boolean checkMatchMinDur(int minDur, FilmData film) {
        if (minDur == FilterCheck.FILTER_ALL_OR_MIN) {
            return true;
        }

        final int durationMinute = film.getDurationMinute();
        if (durationMinute != 0 && durationMinute < minDur) {
            return false;
        }

        return true;
    }

    public static boolean checkMatchMaxDur(int maxDur, FilmData film) {
        if (maxDur == FilterCheck.FILTER_DURATION_MAX_MINUTE) {
            return true;
        }

        final int durationMinute = film.getDurationMinute();
        if (durationMinute != 0 && durationMinute > maxDur) {
            return false;
        }

        return true;
    }
}