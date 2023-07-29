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


package de.p2tools.p2lib.tools;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PIndex {

    private static long counter = new Date().getTime();
    private static int counterInt = (int) ChronoUnit.SECONDS.between(
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.now());

    private PIndex() {
    }

    public static synchronized String getIndex(String prefix) {
        final String idx;
        if (prefix == null || prefix.isEmpty()) {
            idx = getIndex() + "";
        } else {
            idx = prefix + "-" + getIndex();
        }
        return idx;
    }

    public static synchronized long getIndex() {
        ++counter;
        return counter;
    }

    public static synchronized String getIndexStr() {
        return getIndex() + "";
    }

    public static synchronized String getIndexInt(String prefix) {
        final String idx;
        if (prefix == null || prefix.isEmpty()) {
            idx = getIndexInt() + "";
        } else {
            idx = prefix + "-" + getIndexInt();
        }
        return idx;
    }

    public static synchronized int getIndexInt() {
        ++counterInt;
        return counterInt;
    }

    public static synchronized String getIndexIntStr() {
        return getIndexInt() + "";
    }
}
