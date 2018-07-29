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


package de.p2tools.p2Lib.guiTools.pMask;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;

public class PMaskerPane extends BorderPane {
    public PMaskerPane() {

        // todo erst mal ein Anfang :)

        getStyleClass().add("pMaskerPane");

        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pMask/pMaskerPane.css";
        getStylesheets().add(CSS_FILE);

        final ProgressIndicator pin = new ProgressIndicator();
        pin.setMaxSize(100, 100);
        this.setCenter(pin);

    }

}
