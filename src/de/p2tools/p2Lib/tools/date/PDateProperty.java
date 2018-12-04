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
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public class PDateProperty extends SimpleObjectProperty<PDate> {


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
        PDate pDate = new PDate();
        pDate.setPDate(strDate, strTime);
        setValue(pDate);
    }

    public void clearPDate() {
        this.setValue(new PDate(0));
        return;
    }

    public void setPDateToday() {
        try {
            final String strToday = new PDate().getDateTime(PDateFactory.FORMAT_dd_MM_yyyy);
            final long lToday = PDateFactory.FORMAT_dd_MM_yyyy.parse(strToday).getTime();
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
            return PDateFactory.FORMAT_dd_MM_yyyy.format(this.getValue());
        }
    }

    public String toStringR() {
        if (this.getValue().getTime() == 0) {
            return PDateFactory.FORMAT_yyyy_MM_dd.format(new Date());
        } else {
            return PDateFactory.FORMAT_yyyy_MM_dd.format(this.getValue());
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
