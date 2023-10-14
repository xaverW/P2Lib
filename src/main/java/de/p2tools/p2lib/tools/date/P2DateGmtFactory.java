/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.SimpleTimeZone;

public class P2DateGmtFactory {
    private P2DateGmtFactory() {
    }

    public static String getDateTimeGmt() {
        SimpleDateFormat gmtDateFormat = new SimpleDateFormat(DateFactory.STR__dd_MM_yyyy___HH__mm__ss);
        gmtDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return gmtDateFormat.format(new Date());
    }

    public static String getLocalDateTimeStringFromGmt(String strDate) {
        LocalDateTime localDateTime = getLocalDateTimeFromGmt(strDate);
        return PLDateTimeFactory.toString(localDateTime);
    }

    public static LocalDateTime getLocalDateTimeFromGmt(String strDate) {
        if (strDate == null || strDate.isEmpty()) {
            return LocalDateTime.now();
        }

        try {
            final SimpleDateFormat sdfUtc = new SimpleDateFormat(DateFactory.STR__dd_MM_yyyy___HH__mm__ss);
            sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

            Date filmDate = null;
            try {
                filmDate = sdfUtc.parse(strDate);
            } catch (final ParseException ignored) {
            }
            if (filmDate == null) {
                return LocalDateTime.now();
            } else {
                return filmDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            }
//            DateTimeFormatter dateTimeFormatter =
//                    DateTimeFormatter.ofPattern(DateFactory.F_FORMAT_dd_MM_yyyy___HH__mm__ss.toString(), Locale.ENGLISH);
//            return LocalDateTime.parse(strDate, dateTimeFormatter);
        } catch (final Exception ex) {
            return LocalDateTime.now();
        }
    }


}
