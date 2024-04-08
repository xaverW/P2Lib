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

import de.p2tools.p2lib.configfile.pdata.P2Data;

/**
 * its a pseudo CONFIG, it contains a CONFIGDATA, also
 * a array of config
 */
public class Config_pData extends Config {

    private P2Data actValue;

    public Config_pData(P2Data p2Data) {
        super(p2Data.getTag());
        this.actValue = p2Data;
    }

//    public Config_pData(String key, PData pData) {
    // --> key wird beim schreiben NICHT verwendet!!!
//        super(key);
//        this.actValue = pData;
//    }

    @Override
    public P2Data getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (P2Data) act;
    }

    public void setActValue(P2Data act) {
        actValue = act;
    }
}
