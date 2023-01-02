/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.tools.date;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PLTimeFactory {

    public static final DateTimeFormatter FORMAT_HH_mm = DateTimeFormatter.ofPattern("HH:mm");

    private PLTimeFactory() {
    }

    public static LocalTime getPLocalTime(String strTime) {
        if (strTime == null || strTime.isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(strTime, FORMAT_HH_mm);
        } catch (final Exception ex) {
        }

        return null;
    }

    public static String getLocalTimeStr(LocalTime localTime) {
        if (localTime == null) {
            return "";
        } else {
            return localTime.format(FORMAT_HH_mm);
        }
    }
}
