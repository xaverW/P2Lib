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
import de.p2tools.p2lib.configfile.pdata.P2DataList;

/**
 * its a pseudo CONFIG, it contains a
 * CONFIGSLIST, therefore a list of ConfigData
 * and that contains an array of Config
 */
public class Config_pDataList extends Config {

    private P2DataList actValue;

//    public Config_pDataList(String key, PDataList<? extends PData> pDataList) {
    // --> key wird beim schreiben NICHT verwendet!!!
//        super(key);
//        this.actValue = pDataList;
//    }

    public Config_pDataList(P2DataList<? extends P2Data> p2DataList) {
        super(p2DataList.getTag());
        this.actValue = p2DataList;
    }

    @Override
    public void setActValue(Object act) {
        actValue.addAll((P2DataList) act);
    }

    public void setActValue(P2DataList act) {
        actValue.addAll(act);
    }

    @Override
    public P2DataList getActValue() {
        return actValue;
    }

    @Override
    public String getName() {
        //nur damit es klar ist!! wichtig!!
        return super.getKey();
    }
}
