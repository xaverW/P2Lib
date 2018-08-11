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


package de.p2tools.p2Lib.guiTools.pToggleSwitch;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PToggleSwitch extends HBox {

    private final CheckBox checkBox = new CheckBox();
    private final Label label = new Label();
    private boolean tglInFront = false;
    private boolean hGrow = true;

    public PToggleSwitch() {
        super();
        init();
    }

    public PToggleSwitch(String text) {
        super();
        label.setText(text);
        init();
    }

    public PToggleSwitch(String text, boolean tglInFront, boolean hGrow) {
        super();
        label.setText(text);
        this.tglInFront = tglInFront;
        this.hGrow = hGrow;
        init();
    }

    public final BooleanProperty selectedProperty() {
        return checkBox.selectedProperty();
    }

    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        return checkBox.isSelected();
    }

    public final void setText(String value) {
        label.setText(value);
    }

    public final String getText() {
        return label.getText();
    }

    public final void setTooltip(Tooltip value) {
        checkBox.setTooltip(value);
    }

    public final void setHGrow(boolean hGrow) {
        this.hGrow = hGrow;
        setCheckHgrow();
    }

    private void init() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15);
//        this.setPadding(new Insets(2));

        if (tglInFront) {
            label.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().addAll(checkBox, label);
        } else {
            label.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().addAll(label, checkBox);
        }
        setCheckHgrow();
        getStyleClass().add("pToggleSwitch");
        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pToggleSwitch/pToggleSwitch.css";
        getStylesheets().add(CSS_FILE);

    }

    private void setCheckHgrow() {
        if (hGrow) {
            HBox.setHgrow(label, Priority.ALWAYS);
        }
    }
}