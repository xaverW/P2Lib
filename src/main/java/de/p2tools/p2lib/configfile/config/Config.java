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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * this is the class for one configuration
 * for example: the configsData for a USER(name,age,size)
 * then it has 3 Config: one for the "name", "age", "size"
 * a config can store the info in a STRING or a PROPERTY
 */

public abstract class Config {

    final String key;
    String name;
    String regEx = ""; // damit kann eine Eingabe als *falsch* markiert werden z.B. PLT muss 5 Zahlen haben

    public Config() {
        this.key = "";
        this.name = "";
    }

    public Config(String key) {
        this.key = key;
        this.name = key;
    }

    public Config(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Config(String key, boolean intern) {
        if (intern) {
            this.key = key.intern();
        } else {
            this.key = key;
        }
    }

    public Config(String key, String name, String regEx) {
        this.key = key;
        this.name = name;
        this.regEx = regEx;
    }

    public Object getActValue() {
        return null;
    }

    public void setActValue(Object act) {
    }

    public void setActValue(String act) {
    }

    public String getActValueString() {
        return "";
    }

    public String getKey() {
        return key;
    }

    public void resetValue() {
    }

    public ObservableValue getProperty() {
        return null;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Control getControl() {
        TextField control = new TextField(getProperty().getValue().toString());
        return control;
    }
}
