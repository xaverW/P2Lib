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
import de.p2tools.p2Lib.tools.date.PLDateTimeFactory;
import de.p2tools.p2Lib.tools.date.PLDateTimeProperty;
import javafx.scene.control.Control;

import java.time.LocalDateTime;

public class ConfigExtra_lDateTimeProp extends ConfigExtra {

    private PLDateTimeProperty actValue;

    public ConfigExtra_lDateTimeProp(String key, PLDateTimeProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public ConfigExtra_lDateTimeProp(String key, String name, PLDateTimeProperty actValue) {
        super(key, name);
        this.actValue = actValue;
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
        try {
            actValue.setValue(PLDateTimeFactory.setDate(act));
        } catch (Exception ex) {
            actValue.setValue(LocalDateTime.now());
        }
    }

    @Override
    public LocalDateTime getActValue() {
        return actValue.get();
    }

    @Override
    public String getActValueString() {
        final String ret = actValue.get() == null ? "" : actValue.get().format(PLDateTimeFactory.FORMAT_dd_MM_yyyy_HH_mm_ss);
        return ret;
    }

    @Override
    public PLDateTimeProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PLDatePicker control = new PLDatePicker(actValue.getValue().toLocalDate());
        return control;
    }
}
