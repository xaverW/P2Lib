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


package de.p2tools.p2lib.configfile.config;

import de.p2tools.p2lib.guitools.P2TextFieldDouble;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Control;

public class Config_doubleProp extends Config {

    private DoubleProperty actValue;

    public Config_doubleProp(String key, DoubleProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_doubleProp(String key, String name, DoubleProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((Double) act);
    }

    public void setActValue(double act) {
        actValue.setValue(act);
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
    public Double getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
    }

    @Override
    public DoubleProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        P2TextFieldDouble control = new P2TextFieldDouble(getProperty());
        return control;
    }
}
