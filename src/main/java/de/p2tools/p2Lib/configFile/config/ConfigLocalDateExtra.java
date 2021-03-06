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
import de.p2tools.p2Lib.tools.date.PLocalDate;
import javafx.scene.control.Control;

public class ConfigLocalDateExtra extends ConfigExtra {

    private PLocalDate pLocalDate = null;

    public ConfigLocalDateExtra(String key, String name, String actValue) {
        super(key, name);
        pLocalDate = new PLocalDate();
        pLocalDate.setPLocalDate(actValue);
    }

    public ConfigLocalDateExtra(String key, String name, PLocalDate actPDate) {
        super(key, name);
        this.pLocalDate = actPDate;
    }

    public PLocalDate getPDate() {
        return pLocalDate;
    }

    @Override
    public String getActValue() {
        return pLocalDate.toString();
    }

    @Override
    public String getActValueString() {
        return pLocalDate.toString();
    }

    @Override
    public void setActValue(String act) {
        pLocalDate.setPLocalDate(act);
    }

    @Override
    public Control getControl() {
        PDatePicker control = new PDatePicker(getPDate());
        return control;
    }
}
