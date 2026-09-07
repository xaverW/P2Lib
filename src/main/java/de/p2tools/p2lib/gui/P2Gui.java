/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.gui;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.ikonli.P2IconFactory;
import de.p2tools.p2lib.ikonli.P2IconShow;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class P2Gui extends VBox {

    public P2Gui() {
        pack();
    }

    private void pack() {
        setPadding(new Insets(P2LibConst.PADDING_VBOX));
        setSpacing(P2LibConst.SPACING_VBOX);

        final Button btnQuitt = new Button();
        btnQuitt.setTooltip(new Tooltip("Programm beenden"));
        btnQuitt.getStyleClass().add("switchOffBtn");
        btnQuitt.setOnAction(e -> ProgQuitFactory.quit());
        btnQuitt.setMaxWidth(Double.MAX_VALUE);
        btnQuitt.setGraphic(P2IconFactory.P2ICON.BTN_QUIT_GUI_B.getFontIcon());
        getChildren().add(btnQuitt);

        final Button btnIcon = new Button("Icon");
        btnIcon.setOnAction(a -> new P2IconShow(null));
        getChildren().add(btnIcon);
    }
}
