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

import de.p2tools.p2Lib.guiTools.PTextFieldInteger;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Control;

public class Config_intProp extends Config {

    private IntegerProperty actValue;

    public Config_intProp(String key, IntegerProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_intProp(String key, String name, IntegerProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((Integer) act);
    }

    public void setActValue(int act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(Integer.parseInt(act));
        } catch (Exception ex) {
            actValue.setValue(0);
        }
    }

    @Override
    public Integer getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
    }

    @Override
    public IntegerProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PTextFieldInteger control = new PTextFieldInteger(getProperty());
        return control;
    }
}
