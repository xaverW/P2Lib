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

import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class ConfigIntExtra extends ConfigExtra {

    private int actValue;

    public ConfigIntExtra(String key, String name, int actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public Integer getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return String.valueOf(getActValue());
    }

    public void setActValue(int act) {
        actValue = act;
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue = Integer.parseInt(act);
        } catch (Exception ex) {
            actValue = 0;
        }
    }

//    @Override
//    public IntegerProperty getProperty() {
//        return actValue;
//    }

    @Override
    public Control getControl() {
        Label control = new Label(actValue + "");
        return control;
    }
}
