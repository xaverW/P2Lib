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


package de.p2tools.p2lib.guitools.ptable;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;


/**
 * das style der box ist: "checkbox-table"
 */
public class P2CellColor<S, T> extends TableCell<S, T> {

    public Callback<TableColumn<S, Color>, TableCell<S, Color>> cellFactory
            = (final TableColumn<S, Color> param) -> new TableCell<>() {

        @Override
        public void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
                setText(null);
                return;
            }
            Label lbl = new Label("      ");
            lbl.setBackground(new Background(
                    new BackgroundFill(item, CornerRadii.EMPTY, Insets.EMPTY)));
            setGraphic(lbl);
        }
    };
}
