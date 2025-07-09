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


package de.p2tools.p2lib.configfile.configlist;

import javafx.collections.ObservableList;

public class ConfigLongList extends ConfigList {
    private final ObservableList<Long> actValue;

    public ConfigLongList(String key, ObservableList<Long> actValue) {
        super(key);
        this.actValue = actValue;
    }

    public void setActValue(String actValue) {
        try {
            this.actValue.add(Long.valueOf(actValue));
        } catch (Exception ignore) {
        }
    }

    public ObservableList getActValue() {
        return actValue;
    }

    public ObservableList<Long> getStrList() {
        return actValue;
    }

    public String getActValueString() {
        return super.getKey();
    }
}
