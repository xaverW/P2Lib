/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.guitools.PLDatePicker;
import de.p2tools.p2lib.tools.date.PLDateFactory;
import de.p2tools.p2lib.tools.date.PLDateProperty;
import javafx.scene.control.Control;

import java.time.LocalDate;

public class Config_lDateProp extends Config {

    private final PLDateProperty actValue;

    public Config_lDateProp(String key, PLDateProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_lDateProp(String key, String name, PLDateProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public PLDateProperty getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((LocalDate) act);
    }

    public void setActValue(LocalDate act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        actValue.setValue(PLDateFactory.fromString(act));
    }

    @Override
    public String getActValueString() {
        return PLDateFactory.toString(actValue.getValue());
    }

    @Override
    public PLDateProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PLDatePicker control = new PLDatePicker(actValue.getValue());
        return control;
    }
}
