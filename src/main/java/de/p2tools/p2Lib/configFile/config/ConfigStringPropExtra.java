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

import de.p2tools.p2Lib.guiTools.PTextFieldRegEx;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class ConfigStringPropExtra extends ConfigExtra {

    private StringProperty actValue;

    public ConfigStringPropExtra(String key, String name, StringProperty actValue) {
        super(key, name);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
    }

    public ConfigStringPropExtra(String key, String name, String regEx, StringProperty actValue) {
        super(key, name, regEx, null);
        this.actValue = actValue;
        if (actValue.getValue() == null) {
            actValue.setValue("");
        }
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
    public void setActValue(String act) {
        try {
            actValue.setValue(act);
        } catch (Exception ex) {
            PLog.errorLog(102540698, act);
        }
    }

    @Override
    public void setActValue(Object act) {
        try {
            actValue.setValue(act.toString());
        } catch (Exception ex) {
            PLog.errorLog(907894213, ex);
        }
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
