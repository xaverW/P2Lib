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

    public static final DateTimeFormatter FORMAT_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMAT_yyyy = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter FORMAT_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private LocalTime localDate; // todo

    public PLocalTime() {
        localDate = LocalTime.now();
    }

    public PLocalTime(String date) {
        setPDate(date);
    }

    public PLocalTime(LocalTime date) {
        setPDate(date);
    }

    public void setPDate(LocalTime date) {
        localDate = date;
    }

    public void setPDate(String strDate) {
        localDate = getPLocalDate(strDate);
    }

    public void clearPDate() {
        localDate = null;
    }

    public boolean isEmpty() {
        return localDate == null;
    }

    public void setPDateNow() {
        localDate = LocalTime.now();
    }

    public String getDateTime(DateTimeFormatter format) {
        return localDate == null ? "" : localDate.format(format);
    }

    public LocalTime getLocalDate() {
        return localDate;
    }

    public static LocalTime getPLocalDate(String strDate) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(strDate, FORMAT_dd_MM_yyyy);
        } catch (final Exception ex) {
        }

        return null;
    }

    @Override
    public String toString() {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_dd_MM_yyyy);
        }
    }

    public String toStringR() {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_yyyy_MM_dd);
        }
    }

    @Override
    public int compareTo(PLocalTime o) {

        if (o == null || o.localDate == null) {
            return 1;
        }

        if (this.localDate == null) {
            return -1;
        }

        return this.localDate.compareTo(o.localDate);
    }
}
