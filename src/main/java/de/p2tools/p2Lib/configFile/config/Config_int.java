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

public abstract class Config_int extends Config {

    private Integer actValue;

    public Config_int(String key, int actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_int(String key, int actValue, boolean intern) {
        super(key, intern);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (Integer) act;
        setUsedValue(actValue);
    }

    public void setActValue(int act) {
        actValue = act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue = Integer.parseInt(act);
        } catch (Exception ex) {
            actValue = 0;
        }
        setUsedValue(actValue);
    }

    @Override
    public Integer getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return String.valueOf(actValue);
    }

    public abstract void setUsedValue(Integer value);
}
