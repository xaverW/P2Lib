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

import de.p2tools.p2Lib.tools.log.PLog;
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PLDateFactory {

    public static final DateTimeFormatter FORMAT_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMAT_yyyy = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter FORMAT_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat F_FORMAT_yyyy = FastDateFormat.getInstance("yyyy");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");

    public static final DateTimeFormatter DT_FORMATTER_d_M_yyyy = DateTimeFormatter.ofPattern("d.M.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_d_MM_yyyy = DateTimeFormatter.ofPattern("d.MM.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_dd_M_yyyy = DateTimeFormatter.ofPattern("dd.M.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter DT_FORMATTER_EEE_MMM_dd_ = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    private PLDateFactory() {
    }

    //===================
//    public void setPLocalDate(LocalDate date) {
//        localDate = date;
//    }

//    public void setPLocalDate(PLocalDate date) {
//        localDate = date.getLocalDate();
//    }

    public static LocalDate getLocalDate(String strDate) {
        return LocalDate.parse(strDate, FORMAT_dd_MM_yyyy);
    }

    //
//    public void setPLocalDate(LocalDate localDate, PDate date) {
//        localDate = getLocalDate(date.toString());
//    }

    public static void setPLocalDateNow(LocalDate localDate) {
        localDate = LocalDate.now();
    }

    //====================
    public static boolean isEmpty(LocalDate localDate) {
        return localDate == null;
    }

    public static void clearPDate(LocalDate localDate) {
        localDate = null;
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
            PLog.errorLog(980214963, "Jahr: " + year);
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
            LocalDate localDate = LocalDate.parse(d, DT_FORMATTER_yyyy_MM_dd);
            if (localDate != null) {
                return localDate.format(DT_FORMATTER_dd_MM_yyyy);
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

    public static PDate getPDate(LocalDate localDate) {
        try {
            PDate pd = new PDate();
            pd.setPDate(getDateTime(localDate, FORMAT_dd_MM_yyyy));
            return pd;
        } catch (final Exception ex) {
        }

        return null;
    }

    public static String getYear(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_yyyy);
        }
    }

    public static int getActYearInt() {
        return LocalDate.now().getYear();
    }

    public static void setPLocalDateYesterday(LocalDate localDate) {
        setPLocalDateNow(localDate);
        localDate = localDate.minusDays(1);
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

    public static String toString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_dd_MM_yyyy);
        }
    }

    public static String toStringR(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(FORMAT_yyyy_MM_dd);
        }
    }
}
