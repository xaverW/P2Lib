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

public class FilterCheck {

    public static final String FILTER_SHOW_DATE_ALL = "";
    public static final int FILTER_ALL_OR_MIN = 0;
    public static final int FILTER_DURATION_MAX_MINUTE = 150;//Filmlänge [Minuten]
    public static final int FILTER_TIME_MAX_SEC = 24 * 60 * 60;//Sendezeit [Minuten], das ist eigentlich bereits 00:00 vom nächsten Tag!!
    public static final int FILTER_TIME_RANGE_MAX_VALUE = 50;//Zeitraum zurück [Tag]

    private FilterCheck() {
    }

    public static boolean checkLowerCase(Filter filter, String checkString, String checkStrLowerCase) {
        // wenn einer passt, dann ists gut
        if (filter.pattern != null) {
            // dann ists eine RegEx
            return filter.pattern.matcher(checkString).matches();
        }

        if (filter.exclude) {
            //dann werden die Begriffe ausgeschlossen
            return !checkInclude(filter, checkStrLowerCase);
        } else {
            //dann müssen die Begriffe enthalten sein
            return checkInclude(filter, checkStrLowerCase);
        }
    }

    public static boolean check(Filter filter, String checkString) {
        // wenn einer passt, dann ists gut
        if (filter.pattern != null) {
            // dann ists eine RegEx
            return filter.pattern.matcher(checkString).matches();
        }

        if (filter.exclude) {
            //dann werden die Begriffe ausgeschlossen
            return !checkInclude(filter, checkString.toLowerCase());
        } else {
            //dann müssen die Begriffe enthalten sein
            return checkInclude(filter, checkString.toLowerCase());
        }
    }

    private static boolean checkInclude(Filter filter, String im) {
        if (filter.isFilterAnd) {
            //Suchbegriffe müssen alle passen
            for (final String s : filter.filterArr) {
                //dann jeden Suchbegriff checken
                if (!im.contains(s)) {
                    return false;
                }
            }
            return true;

        } else {
            //nur ein Suchbegriff muss passen
            for (final String s : filter.filterArr) {
                //dann jeden Suchbegriff checken
                if (im.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}