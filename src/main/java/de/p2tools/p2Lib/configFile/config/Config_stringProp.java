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

import javafx.beans.property.StringProperty;

public class Config_stringProp extends Config {

    private StringProperty actValue;

    public Config_stringProp(String key, StringProperty actValue) {
        super(key);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
    }

    public Config_stringProp(String key, StringProperty actValue, boolean intern) {
        super(key, intern);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((String) act);
    }

    @Override
    public void setActValue(String act) {
        actValue.setValue(act);
    }

    @Override
    public String getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return getActValue();
    }


    @Override
    public StringProperty getProperty() {
        return actValue;
    }
}
