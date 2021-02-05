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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PLocalDate implements Comparable<PLocalDate> {

    public static final DateTimeFormatter FORMAT_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMAT_d_MM_yyyy = DateTimeFormatter.ofPattern("d.MM.yyyy");
    public static final DateTimeFormatter FORMAT_dd_M_yyyy = DateTimeFormatter.ofPattern("dd.M.yyyy");
    public static final DateTimeFormatter FORMAT_d_M_yyyy = DateTimeFormatter.ofPattern("d.M.yyyy");
    public static final DateTimeFormatter FORMAT_yyyy = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter FORMAT_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private LocalDate localDate = null; // todo

    public PLocalDate() {
        setPLocalDate(LocalDate.now());
    }

    public PLocalDate(int days) {
        setPLocalDate(LocalDate.now());
        setPLocalDateNextDay(days);
    }

    public PLocalDate(String date) {
        setPLocalDate(date);
    }

    public PLocalDate(LocalDate date) {
        setPLocalDate(date);
    }

    //===================
    public void setPLocalDate(LocalDate date) {
        localDate = date;
    }

    public void setPLocalDate(PLocalDate date) {
        localDate = date.getLocalDate();
    }

    public void setPLocalDate(String strDate) {
        localDate = PDateFactory.getPLocalDate(strDate);
    }

    public void setPLocalDate(PDate date) {
        localDate = PDateFactory.getPLocalDate(date.toString());
    }

    public void setPLocalDateNow() {
        localDate = LocalDate.now();
    }

    //====================
    public boolean isEmpty() {
        return localDate == null;
    }

    public void clearPDate() {
        localDate = null;
    }

    public String getDateTime(DateTimeFormatter format) {
        return localDate == null ? "" : localDate.format(format);
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDate getNewLocalDate() {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
    }

    public PLocalDate getNewPLocalDate() {
        return new PLocalDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()));
    }

    public PDate getPDate() {
        try {
            PDate pd = new PDate();
            pd.setPDate(getDateTime(FORMAT_dd_MM_yyyy));
            return pd;
        } catch (final Exception ex) {
        }

        return null;
    }

    public String getYear() {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_yyyy);
        }
    }

    public void setPLocalDateYesterdy() {
        setPLocalDateNow();
        localDate = localDate.minusDays(1);
    }

    public void setPLocalDateBeforeDay() {
        localDate = localDate.minusDays(1);
    }

    public void setPLocalDateNextDay() {
        localDate = localDate.plusDays(1);
    }

    public void setPLocalDateNextDay(int days) {
        localDate = localDate.plusDays(days);
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
    public int compareTo(PLocalDate o) {

        if (o == null || o.localDate == null) {
            return 1;
        }

        if (this.localDate == null) {
            return -1;
        }

        return this.localDate.compareTo(o.localDate);
    }
}
