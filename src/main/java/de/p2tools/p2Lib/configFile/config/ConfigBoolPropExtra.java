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


package de.p2tools.p2Lib.configFile.config;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;

public class ConfigBoolPropExtra extends ConfigExtra {

    private BooleanProperty actValue;

    public ConfigBoolPropExtra(String key, String name, BooleanProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public Boolean getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(Boolean.valueOf(act));
        } catch (Exception ex) {
        }
    }

    @Override
    public BooleanProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        CheckBox control = new CheckBox();
        control.selectedProperty().bindBidirectional(getProperty());
        return control;
    }
}
