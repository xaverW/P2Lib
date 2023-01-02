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

public class ConfigExtra_lDate extends ConfigExtra {

    private LocalDate actValue;

    public ConfigExtra_lDate(String key, String actValue) {
        super(key);
        this.actValue = PLDateFactory.getLocalDate(actValue);
    }

    public ConfigExtra_lDate(String key, String name, String actValue) {
        super(key, name);
        this.actValue = PLDateFactory.getLocalDate(actValue);
    }

    public ConfigExtra_lDate(String key, String name, LocalDate actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = ((LocalDate) act);
    }

    public void setActValue(LocalDate act) {
        actValue = act;
    }

    @Override
    public LocalDate getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return actValue.toString();
    }

    @Override
    public Control getControl() {
        PLDatePicker control = new PLDatePicker(actValue);
        return control;
    }
}
