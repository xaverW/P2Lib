/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.tools.file.PFileSize;

public class Config_pFileSize extends Config {

    private final PFileSize actValue;

    public Config_pFileSize(String key, PFileSize actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_pFileSize(String key, String name, PFileSize actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public PFileSize getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setSizeL(((PFileSize) act).getSizeL());
    }

    public void setActValue(PFileSize act) {
        actValue.setSizeL(act.getSizeL());
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setSizeStr(act);
        } catch (Exception ex) {
            actValue.setSizeL(0);
        }
        actValue.setSizeStr(act);
    }

    @Override
    public String getActValueString() {
        final String ret = getActValue() == null ? "" : getActValue().toString();
        return ret;
    }
}
