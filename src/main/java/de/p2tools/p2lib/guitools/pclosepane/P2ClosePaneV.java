/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.guitools.pclosepane;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class P2ClosePaneV extends VBox {

    private final VBox vBoxAll = new VBox();

    public P2ClosePaneV(BooleanProperty booleanProperty, boolean scroll) {
        Button button = new Button();
        button.getStyleClass().add("close-button");
        button.setOnAction(a -> booleanProperty.setValue(false));

        HBox hBox = new HBox();
        hBox.getStyleClass().add("close-pane");
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(button);

        if (scroll) {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setContent(vBoxAll);

            getChildren().addAll(hBox, scrollPane);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
        } else {
            getChildren().addAll(hBox, vBoxAll);
            VBox.setVgrow(vBoxAll, Priority.ALWAYS);
        }
    }

    public VBox getVBoxAll() {
        return vBoxAll;
    }
}
