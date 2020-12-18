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

public class PLocalTime implements Comparable<PLocalTime> {

    public static final DateTimeFormatter FORMAT_HH_mm = DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime localTime; // todo

    public PLocalTime() {
        localTime = LocalTime.now();
    }

    public PLocalTime(String date) {
        setPLocalTime(date);
    }

    public PLocalTime(LocalTime date) {
        setPLocalTime(date);
    }

    public void setPLocalTime(LocalTime date) {
        localTime = date;
    }

    public void setPLocalTime(String strDate) {
        localTime = getPLocalTime(strDate);
    }

    public void clearPLocalTime() {
        localTime = null;
    }

    public boolean isEmpty() {
        return localTime == null;
    }

    public void setPLocalTimeNow() {
        localTime = LocalTime.now();
    }

    public String getPLocalTime(DateTimeFormatter format) {
        return localTime == null ? "" : localTime.format(format);
    }

    public LocalTime getLocalTime() {
        return localTime;
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

    @Override
    public String toString() {
        if (localTime == null) {
            return "";
        } else {
            return localTime.format(FORMAT_HH_mm);
        }
    }

    public String toStringR() {
        if (localTime == null) {
            return "";
        } else {
            return localTime.format(FORMAT_HH_mm);
        }
    }

    @Override
    public int compareTo(PLocalTime o) {

        if (o == null || o.localTime == null) {
            return 1;
        }

        if (this.localTime == null) {
            return -1;
        }

        return this.localTime.compareTo(o.localTime);
    }
}
