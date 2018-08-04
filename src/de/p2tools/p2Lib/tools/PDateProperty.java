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
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public class PDateProperty extends SimpleObjectProperty<PDate> {
    public static final FastDateFormat FORMAT_HH_mm_ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat FORMAT_dd_MM_yyyyKomma__HH_mm = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm");
    public static final FastDateFormat FORMAT_dd_MM_yyyyKomma__HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm:ss");
    public static final FastDateFormat FORMAT_dd_MM_yyyy_HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss");


    public PDateProperty() {
        setValue(new PDate());
    }

    public PDateProperty(PDate pDate) {
        setValue(pDate);
    }

    public PDateProperty(long l) {
        setValue(new PDate(l));
    }

    public PDateProperty(String date) {
        setPDate(date);
    }

    public PDateProperty(String date, String time) {
        setPDate(date, time);
    }

    public void setPDate(String strDate) {
        setPDate(strDate, "");
    }

    public void setPDate(String strDate, String strTime) {
        if (strDate.isEmpty()) {
            this.setValue(new PDate(0));
            return;
        }

        PDate pDate = new PDate(0);
        try {
            if (strTime.isEmpty()) {
                pDate.setTime(FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                pDate.setTime(FORMAT_dd_MM_yyyyKomma__HH_mm.parse(strDate + strTime).getTime());
            }
            setValue(pDate);
            return;
        } catch (final Exception ex) {
        }

        try {
            if (strTime.isEmpty()) {
                pDate.setTime(FORMAT_dd_MM_yyyy.parse(strDate).getTime());
            } else {
                pDate.setTime(FORMAT_dd_MM_yyyyKomma__HH_mm_ss.parse(strDate + strTime).getTime());
            }
            setValue(pDate);
            return;
        } catch (final Exception ex) {
            PLog.errorLog(952103654, ex, new String[]{"Datum: " + strDate, "Zeit: " + strTime});
        }

        setValue(pDate);
    }

    public void clearPDate() {
        this.setValue(new PDate(0));
        return;

    }

    public void setPDateToday() {
        try {
            final String strToday = new PDate().getDateTime(FORMAT_dd_MM_yyyy);
            final long lToday = FORMAT_dd_MM_yyyy.parse(strToday).getTime();
            this.setValue(new PDate(lToday));
        } catch (final Exception ex) {
            this.setValue(new PDate(0));
            PLog.errorLog(915263630, ex);
        }
    }

    public void setPDateNow() {
        this.setValue(new PDate());
    }

    public String getDateTime(FastDateFormat format) {
        if (getValue().getTime() == 0) {
            return "";
        } else {
            return format.format(this.getValue());
        }
    }

    @Override
    public String toString() {
        if (this.getValue().getTime() == 0) {
            return "";
        } else {
            return FORMAT_dd_MM_yyyy.format(this.getValue());
        }
    }

    public String toStringR() {
        if (this.getValue().getTime() == 0) {
            return FORMAT_yyyy_MM_dd.format(new Date());
        } else {
            return FORMAT_yyyy_MM_dd.format(this.getValue());
        }
    }

    /**
     * Liefert den Betrag! der Zeitdifferenz zu jetzt.
     *
     * @return Differenz in Sekunden.
     */
    public long diffInSeconds() {
        return getValue().diffInSeconds();
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
