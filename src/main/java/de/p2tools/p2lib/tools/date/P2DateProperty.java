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
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.time.FastDateFormat;

public class P2DateProperty extends SimpleObjectProperty<P2Date> {

    public P2DateProperty() {
        setValue(new P2Date());
    }

    public P2DateProperty(P2Date p2Date) {
        setValue(p2Date);
    }

    public P2DateProperty(long l) {
        setValue(new P2Date(l));
    }

    public P2DateProperty(String date) {
        setPDate(date);
    }

    public P2DateProperty(String date, String time) {
        setPDate(date, time);
    }

    public void setPDate(String strDate) {
        setPDate(strDate, "");
    }

    public void setPDate(String strDate, String strTime) {
        P2Date p2Date = new P2Date();
        p2Date.setPDate(strDate, strTime);
        setValue(p2Date);
    }

    public void clearPDate() {
        this.setValue(new P2Date(0));
        return;
    }

    public void setPDateToday() {
        try {
            final String strToday = new P2Date().getDateTime(P2DateConst.F_FORMAT_dd_MM_yyyy);
            final long lToday = P2DateConst.F_FORMAT_dd_MM_yyyy.parse(strToday).getTime();
            this.setValue(new P2Date(lToday));
        } catch (final Exception ex) {
            this.setValue(new P2Date(0));
            PLog.errorLog(915263630, ex);
        }
    }

    public void setPDateNow() {
        this.setValue(new P2Date());
    }

    public String getDateTime(FastDateFormat format) {
        if (getValue().getTime() == 0) {
            return "";
        } else {
            return format.format(this.getValue());
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

    @Override
    public String toString() {
        return P2DateFactory.toString(getValue());
    }
}
