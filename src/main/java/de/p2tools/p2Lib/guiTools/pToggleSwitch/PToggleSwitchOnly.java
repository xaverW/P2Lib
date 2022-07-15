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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class PToggleSwitchOnly extends HBox {

    private final CheckBox checkBox = new CheckBox();
    private boolean tglInFront = false;
    private boolean hGrow = true;

    public PToggleSwitchOnly() {
        super();
        init();
    }

    public final BooleanProperty selectedProperty() {
        return checkBox.selectedProperty();
    }

    public final BooleanProperty checkBoxDisableProperty() {
        return checkBox.disableProperty();
    }

    public void setCheckBoxDisable(boolean value) {
        checkBox.setDisable(value);
    }

    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        return checkBox.isSelected();
    }

    public final BooleanProperty indeterminateProperty() {
        return checkBox.indeterminateProperty();
    }

    public final void setIndeterminate(boolean value) {
        checkBox.setIndeterminate(value);
    }

    public final boolean isIndeterminate() {
        return checkBox.isIndeterminate();
    }

    public final void setAllowIndeterminate(boolean value) {
        checkBox.setAllowIndeterminate(value);
    }

    public final void setTooltip(Tooltip value) {
        checkBox.setTooltip(value);
    }

    private void init() {
        this.getChildren().addAll(checkBox);
        getStyleClass().add("pToggleSwitch");
    }
}