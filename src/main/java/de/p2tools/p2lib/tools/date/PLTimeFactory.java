/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.tools.date;

import java.time.LocalTime;

public class PLTimeFactory {

    private PLTimeFactory() {
    }

    public static LocalTime fromString_HM(String strDate) {
        try {
            if (strDate.isEmpty()) {
                return LocalTime.MIN;
            } else {
                return LocalTime.parse(strDate, DateFactory.DT_FORMATTER_HH__mm);
            }
        } catch (Exception ex) {
            return LocalTime.MIN;
        }
    }

    public static String toString_HM(LocalTime localTime) {
        if (localTime == null) {
            return "";
        } else if (localTime.compareTo(LocalTime.MIN) == 0) {
            return "";
        } else {
            return localTime.format(DateFactory.DT_FORMATTER_HH__mm);
        }
    }

    public static LocalTime fromString(String strDate) {
        try {
            if (strDate.isEmpty()) {
                return LocalTime.MIN;
            } else {
                return LocalTime.parse(strDate, DateFactory.DT_FORMATTER_HH__mm__ss);
            }
        } catch (Exception ex) {
            return LocalTime.MIN;
        }
    }

    public static String toString(LocalTime localTime) {
        if (localTime == null) {
            return "";
        } else if (localTime.compareTo(LocalTime.MIN) == 0) {
            return "";
        } else {
            return localTime.format(DateFactory.DT_FORMATTER_HH__mm__ss);
        }
    }
}
