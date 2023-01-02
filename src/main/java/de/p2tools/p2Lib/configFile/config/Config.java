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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;

/**
 * this is the class for one configuration
 * for example: the configsData for a USER(name,age,size)
 * then it has 3 Config: one for the "name", "age", "size"
 * a config can store the info in a STRING or a PROPERTY
 */

public abstract class Config {

    final String key;
//    Object actValue;


    public Config() {
        this.key = "";
//        actValue = null;
    }

    public Config(String key) {
        this.key = key;
//        actValue = null;
    }

//    public Config(String key, Object actValue) {
//        this.key = key;
////        this.actValue = actValue;
//    }

    public Config(String key, boolean intern) {
        if (intern) {
            this.key = key.intern();
        } else {
            this.key = key;
        }
//        this.actValue = actValue;
    }

    public void setActValue(String act) {
//        actValue = act;
    }

    public void setActValue(Object act) {
//        actValue = act;
    }

    public Object getActValue() {
        return null;
    }

    public String getActValueString() {
        return "";
    }

    public String getKey() {
        return key;
    }

    //für die erweiterten
    public void resetValue() {
    }

    public ObservableValue getProperty() {
        return null;
    }

    public void setRegEx(String regEx) {
    }

    public String getRegEx() {
        return "";
    }

    public void setName(String name) {
    }

    public String getName() {
        return key;
    }

    public Control getControl() {
        return null;
    }
}
