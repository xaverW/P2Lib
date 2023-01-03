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

import de.p2tools.p2Lib.configFile.pData.PData;

/**
 * its a pseudo CONFIG, it contains a CONFIGDATA, also
 * a array of config
 */
public abstract class Config_pData extends Config {

    private PData actValue;

    public Config_pData(String key, PData pData) {
        super(key);
        this.actValue = pData;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (PData) act;
        setUsedValue(actValue);
    }

    public void setActValue(PData act) {
        actValue = act;
        setUsedValue(actValue);
    }

    @Override
    public PData getActValue() {
        return actValue;
    }

    public abstract void setUsedValue(PData value);
}
