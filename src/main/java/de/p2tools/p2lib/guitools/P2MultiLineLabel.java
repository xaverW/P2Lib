/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class P2MultiLineLabel extends Label {
    public P2MultiLineLabel() {
        super();
        init();
    }

    public P2MultiLineLabel(String text) {
        super(text);
        init();
    }

    private void init() {
        setMaxWidth(Double.MAX_VALUE);
        setWrapText(true);
        setPadding(new Insets(2, 4, 2, 4));
        setStyle("-fx-border-color: #c5c5c5;");
    }
}
