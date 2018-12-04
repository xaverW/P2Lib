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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PLocalDateTime {
    public static final DateTimeFormatter FORMAT_HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter FORMAT_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMAT_yyyy = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter FORMAT_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter FORMAT_dd_MM_yyyyKomma__HH_mm = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    public static final DateTimeFormatter FORMAT_dd_MM_yyyyKomma__HH_mm_ss = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
    public static final DateTimeFormatter FORMAT_dd_MM_yyyy_HH_mm_ss = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private LocalDateTime localDateTime;

    public PLocalDateTime() {
        localDateTime = LocalDateTime.now();
    }

    public PLocalDateTime(String date) {
        setPDate(date);
    }

    public PLocalDateTime(String date, String time) {
        setPDate(date, time);
    }

    public void setPDate(String strDate) {
        setPDate(strDate, "");
    }

    public void setPDate(String strDate, String strTime) {
        if (strDate.isEmpty()) {
            localDateTime = LocalDateTime.now();
            return;
        }

        try {
            if (strTime.isEmpty()) {
                localDateTime = LocalDateTime.parse(strDate, FORMAT_dd_MM_yyyy);
            } else {
                localDateTime = LocalDateTime.parse(strDate + strTime, FORMAT_dd_MM_yyyyKomma__HH_mm);
            }
            return;
        } catch (final Exception ex) {
        }

        localDateTime = LocalDateTime.MIN;
    }

    public void clearPDate() {
        localDateTime = LocalDateTime.MIN;
    }

    public boolean isEmpty() {
        return localDateTime.isEqual(LocalDateTime.MIN);
    }

    public void setPDateNow() {
        localDateTime = LocalDateTime.MIN;
    }

    public String getDateTime(DateTimeFormatter format) {
        return localDateTime.format(format);
    }

    @Override
    public String toString() {
        if (localDateTime.isEqual(LocalDateTime.MIN)) {
            return "";
        } else {
            return localDateTime.format(FORMAT_dd_MM_yyyy);
        }
    }

    public String toStringR() {
        if (localDateTime.isEqual(LocalDateTime.MIN)) {
            return "";
        } else {
            return localDateTime.format(FORMAT_yyyy_MM_dd);
        }
    }

}
