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

import java.util.Date;

public class PIndex {

    private static long counter = new Date().getTime();

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
}
