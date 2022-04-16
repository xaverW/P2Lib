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

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class ConfigStringExtra extends ConfigExtra {

    private String[] actValue;
    private int no;

    public ConfigStringExtra(String key, String name, String[] actValue, int no) {
        super(key, name);
        this.no = no;
        this.actValue = actValue;
        if (this.actValue[no] == null) {
            this.actValue[no] = "";
        }
    }

    @Override
    public String getActValue() {
        return actValue[no];
    }

    @Override
    public String getActValueString() {
        return getActValue();
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue[no] = act;
        } catch (Exception ex) {
            PLog.errorLog(956230142, ex);
        }
    }

    @Override
    public Control getControl() {
        Control control;
        final Label txt = new Label();
        txt.setText(actValue[no]);
        control = txt;

        return control;
    }
}
