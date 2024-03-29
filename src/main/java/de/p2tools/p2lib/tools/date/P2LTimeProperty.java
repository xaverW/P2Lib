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

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalTime;

public class P2LTimeProperty extends SimpleObjectProperty<LocalTime> {

    public P2LTimeProperty() {
        clearPLocalTime();
    }

    public P2LTimeProperty(LocalTime pDate) {
        setValue(pDate);
    }

    public P2LTimeProperty(String date) {
        setPLocalTime(date);
    }

    public void setPLocalTime(LocalTime localDate) {
        setValue(localDate);
    }

    public void setPLocalTime(String strDate) {
        LocalTime pLocalDate = P2LTimeFactory.fromString(strDate);
        setValue(pLocalDate);
    }

    public void clearPLocalTime() {
        LocalTime pLocalTime = LocalTime.now();
        this.setValue(pLocalTime);
        return;
    }

    @Override
    public String toString() {
        return P2LTimeFactory.toString(this.getValue());
    }
}
