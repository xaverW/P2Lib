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

import de.p2tools.p2Lib.tools.date.PLDateTimeFactory;

import java.time.LocalDateTime;

public abstract class Config_lDateTime extends Config {

    private LocalDateTime actValue;

    public Config_lDateTime(String key, String actValue) {
        super(key);
        this.actValue = PLDateTimeFactory.setDate(actValue);
    }

    public Config_lDateTime(String key, String actValue, boolean intern) {
        super(key, intern);
        this.actValue = PLDateTimeFactory.setDate(actValue);
    }

    public Config_lDateTime(String key, LocalDateTime pLocalDateTime) {
        super(key);
        this.actValue = pLocalDateTime;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (LocalDateTime) act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        this.actValue = PLDateTimeFactory.setDate(act);
    }

    @Override
    public LocalDateTime getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return actValue.toString();
    }

    public abstract void setUsedValue(LocalDateTime value);
}
