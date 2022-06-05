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

import de.p2tools.p2Lib.guiTools.PDownloadSize;

public class ConfigPDownloadSize extends ConfigExtra {

    private PDownloadSize pDownloadSize;

    public ConfigPDownloadSize(String key, String name, PDownloadSize pDownloadSize) {
        super(key, name);
        this.pDownloadSize = pDownloadSize;
    }

    @Override
    public PDownloadSize getActValue() {
        return pDownloadSize;
    }

    @Override
    public String getActValueString() {
        final String ret = getActValue() == null ? "" : getActValue().toString();
        return ret;
    }

    public void setActValue(PDownloadSize act) {
        pDownloadSize = act;
    }

    @Override
    public void setActValue(String act) {
        try {
            pDownloadSize.setFileSize(act);
        } catch (Exception ex) {
            pDownloadSize.setFileSize(0);
        }
    }
}
