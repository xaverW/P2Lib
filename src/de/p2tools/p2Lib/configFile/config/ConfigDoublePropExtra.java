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

import de.p2tools.p2Lib.guiTools.PTextFieldDouble;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Control;

public class ConfigDoublePropExtra extends ConfigExtra {

    private DoubleProperty actValue;

    public ConfigDoublePropExtra(String key, String name, DoubleProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }


    @Override
    public Double getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(Double.parseDouble(act));
        } catch (Exception ex) {
            actValue.setValue(0.0);
        }
    }

    @Override
    public DoubleProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PTextFieldDouble control = new PTextFieldDouble(getProperty());
        return control;
    }
}
