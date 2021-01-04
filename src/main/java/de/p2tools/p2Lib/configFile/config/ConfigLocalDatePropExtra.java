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

import de.p2tools.p2Lib.guiTools.PDatePicker;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.scene.control.Control;

public class ConfigLocalDatePropExtra extends ConfigExtra {

    private PLocalDateProperty actValue;

    public ConfigLocalDatePropExtra(String key, String name, PLocalDateProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    public PLocalDate getPDate() {
        return actValue.getValue();
    }

    @Override
    public PLocalDate getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        //        return getActValue().toString();
        final String ret = getActValue() == null ? "" : getActValue().toString();
        return ret;
    }

    public void setActValue(PLocalDate act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(new PLocalDate(act));
        } catch (Exception ex) {
            actValue.setValue(new PLocalDate());
        }
    }

    @Override
    public PLocalDateProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PDatePicker control = new PDatePicker(getPDate());
        return control;
    }
}
