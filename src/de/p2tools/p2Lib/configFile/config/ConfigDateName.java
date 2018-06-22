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

import de.p2tools.p2Lib.tools.PDate;

public class ConfigDateName extends ConfigName {

    private PDate pDate = null;

    public ConfigDateName(String key, String name, String actValue) {
        super(key, name);
        pDate = new PDate();
        pDate.setPDate(actValue);
    }

    public ConfigDateName(String key, String name, String actValue, boolean intern) {
        super(key, name, null, intern);
        pDate = new PDate();
        pDate.setPDate(actValue);
    }

    public ConfigDateName(String key, String name, PDate actPDate) {
        super(key, name);
        this.pDate = actPDate;
    }

    @Override
    public String getActValue() {
        return pDate.toString();
    }

    public PDate getpDate() {
        return pDate;
    }

    @Override
    public String getActValueString() {
        return pDate.toString();
    }

    @Override
    public void setActValue(String act) {
        pDate.setPDate(act);
    }

}
