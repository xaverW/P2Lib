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

import de.p2tools.p2lib.guitools.PTextFieldRegEx;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class Config_stringProp extends Config {

    private final StringProperty actValue;

    public Config_stringProp(String key, StringProperty actValue) {
        super(key);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
    }

    public Config_stringProp(String key, String name, StringProperty actValue) {
        super(key, name);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
    }

    public Config_stringProp(String key, String name, String regEx, StringProperty actValue) {
        super(key, name, regEx);
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

    @Override
    public Control getControl() {
        Control control;

        if (regEx != null && !regEx.isEmpty()) {
            final PTextFieldRegEx txt = new PTextFieldRegEx(regEx);
            txt.setStringProperty(getProperty());
            control = txt;

        } else {
            final TextField txt = new TextField();
            txt.textProperty().bindBidirectional(getProperty());
            control = txt;
        }
        return control;
    }
}
