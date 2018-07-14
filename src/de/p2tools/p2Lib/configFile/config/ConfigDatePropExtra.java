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

import de.p2tools.p2Lib.guiTools.PDatePicker;
import de.p2tools.p2Lib.tools.PDate;
import de.p2tools.p2Lib.tools.PDateProperty;
import javafx.scene.control.Control;

public class ConfigDatePropExtra extends ConfigExtra {

    private PDateProperty actValue;

    public ConfigDatePropExtra(String key, String name, PDateProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    public PDate getPDate() {
        return actValue.getValue();
    }

    @Override
    public PDate getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return getActValue().toString();
    }

    public void setActValue(PDate act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(new PDate(act));
        } catch (Exception ex) {
            actValue.setValue(new PDate());
        }
    }

    @Override
    public PDateProperty getProperty() {
        return actValue;
    }

    @Override
    public Control getControl() {
        PDatePicker control = new PDatePicker(getPDate());
        return control;
    }
}
