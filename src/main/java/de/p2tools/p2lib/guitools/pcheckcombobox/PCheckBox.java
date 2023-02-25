/*
 * P2Tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.guitools.pcheckcombobox;

import javafx.scene.control.CheckBox;

public class PCheckBox extends CheckBox {

    private String shortText = "";

    public PCheckBox(String text) {
        super(text);
    }

    String getShortText() {
        return shortText;
    }

    void setShortText(String shortText) {
        this.shortText = shortText;
    }

    String getResText() {
        if (shortText.isEmpty()) {
            return super.getText();
        } else {
            return shortText;
        }
    }
}