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

import de.p2tools.p2lib.configfile.config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * this is the class for one configuration
 * for example: the configsData for a ComboBox(the ObservableList),
 * this Config contains a LIST of Elements, it can store also more
 * Objects as ONE CONFIG
 */

public abstract class ConfigList extends Config {
    private final String key;
    private ObservableList<Object> actValue = FXCollections.observableArrayList();

    public ConfigList(String key) {
        this.key = key;
    }

    public ConfigList(String key, ObservableList<Object> actValue) {
        this.key = key;
        this.actValue = actValue;
    }

    public String getKey() {
        return key;
    }

    public ObservableList<Object> getList() {
        return actValue;
    }

    public void setActValue(Object actValue) {
        this.actValue.add(actValue);
    }

    public ObservableList<Object> getActValue() {
        return actValue;
    }

}


