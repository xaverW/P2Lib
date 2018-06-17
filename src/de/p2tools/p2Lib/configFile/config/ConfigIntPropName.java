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

import javafx.beans.property.IntegerProperty;

public class ConfigIntPropName extends ConfigName {

    private IntegerProperty actValue;

    public ConfigIntPropName(String key, String name, IntegerProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }


    public ConfigIntPropName(String key, String name, IntegerProperty actValue, boolean intern) {
        super(key, name, null, intern);
        this.actValue = actValue;
    }


    @Override
    public Integer getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
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
    public IntegerProperty getProperty() {
        return actValue;
    }

}
