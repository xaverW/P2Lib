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
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2lib.tools.date.P2LDateTimeProperty;
import javafx.scene.control.Control;

import java.time.LocalDateTime;

public class Config_lDateTimeProp extends Config {

    private final P2LDateTimeProperty actValue;

    public Config_lDateTimeProp(String key, P2LDateTimeProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_lDateTimeProp(String key, String name, P2LDateTimeProperty actValue) {
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
        actValue.setValue(P2LDateTimeFactory.fromString(act));
    }

    @Override
    public String getActValueString() {
        return P2LDateTimeFactory.toString(actValue.getValue());
    }

    @Override
    public P2LDateTimeProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        P2LDatePicker control = new P2LDatePicker(actValue.getValue().toLocalDate());
        return control;
    }
}
