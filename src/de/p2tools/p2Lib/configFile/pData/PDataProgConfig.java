/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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


package de.p2tools.p2Lib.configFile.pData;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.configList.ConfigStringList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Its a "PData" for the program configuration, also a list of "Config"
 * in whitch the programparameter are stored. This class exists only one time
 * in a programm!
 */
public class PDataProgConfig extends PDataSample<PDataProgConfig> {

    public static final String TAG = "ProgConfig";
    private static final ArrayList<Config> arrayList = new ArrayList<>();

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Programmeinstellungen";
    }

    @Override
    public Config[] getConfigsArr() {
        return arrayList.toArray(new Config[]{});
    }

    public static synchronized StringProperty addStrProp(String key) {
        StringProperty property = new SimpleStringProperty("");
        ConfigStringProp c = new ConfigStringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized StringProperty addStrProp(String key, String init) {
        StringProperty property = new SimpleStringProperty(init);
        ConfigStringProp c = new ConfigStringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized IntegerProperty addIntProp(String key, int init) {
        IntegerProperty property = new SimpleIntegerProperty(init);
        ConfigIntProp c = new ConfigIntProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized DoubleProperty addDoubleProp(String key, double init) {
        DoubleProperty property = new SimpleDoubleProperty(init);
        ConfigDoubleProp c = new ConfigDoubleProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized BooleanProperty addBoolProp(String key, boolean init) {
        BooleanProperty property = new SimpleBooleanProperty(init);
        ConfigBoolProp c = new ConfigBoolProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized ObservableList<String> addListProp(String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ConfigStringList c = new ConfigStringList(key, list);
        arrayList.add(c);
        return list;
    }
}
