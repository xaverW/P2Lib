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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDateFactory {
    public static final FastDateFormat F_FORMAT_HH_mm_ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyHH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyyHH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyy_HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyKomma__HH_mm = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyKomma__HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm:ss");
    public static final FastDateFormat F_FORMAT_yyyy = FastDateFormat.getInstance("yyyy");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat F_FORMAT_yyyyMMdd = FastDateFormat.getInstance("yyyyMMdd");

    public static final DateTimeFormatter DT_FORMATTER_d_M_yyyy = DateTimeFormatter.ofPattern("d.M.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_d_MM_yyyy = DateTimeFormatter.ofPattern("d.MM.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_dd_M_yyyy = DateTimeFormatter.ofPattern("dd.M.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");


    public static String getTodayStr() {
        return new PDate().getDateTime(F_FORMAT_dd_MM_yyyy);
    }

    public static String getTodayInverseStr() {
        return new PDate().getDateTime(F_FORMAT_yyyy_MM_dd);
    }

    public static PDate getToday() {
        PDate pDate = new PDate();
        pDate.setPDateToday();
        return pDate;
    }

    public static PDate getYesterday() {
        PLocalDate pDate = new PLocalDate();
        pDate.setPLocalDateYesterdy();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return new PDate(Date.from(pDate.getLocalDate().atStartOfDay(defaultZoneId).toInstant()));
    }

    public static String getAktYearStr() {
        return LocalDate.now().getYear() + "";
    }

    public static int getAktYearInt() {
        return LocalDate.now().getYear();
    }

    public static int getYearIntFromString(String year) {
        int ret;
        try {
            final long y = F_FORMAT_yyyy.parse(year).getTime();
            ret = Integer.parseInt(year);
        } catch (Exception ex) {
            ret = 0;
            PLog.errorLog(621212154, "Jahr: " + year);
        }

        return ret;
    }

    public static List<String> getYearListSince(String year) {
        List<String> list = new ArrayList<>();
        int aktYear = getAktYearInt();
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

        int aktYear = getAktYearInt();
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
            return LocalDate.parse(strDate, DT_FORMATTER_dd_MM_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DT_FORMATTER_d_MM_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DT_FORMATTER_dd_M_yyyy);
        } catch (final Exception ex) {
        }
        try {
            return LocalDate.parse(strDate, DT_FORMATTER_d_M_yyyy);
        } catch (final Exception ex) {
        }

        return null;
    }

    public static String getDate_yMd(String d) {
        try {
            LocalDate pLocalDate = LocalDate.parse(d, PDateFactory.DT_FORMATTER_yyyy_MM_dd);
            if (pLocalDate != null) {
                return pLocalDate.format(PDateFactory.DT_FORMATTER_dd_MM_yyyy);
            }
        } catch (final Exception ex) {
        }

        return d;
    }
}
