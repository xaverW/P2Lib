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

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalTime;

public class PLocalTimeProperty extends SimpleObjectProperty<PLocalTime> {

    public PLocalTimeProperty() {
        clearPLocalTime();
    }

    public PLocalTimeProperty(PLocalTime pDate) {
        setValue(pDate);
    }

    public PLocalTimeProperty(String date) {
        setPLocalTime(date);
    }

    public void setPLocalTime(LocalTime localDate) {
        PLocalTime pLocalDate = new PLocalTime();
        pLocalDate.setPDate(localDate);
        setValue(pLocalDate);
    }

    public void setPLocalTime(String strDate) {
        PLocalTime pLocalDate = new PLocalTime();
        pLocalDate.setPDate(strDate);
        setValue(pLocalDate);
    }

    public void clearPLocalTime() {
        PLocalTime pLocalDate = new PLocalTime();
        pLocalDate.clearPDate();
        this.setValue(pLocalDate);
        return;
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }

    public String toStringR() {
        return this.getValue().toStringR();
    }
}
