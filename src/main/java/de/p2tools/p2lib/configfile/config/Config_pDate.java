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

import de.p2tools.p2lib.tools.date.P2Date;

public abstract class Config_pDate extends Config {

    private P2Date actValue;

    public Config_pDate(String key, P2Date actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_pDate(String key, String name, P2Date actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public P2Date getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (P2Date) act;
        setUsedValue(actValue);
    }

    public void setActValue(P2Date act) {
        actValue = act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue = new P2Date(act);
        } catch (Exception ex) {
            actValue = new P2Date();
        }
        setUsedValue(actValue);
    }

    @Override
    public String getActValueString() {
        final String ret = getActValue() == null ? "" : getActValue().toString();
        return ret;
    }

    public abstract void setUsedValue(P2Date act);
}
