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

import de.p2tools.p2lib.tools.date.PLTimeFactory;
import de.p2tools.p2lib.tools.date.PLTimeProperty;

import java.time.LocalTime;

public class Config_lTimeProp extends Config {

    private final PLTimeProperty actValue;

    public Config_lTimeProp(String key, PLTimeProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_lTimeProp(String key, String name, PLTimeProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public LocalTime getActValue() {
        return actValue.getValue();
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
        PLTimeFactory.fromString(act);
    }

    @Override
    public String getActValueString() {
        return PLTimeFactory.toString(actValue.getValue());
    }

    @Override
    public PLTimeProperty getProperty() {
        return actValue;
    }

//   @Override
//   public Control getControl() {
//       PLDatePicker control = new PLDatePicker(getPDate());
//       return control;
//   }
}
