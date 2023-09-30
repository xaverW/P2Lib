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

import de.p2tools.p2lib.guitools.P2LDatePicker;
import de.p2tools.p2lib.tools.date.PLDateTimeFactory;
import de.p2tools.p2lib.tools.date.PLDateTimeProperty;
import javafx.scene.control.Control;

import java.time.LocalDateTime;

public class Config_lDateTimeProp extends Config {

    private final PLDateTimeProperty actValue;

    public Config_lDateTimeProp(String key, PLDateTimeProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_lDateTimeProp(String key, String name, PLDateTimeProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public LocalDateTime getActValue() {
        return actValue.get();
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((LocalDateTime) act);
    }

    public void setActValue(LocalDateTime act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        actValue.setValue(PLDateTimeFactory.fromString(act));
    }

    @Override
    public String getActValueString() {
        return PLDateTimeFactory.toString(actValue.getValue());
    }

    @Override
    public PLDateTimeProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        P2LDatePicker control = new P2LDatePicker(actValue.getValue().toLocalDate());
        return control;
    }
}
