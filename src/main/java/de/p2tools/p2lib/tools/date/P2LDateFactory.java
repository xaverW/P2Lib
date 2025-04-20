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

import de.p2tools.p2lib.tools.log.P2Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class P2LDateFactory {

    private P2LDateFactory() {
    }

    public static String getDateTime(LocalDate localDate, DateTimeFormatter format) {
        return localDate == null ? "" : localDate.format(format);
    }

    public static int getYearIntFromString(String year) {
        int ret;
        try {
            ret = Integer.parseInt(year);
        } catch (Exception ex) {
            ret = 0;
            P2Log.errorLog(980214963, "Jahr: " + year);
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

    public static String getDate_yMd(String d) {
        try {
            LocalDate localDate = LocalDate.parse(d, P2DateConst.DT_FORMATTER_yyyy_MM_dd);
            if (localDate != null) {
                return localDate.format(P2DateConst.DT_FORMATTER_dd_MM_yyyy);
            }
        } catch (final Exception ex) {
        }

        return d;
    }

//    public LocalDate getLocalDate() {
//        return localDate;
//    }

    public static LocalDate getNewLocalDate(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
    }

//    public PLocalDate getNewPLocalDate() {
//        return new PLocalDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()));
//    }

    public static P2Date getPDate(LocalDate localDate) {
        try {
            P2Date pd = new P2Date();
            pd.setPDate(getDateTime(localDate, P2DateConst.DT_FORMATTER_dd_MM_yyyy));
            return pd;
        } catch (final Exception ex) {
        }

        return null;
    }

    public static String getYear(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(P2DateConst.DT_FORMATTER_yyyy);
        }
    }

    public static int getActYearInt() {
        return LocalDate.now().getYear();
    }

    public static void setPLocalDateBeforeDay(LocalDate localDate) {
        localDate = localDate.minusDays(1);
    }

    public static void setPLocalDateNextDay(LocalDate localDate) {
        localDate = localDate.plusDays(1);
    }

    public static void setPLocalDateNextDay(LocalDate localDate, int days) {
        localDate = localDate.plusDays(days);
    }

    public static LocalDate fromString(String strDate) {
        return fromString(strDate, false);
    }

    public static LocalDate fromString(String strDate, boolean now) {
        try {
            if (strDate.isEmpty()) {
                return LocalDate.MIN;
            } else {
                return LocalDate.parse(strDate, P2DateConst.DT_FORMATTER_dd_MM_yyyy);
            }
        } catch (Exception ex) {
            if (now) {
                return LocalDate.now();
            } else {
                return LocalDate.MIN;
            }
        }
    }

    public static LocalDate fromStringR(String strDate) {
        return fromStringR(strDate, false);
    }

    public static LocalDate fromStringR(String strDate, boolean now) {
        try {
            if (strDate.isEmpty()) {
                return LocalDate.MIN;
            } else {
                return LocalDate.parse(strDate, P2DateConst.DT_FORMATTER_yyyy_MM_dd);
            }
        } catch (Exception ex) {
            if (now) {
                return LocalDate.now();
            } else {
                return LocalDate.MIN;
            }
        }
    }

    public static String toString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else if (localDate.isEqual(LocalDate.MIN)) {
            return "";
        } else {
            return localDate.format(P2DateConst.DT_FORMATTER_dd_MM_yyyy);
        }
    }

    public static String toStringR(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else if (localDate.isEqual(LocalDate.MIN)) {
            return "";
        } else {
            return localDate.format(P2DateConst.DT_FORMATTER_yyyy_MM_dd);
        }
    }

    public static String toStringYYYYMMdd(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else if (localDate.isEqual(LocalDate.MIN)) {
            return "";
        } else {
            return localDate.format(P2DateConst.DT_FORMATTER_yyyyMMdd);
        }
    }

    public static String getNowString() {
        return LocalDate.now().format(P2DateConst.DT_FORMATTER_dd_MM_yyyy);
    }

    public static String getNowStringR() {
        return LocalDate.now().format(P2DateConst.DT_FORMATTER_yyyy_MM_dd);
    }
}
