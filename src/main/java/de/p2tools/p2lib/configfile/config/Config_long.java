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

public abstract class Config_long extends Config {

    private Long actValue;

    public Config_long(String key, Long actValue) {
        super(key);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (Long) act;
        setUsedValue(actValue);
    }

    public void setActValue(long act) {
        actValue = act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue = Long.parseLong(act);
        } catch (Exception ex) {
            actValue = 0L;
        }
        setUsedValue(actValue);
    }

    @Override
    public Long getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return String.valueOf(actValue);
    }

    public abstract void setUsedValue(Long value);
}
