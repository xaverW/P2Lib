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

import de.p2tools.p2Lib.guiTools.PLDatePicker;
import de.p2tools.p2Lib.tools.date.PLDateFactory;
import javafx.scene.control.Control;

import java.time.LocalDate;

public abstract class Config_lDate extends Config {

    private LocalDate actValue;

    public Config_lDate(String key, String actValue) {
        super(key);
        this.actValue = PLDateFactory.fromString(actValue);
    }

    public Config_lDate(String key, String name, String actValue) {
        super(key, name);
        this.actValue = PLDateFactory.fromString(actValue);
    }

    public Config_lDate(String key, String name, LocalDate actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public LocalDate getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = ((LocalDate) act);
        setUsedValue(actValue);
    }

    public void setActValue(LocalDate act) {
        actValue = act;
        setUsedValue(actValue);
    }

    public void setActValue(String act) {
        actValue = PLDateFactory.fromString(act);
        setUsedValue(actValue);
    }

    @Override
    public String getActValueString() {
        return PLDateFactory.toString(actValue);
    }

    @Override
    public Control getControl() {
        PLDatePicker control = new PLDatePicker(actValue);
        return control;
    }

    public abstract void setUsedValue(LocalDate act);
}
