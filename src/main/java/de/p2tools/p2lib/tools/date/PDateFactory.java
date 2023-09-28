/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.tools.log.PLog;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDateFactory {

    public static String getTodayStr() {
        return new PDate().getDateTime(DateFactory.F_FORMAT_dd_MM_yyyy);
    }

    public static String getTodayInverseStr() {
        return new PDate().getDateTime(DateFactory.F_FORMAT_yyyy_MM_dd);
    }

    public static PDate getToday() {
        PDate pDate = new PDate();
        pDate.setPDateToday();
        return pDate;
    }

    public static PDate getYesterday() {
        LocalDate pDate = LocalDate.now().minusDays(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return new PDate(Date.from(pDate.atStartOfDay(defaultZoneId).toInstant()));
    }

    public static String getAktYearStr() {
        return LocalDate.now().getYear() + "";
    }

    public static int getActYearInt() {
        return LocalDate.now().getYear();
    }

    public static int getYearIntFromString(String year) {
        int ret;
        try {
            final long y = DateFactory.F_FORMAT_yyyy.parse(year).getTime();
            ret = Integer.parseInt(year);
        } catch (Exception ex) {
            ret = 0;
            PLog.errorLog(621212154, "Jahr: " + year);
        }

        return ret;
    }

    public static List<String> getYearListSince(String year) {
        List<String> list = new ArrayList<>();
        int aktYear = getActYearInt();
        int startCheckYear = getYearIntFromString(year);

        if (startCheckYear <= 0 ||
                aktYear < startCheckYear) {
            return list;
        }

        while (startCheckYear <= aktYear) {
            list.add(startCheckYear + "");
            ++startCheckYear;
        }

        return list;
    }

    public static List<Integer> getYearListSince(int year) {
        List<Integer> list = new ArrayList<>();

        int aktYear = getActYearInt();
        int startCheckYear = getYearIntFromString(year + "");

        if (startCheckYear <= 0 ||
                aktYear < startCheckYear) {
            return list;
        }

        while (startCheckYear <= aktYear) {
            list.add(startCheckYear);
            ++startCheckYear;
        }

        return list;
    }

    public static LocalDate getLocalDate(String strDate) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(strDate, DateFactory.DT_FORMATTER_dd_MM_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DateFactory.DT_FORMATTER_d_MM_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DateFactory.DT_FORMATTER_dd_M_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DateFactory.DT_FORMATTER_d_M_yyyy);
        } catch (final Exception ex) {
        }

        try {
            return LocalDate.parse(strDate, DateFactory.DT_FORMATTER_EEE_MMM_dd_);
        } catch (final Exception ex) {
        }

        return null;
    }

//    public static String getDate_yMd(String d) {
//        try {
//            LocalDate pLocalDate = LocalDate.parse(d, DT_FORMATTER_yyyy_MM_dd);
//            if (pLocalDate != null) {
//                return pLocalDate.format(DT_FORMATTER_dd_MM_yyyy);
//            }
//        } catch (final Exception ex) {
//        }
//
//        return d;
//    }

    /**
     * Liefert den Betrag! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Sekunden.
     */
    public static int diffInSeconds(Date date) {
        final int ret = (int) (1L * (date.getTime() - new Date().getTime()) / 1000L);
        return Math.abs(ret);
    }

    public static String toString(PDate pDate) {
        if (pDate == null) {
            return "";
        } else {
            return pDate.getDateTime(DateFactory.F_FORMAT_dd_MM_yyyy);
        }
    }

    public static String toStringR(PDate pDate) {
        if (pDate == null) {
            return "";
        } else {
            return pDate.getDateTime(DateFactory.F_FORMAT_yyyy_MM_dd);
        }
    }


}
