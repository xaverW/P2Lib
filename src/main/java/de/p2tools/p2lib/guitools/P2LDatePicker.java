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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.date.P2DateFactory;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class P2LDatePicker extends DatePicker {

    public P2LDatePicker() {
    }

    public P2LDatePicker(LocalDate localDate) {
        super.setValue(localDate);
    }

    public String getDateStr() {
        String ret = "";
        LocalDate date = getValue();
        if (date != null && !date.equals(LocalDate.MIN)) {
            ret = P2DateConst.DT_FORMATTER_dd_MM_yyyy.format(date);
        }
        return ret;
    }

    public LocalDate getDateLDate() {
        return getValue();
    }

    public void clearDate() {
        this.setValue(null);
    }

    public void setToday() {
        this.setValue(LocalDate.now());
    }

    public void setDate(LocalDate pLocalDate) {
        this.setValue(pLocalDate);
    }

    public void setDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(null);
        } else {
            this.setValue(P2DateFactory.getLocalDate(stringDate));
        }
    }
}
