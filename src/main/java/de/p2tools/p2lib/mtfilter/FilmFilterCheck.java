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

    public static boolean checkChannelSmart(Filter sender, FilmData film) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(film.arr[FilmDataXml.FILM_CHANNEL])) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkChannel(Filter sender, FilmData film) {
        if (sender.exact) {
            if (!sender.filter.equalsIgnoreCase(film.arr[FilmDataXml.FILM_CHANNEL])) {
                return false;
            }
        } else {
            if (!FilterCheck.check(sender, film.arr[FilmDataXml.FILM_CHANNEL])) {
                return false;
            }
        }
        return true;
    }


    public static boolean checkThemeExact(Filter theme, FilmData film) {
        if (theme.exact) {
            // da ist keine Form optimal?? aber so passt es zur Sortierung der Themenliste
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

    public static boolean checkTheme(Filter theme, FilmData film) {
        if (!FilterCheck.check(theme, film.arr[FilmDataXml.FILM_THEME])) {
            return false;
        }
        return true;
    }

    public static boolean checkThemeTitle(Filter themeTitle, FilmData film) {
        if (!FilterCheck.check(themeTitle, film.arr[FilmDataXml.FILM_THEME])
                && !FilterCheck.check(themeTitle, film.arr[FilmDataXml.FILM_TITLE])) {
            return false;
        }
        return true;
    }

    public static boolean checkTitle(Filter title, FilmData film) {
        if (!FilterCheck.check(title, film.arr[FilmDataXml.FILM_TITLE])) {
            return false;
        }
        return true;
    }

    public static boolean checkSomewhere(Filter somewhere, FilmData film) {
        if (!FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_DATE])
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_THEME])
                && !FilterCheck.check(somewhere, film.arr[FilmDataXml.FILM_TITLE])
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

    public static boolean checkUrl(Filter url, FilmData film) {
        if (!FilterCheck.check(url, film.arr[FilmDataXml.FILM_WEBSITE])
                && !FilterCheck.check(url, film.arr[FilmDataXml.FILM_URL])) {
            return false;
        }
        return true;
    }

    public static boolean checkLengthMin(int filterLangth, long filmLength) {
        return filterLangth == 0 || filmLength == 0 || filmLength >= filterLangth;
    }

    public static boolean checkLengthMax(int filterLaenge, long filmLength) {
        return filterLaenge == FilterCheck.FILTER_DURATION_MAX_MINUTE || filmLength == 0
                || filmLength <= filterLaenge;
    }

    public static boolean checkLength(int filterLeangth_minute_min, int filterLength_minute_max, long filmLength) {
        return checkLengthMin(filterLeangth_minute_min, filmLength)
                && checkLengthMax(filterLength_minute_max, filmLength);
    }

    public static boolean checkFilmTime(int timeMin, int timeMax, boolean invert, int filmTime) {
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

    public static boolean checkMinDur(int minDur, FilmData film) {
        if (minDur == FilterCheck.FILTER_ALL_OR_MIN) {
            return true;
        }

        final int durationMinute = film.getDurationMinute();
        if (durationMinute != 0 && durationMinute < minDur) {
            return false;
        }

        return true;
    }

    public static boolean checkMaxDur(int maxDur, FilmData film) {
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