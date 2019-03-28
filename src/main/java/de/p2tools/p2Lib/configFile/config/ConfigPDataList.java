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
 * and that contains a array of Config
 */
public class ConfigPDataList extends Config {

    private PDataList<? extends PData> actValue;

    public ConfigPDataList(PDataList<? extends PData> pDataList) {
        super(pDataList.getTag());
        this.actValue = pDataList;
    }

    public PDataList<? extends PData> getActValue() {
        return actValue;
    }

    public String getName() {
        return super.getKey();
    }
}
