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

import java.util.Date;

public class PDate extends Date {
    public static final FastDateFormat FORMAT_HH_mm_ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat FORMAT_yyyy = FastDateFormat.getInstance("yyyy");
    public static final FastDateFormat FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat FORMAT_dd_MM_yyyyKomma__HH_mm = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm");
    public static final FastDateFormat FORMAT_dd_MM_yyyyKomma__HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm:ss");
    public static final FastDateFormat FORMAT_dd_MM_yyyy_HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss");


    public PDate() {
        super();
    }

    public PDate(long l) {
        super(l);
    }

    public PDate(String date) {
        setPDate(date);
    }

    public PDate(String date, String time) {
        setPDate(date, time);
    }

    public void setPDate(String strDate) {
        setPDate(strDate, "");
    }

    public void setPDate(String strDate, String strTime) {
        if (strDate.isEmpty()) {
            setTime(0);
            return;
        }

        try {
            if (strTime.isEmpty()) {
                setTime(FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                setTime(FORMAT_dd_MM_yyyyKomma__HH_mm.parse(strDate + strTime).getTime());
            }
            return;
        } catch (final Exception ex) {
        }

        try {
            if (strTime.isEmpty()) {
                setTime(FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                setTime(FORMAT_dd_MM_yyyyKomma__HH_mm_ss.parse(strDate + strTime).getTime());
            }
            return;
        } catch (final Exception ex) {
            PLog.errorLog(952103654, ex, new String[]{"Datum: " + strDate, "Zeit: " + strTime});
        }

        setTime(0);
    }

    public void clearPDate() {
        setTime(0);
    }

    public boolean isEmpty() {
        return getTime() == 0;
    }

    public void setPDateToday() {
        try {
            final String strToday = new PDate().getDateTime(FORMAT_dd_MM_yyyy);
            final long lToday = FORMAT_dd_MM_yyyy.parse(strToday).getTime();
            setTime(lToday);
        } catch (final Exception ex) {
            setTime(0);
            PLog.errorLog(915263630, ex);
        }
    }

    public void setPDateNow() {
        try {
            setTime(new PDate().getTime());
        } catch (final Exception ex) {
            setTime(0);
            PLog.errorLog(915263630, ex);
        }
    }

    public String getDateTime(FastDateFormat format) {
        if (this.getTime() == 0) {
            return "";
        } else {
            return format.format(this);
        }
    }

    @Override
    public String toString() {
        if (this.getTime() == 0) {
            return "";
        } else {
            return FORMAT_dd_MM_yyyy.format(this);
        }
    }

    public String toStringR() {
        if (this.getTime() == 0) {
            return FORMAT_yyyy_MM_dd.format(new Date());
        } else {
            return FORMAT_yyyy_MM_dd.format(this);
        }
    }

    /**
     * Liefert den Betrag! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Sekunden.
     */
    public long diffInSeconds() {
        final long ret = (int) (1L * (this.getTime() - new PDate().getTime()) / 1000L);
        return Math.abs(ret);
    }

    /**
     * Liefert den BETRAG! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Minuten.
     */
    public long diffInMinutes() {
        return (diffInSeconds() / 60);
    }
}
