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

import de.p2tools.p2Lib.tools.date.PLTimeFactory;
import de.p2tools.p2Lib.tools.date.PLTimeProperty;

import java.time.LocalTime;

public class ConfigExtra_lTimeProp extends ConfigExtra {

    private PLTimeProperty actValue;

    public ConfigExtra_lTimeProp(String key, PLTimeProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public ConfigExtra_lTimeProp(String key, String name, PLTimeProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((LocalTime) act);
    }

    public void setActValue(LocalTime act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(PLTimeFactory.getPLocalTime(act));
        } catch (Exception ex) {
            actValue.setValue(LocalTime.now());
        }
    }

    @Override
    public LocalTime getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        //        return getActValue().toString();
        final String ret = actValue.getValue() == null ? "" : PLTimeFactory.toString(actValue.getValue());
        return ret;
    }

    @Override
    public PLTimeProperty getProperty() {
        return actValue;
    }

//    @Override
//    public Control getControl() {
//        PLDatePicker control = new PLDatePicker(getPDate());
//        return control;
//    }
}
