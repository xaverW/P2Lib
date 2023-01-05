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


package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.tools.date.PDateFactory;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PLDatePicker extends DatePicker {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PLDatePicker() {
    }

    public PLDatePicker(LocalDate localDate) {
        super.setValue(localDate);
    }

    public String getDateStr() {
        String ret = "";
        LocalDate date = getValue();
        if (date != null) {
            ret = dateFormatter.format(date);
        }
        return ret;
    }

    public LocalDate getDateLDate() {
        return getValue();
    }

    public void clearDate() {
        this.setValue(LocalDate.MIN);
    }

    public void setDate(LocalDate pLocalDate) {
        this.setValue(pLocalDate);
    }

    public void setDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(LocalDate.MIN);
        } else {
            this.setValue(PDateFactory.getLocalDate(stringDate));
        }
    }
}
