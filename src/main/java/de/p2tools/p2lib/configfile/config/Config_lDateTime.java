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
import javafx.scene.control.Control;

import java.time.LocalDateTime;

public abstract class Config_lDateTime extends Config {

    private LocalDateTime actValue;

    public Config_lDateTime(String key, String actValue) {
        super(key);
        this.actValue = PLDateTimeFactory.setDate(actValue, "");
    }

    public Config_lDateTime(String key, String name, String actValue) {
        super(key, name);
        this.actValue = PLDateTimeFactory.setDate(actValue, "");
    }

    public Config_lDateTime(String key, String name, LocalDateTime localDateTime) {
        super(key, name);
        this.actValue = localDateTime;
    }

    @Override
    public LocalDateTime getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        this.actValue = (LocalDateTime) act;
        setUsedValue(actValue);
    }

    public void setActValue(LocalDateTime act) {
        this.actValue = act;
        setUsedValue(actValue);
    }

    public void setActValue(String act) {
        actValue = PLDateTimeFactory.fromString(act);
        setUsedValue(actValue);
    }

    @Override
    public String getActValueString() {
        return PLDateTimeFactory.toString(actValue);
    }

    @Override
    public Control getControl() {
        P2LDatePicker control = new P2LDatePicker(actValue.toLocalDate());
        return control;
    }

    public abstract void setUsedValue(LocalDateTime act);
}
