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

import java.time.LocalDate;

public class PLocalDateProperty extends SimpleObjectProperty<PLocalDate> {

    public PLocalDateProperty() {
        clearPLocalDate();
    }

    public PLocalDateProperty(PLocalDate pDate) {
        setValue(pDate);
    }

    public PLocalDateProperty(String date) {
        setPLocalDate(date);
    }

    public void setPLocalDate(LocalDate localDate) {
        PLocalDate pLocalDate = new PLocalDate();
        pLocalDate.setPDate(localDate);
        setValue(pLocalDate);
    }

    public void setPLocalDate(String strDate) {
        PLocalDate pLocalDate = new PLocalDate();
        pLocalDate.setPDate(strDate);
        setValue(pLocalDate);
    }

    public void clearPLocalDate() {
        PLocalDate pLocalDate = new PLocalDate();
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
