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


package de.p2tools.p2Lib.tools;

import de.p2tools.p2Lib.tools.log.PLog;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.ArrayList;
import java.util.List;

public class PDateUtils {
    private static final FastDateFormat FORMAT_yyyy = FastDateFormat.getInstance("yyyy");

    public static String getTodayStr() {
        return new PDate().getDateTime(PDate.FORMAT_dd_MM_yyyy);
    }

    public static String getAktYearStr() {
        return new PDate().getDateTime(FORMAT_yyyy);
    }

    public static int getAktYearInt() {
        return getYearIntFromString(getAktYearStr());
    }

    public static int getYearIntFromString(String year) {
        int ret;
        try {
            final long y = FORMAT_yyyy.parse(year).getTime();
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
}
