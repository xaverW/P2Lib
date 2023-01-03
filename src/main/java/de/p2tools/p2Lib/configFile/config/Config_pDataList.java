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
import de.p2tools.p2Lib.configFile.pData.PDataList;

/**
 * its a pseudo CONFIG, it contains a
 * CONFIGSLIST, therefore a list of ConfigData
 * and that contains an array of Config
 */
public class Config_pDataList extends Config {

    private PDataList actValue;

    public Config_pDataList(String key, PDataList<? extends PData> pDataList) {
        super(key);
        this.actValue = pDataList;
    }

    public Config_pDataList(PDataList<? extends PData> pDataList) {
        super(pDataList.getTag());
        this.actValue = pDataList;
    }

    @Override
    public void setActValue(Object act) {
        actValue.addAll((PDataList) act);
    }

    public void setActValue(PDataList act) {
        actValue.addAll(act);
    }

    @Override
    public PDataList getActValue() {
        return actValue;
    }

    @Override
    public String getName() {
        //nur damit es klar ist!! wichtig!!
        return super.getKey();
    }
}
