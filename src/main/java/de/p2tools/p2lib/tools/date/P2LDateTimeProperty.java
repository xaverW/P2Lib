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

import java.time.LocalDateTime;

public class P2LDateTimeProperty extends SimpleObjectProperty<LocalDateTime> {

    public P2LDateTimeProperty() {
        setValue(LocalDateTime.MIN);
    }

    public P2LDateTimeProperty(LocalDateTime pDate) {
        setValue(pDate);
    }

    public P2LDateTimeProperty(String date) {
        setValue(P2LDateTimeFactory.fromString(date));
    }

    public void clearPLocalDate() {
        this.setValue(LocalDateTime.MIN);
        return;
    }

    @Override
    public String toString() {
        return P2LDateTimeFactory.toString(this.getValue());
    }
}
