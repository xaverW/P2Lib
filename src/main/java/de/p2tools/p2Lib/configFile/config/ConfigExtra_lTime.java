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

import de.p2tools.p2Lib.guiTools.PTimePicker;
import de.p2tools.p2Lib.tools.date.PLTimeFactory;
import javafx.scene.control.Control;

import java.time.LocalTime;

public abstract class ConfigExtra_lTime extends ConfigExtra {

    private LocalTime actValue;

    public ConfigExtra_lTime(String key, String actValue) {
        super(key);
        this.actValue = LocalTime.now();
        this.actValue = PLTimeFactory.getPLocalTime(actValue);
    }

    public ConfigExtra_lTime(String key, String name, String actValue) {
        super(key, name);
        this.actValue = LocalTime.now();
        this.actValue = PLTimeFactory.getPLocalTime(actValue);
    }

    public ConfigExtra_lTime(String key, String name, LocalTime localTime) {
        super(key, name);
        this.actValue = localTime;
    }

    @Override
    public void setActValue(Object act) {
        this.actValue = (LocalTime) act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        this.actValue = PLTimeFactory.getPLocalTime(act);
        setUsedValue(actValue);
    }

    @Override
    public LocalTime getActValue() {
        return this.actValue;
    }

    @Override
    public String getActValueString() {
        return PLTimeFactory.toString(actValue);
    }

    @Override
    public Control getControl() {
        PTimePicker control = new PTimePicker();
        return control;
    }

    public abstract void setUsedValue(LocalTime act);
}
