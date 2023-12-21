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
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.LocalDate;
import java.util.Date;

public class P2Date extends Date {

    public P2Date() {
        super();
    }

    public P2Date(Date date) {
        super(date.getTime());
    }

    public P2Date(long l) {
        super(l);
    }

    public P2Date(String date) {
        setPDate(date);
    }

    public P2Date(String date, String time) {
        setPDate(date, time);
    }

    public void setPDate(P2Date p2Date) {
        this.setTime(p2Date.getTime());
    }

    public void setPDate(String strDate) {
        setPDate(strDate, "");
    }

    public void setPDate(String strDate, String strTime, FastDateFormat dateF, FastDateFormat timeF) {
        if (strDate.isEmpty()) {
            setTime(0);
            return;
        }

        try {
            if (strTime.isEmpty()) {
                setTime(dateF.parse(strDate).getTime());
            } else {
                setTime(timeF.parse(strDate + strTime).getTime());
            }
            return;
        } catch (final Exception ex) {
        }

        setTime(0);
    }

    public void setPDate(String strDate, FastDateFormat dateF) {
        if (strDate.isEmpty()) {
            setTime(0);
            return;
        }

        try {
            setTime(dateF.parse(strDate).getTime());
            return;
        } catch (final Exception ex) {
        }

        setTime(0);
    }

    public void setPDate(String strDate, String strTime) {
        if (strDate.isEmpty()) {
            setTime(0);
            return;
        }

        try {
            if (strTime.isEmpty()) {
                setTime(P2DateConst.F_FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                setTime(P2DateConst.F_FORMAT_dd_MM_yyyyKomma___HH_mm.parse(strDate + strTime).getTime());
            }
            return;
        } catch (final Exception ex) {
        }

        try {
            if (strTime.isEmpty()) {
                setTime(P2DateConst.F_FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                setTime(P2DateConst.F_FORMAT_dd_MM_yyyyKomma___HH_mm_ss.parse(strDate + strTime).getTime());
            }
            return;
        } catch (final Exception ex) {
            PLog.errorLog(952103654, ex, new String[]{"Datum: " + strDate, "Zeit: " + strTime});
        }

        setTime(0);
    }

    public LocalDate getLocalDate() {
        String strDate = toString();
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }

        try {
            return P2LDateFactory.fromString(strDate);
        } catch (final Exception ex) {
        }
        return null;
    }

    public void clearPDate() {
        setTime(0);
    }

    public boolean isEmpty() {
        return getTime() == 0;
    }

    public void setPDateToday() {
        try {
            final String strToday = new P2Date().getDateTime(P2DateConst.F_FORMAT_dd_MM_yyyy);
            final long lToday = P2DateConst.F_FORMAT_dd_MM_yyyy.parse(strToday).getTime();
            setTime(lToday);
        } catch (final Exception ex) {
            setTime(0);
            PLog.errorLog(915263630, ex);
        }
    }

    public void setPDateNow() {
        try {
            setTime(new P2Date().getTime());
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

    public String get_yyyy_MM_dd() {
        if (this.getTime() == 0) {
            return P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date());
        } else {
            return P2DateConst.F_FORMAT_yyyy_MM_dd.format(this);
        }
    }

    public String get_dd_MM_yyyy() {
        if (this.getTime() == 0) {
            return P2DateConst.F_FORMAT_dd_MM_yyyy.format(new Date());
        } else {
            return P2DateConst.F_FORMAT_dd_MM_yyyy.format(this);
        }
    }

    /**
     * Liefert den Betrag! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Sekunden.
     */
    public int diffInSeconds() {
        final int ret = (int) (1L * (this.getTime() - new P2Date().getTime()) / 1000L);
        return Math.abs(ret);
    }

    /**
     * Liefert den BETRAG! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Minuten.
     */
    public int diffInMinutes() {
        return (diffInSeconds() / 60);
    }

    @Override
    public String toString() {
        return P2DateFactory.toString(this);
    }

    public String toStringR() {
        return P2DateFactory.toStringR(this);
    }
}
