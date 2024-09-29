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


package de.p2tools.p2lib.guitools.pmask;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class P2MaskerPaneMin extends BorderPane {

    private final VBox vBoxCont = new VBox();

    public P2MaskerPaneMin() {
        getStyleClass().add("p2MaskerPane");
        setVBoxCont();
        this.setCenter(vBoxCont);
        this.setVisible(false);
    }

    public BooleanProperty getVisibleProperty() {
        return this.visibleProperty();
    }

    public void bindVisibleProperty(BooleanProperty visible) {
        this.visibleProperty().bind(visible);
    }

    public void setMaskerVisible(boolean maskerVisible) {
        Platform.runLater(() -> {
            this.setVisible(maskerVisible);
        });
    }

    private void setVBoxCont() {
        vBoxCont.getChildren().clear();
        vBoxCont.setMinWidth(500);
        vBoxCont.setMaxWidth(500);
        vBoxCont.setSpacing(20);
        vBoxCont.setPadding(new Insets(20));
        vBoxCont.setAlignment(Pos.CENTER);
    }
}
