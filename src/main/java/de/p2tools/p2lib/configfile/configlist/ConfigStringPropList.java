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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ConfigStringPropList extends ConfigList {
    private ObservableList<StringProperty> actValue;

    public ConfigStringPropList(String key, ObservableList<StringProperty> actValue) {
        super(key);
        this.actValue = actValue;
    }

    public void setActValue(StringProperty actValue) {
        this.actValue.add(actValue);
    }

    @Override
    public void setActValue(String actValue) {
        this.actValue.add(new SimpleStringProperty(actValue));
    }

    @Override
    public ObservableList getActValue() {
        return actValue;
    }

    public ObservableList<StringProperty> getStrList() {
        return actValue;
    }

    @Override
    public String getActValueString() {
        return super.getKey();
    }

}
