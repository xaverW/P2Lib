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


import de.p2tools.p2lib.mediathek.download.DownloadSize;

public class Config_pDownloadSizeProp extends Config {

    private DownloadSize actValue;

    public Config_pDownloadSizeProp(String key, DownloadSize downloadSize) {
        super(key);
        this.actValue = downloadSize;
    }

    public Config_pDownloadSizeProp(String key, String name, DownloadSize downloadSize) {
        super(key, name);
        this.actValue = downloadSize;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setTargetSize(((DownloadSize) act).getTargetSize());
    }

    public void setActValue(DownloadSize act) {
        actValue.setTargetSize(act.getTargetSize());
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setTargetSize(act);
        } catch (Exception ex) {
            actValue.setTargetSize(0);
        }
    }

    @Override
    public DownloadSize getActValue() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        final String ret = getActValue() == null ? "" : getActValue().toString();
        return ret;
    }
}
