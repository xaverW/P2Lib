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


package de.p2tools.p2Lib.data;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.configList.ConfigStringList;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PDateProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.util.ArrayList;

/**
 * Its a "PData" for the program configuration, also a list of "Config"
 * in whitch the programparameter are stored. This class exists only one time
 * in a programm!
 */
public class PDataProgConfig extends PDataSample<PDataProgConfig> {

    public String TAG = "progConfig";
    private ArrayList<Config> arrayList;

    public PDataProgConfig() {
    }

    public PDataProgConfig(ArrayList<Config> arrayList, String TAG) {
        this.arrayList = arrayList;
        this.TAG = TAG;
    }

    public static synchronized void addComment(ArrayList<Config> arrayList, String comment) {
        if (!comment.isEmpty()) {
            Config_comment c = new Config_comment(comment);
            arrayList.add(c);
        }
    }

    //========================================================================================================
    //Daten
    //========================================================================================================
    public static synchronized StringProperty addStrPropC(String comment, ArrayList<Config> arrayList, String key) {
        addComment(arrayList, comment);
        return addStrProp(arrayList, key);
    }

    public static synchronized StringProperty addStrProp(ArrayList<Config> arrayList, String key) {
        StringProperty property = new SimpleStringProperty("");
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized StringProperty addStrPropC(String comment, ArrayList<Config> arrayList, String key, String init) {
        addComment(arrayList, comment);
        return addStrProp(arrayList, key, init);
    }

    public static synchronized StringProperty addStrProp(ArrayList<Config> arrayList, String key, String init) {
        StringProperty property = new SimpleStringProperty(init);
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized IntegerProperty addIntPropC(String comment, ArrayList<Config> arrayList, String key, int init) {
        addComment(arrayList, comment);
        return addIntProp(arrayList, key, init);
    }

    public static synchronized IntegerProperty addIntProp(ArrayList<Config> arrayList, String key, int init) {
        IntegerProperty property = new SimpleIntegerProperty(init);
        Config_intProp c = new Config_intProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized LongProperty addLongPropC(String comment, ArrayList<Config> arrayList, String key, long init) {
        addComment(arrayList, comment);
        return addLongProp(arrayList, key, init);
    }

    public static synchronized LongProperty addLongProp(ArrayList<Config> arrayList, String key, long init) {
        LongProperty property = new SimpleLongProperty(init);
        Config_longProp c = new Config_longProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized DoubleProperty addDoublePropC(String comment, ArrayList<Config> arrayList, String key, double init) {
        addComment(arrayList, comment);
        return addDoubleProp(arrayList, key, init);
    }

    public static synchronized DoubleProperty addDoubleProp(ArrayList<Config> arrayList, String key, double init) {
        DoubleProperty property = new SimpleDoubleProperty(init);
        Config_doubleProp c = new Config_doubleProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized BooleanProperty addBoolPropC(String comment, ArrayList<Config> arrayList, String key, boolean init) {
        addComment(arrayList, comment);
        return addBoolProp(arrayList, key, init);
    }

    public static synchronized BooleanProperty addBoolProp(ArrayList<Config> arrayList, String key, boolean init) {
        BooleanProperty property = new SimpleBooleanProperty(init);
        Config_boolProp c = new Config_boolProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized PDateProperty addPDateProp(ArrayList<Config> arrayList, String key, PDate pDate) {
        PDateProperty property = new PDateProperty(pDate);
        ConfigExtra_pDateProp c = new ConfigExtra_pDateProp(key, key, property);
        arrayList.add(c);
        return property;
    }

    //========================================================================================================
    //Listen
    //========================================================================================================
    public static synchronized ObservableList<String> addListPropC(String comment, ArrayList<Config> arrayList, String key) {
        addComment(arrayList, comment);
        return addListProp(arrayList, key);
    }

    public static synchronized ObservableList<String> addListProp(ArrayList<Config> arrayList, String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ConfigStringList c = new ConfigStringList(key, list);
        arrayList.add(c);
        return list;
    }

    //========================================================================================================
    //ObjectProperty
    //========================================================================================================
    public static synchronized ObjectProperty<String> addObjStrPropC(String comment, ArrayList<Config> arrayList, String key) {
        addComment(arrayList, comment);
        return addObjStrProp(arrayList, key);
    }

    public static synchronized ObjectProperty<String> addObjStrProp(ArrayList<Config> arrayList, String key) {
        StringProperty property = new SimpleStringProperty("");
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);

        ObjectProperty<String> objP = new SimpleObjectProperty<>("");
        property.bindBidirectional(objP, new StringConverter<String>() {
            @Override
            public String toString(String string) {
                return string == null ? "" : string;
            }

            @Override
            public String fromString(String string) {
                return string == null ? "" : string;
            }
        });

        return objP;
    }

    public static synchronized ObjectProperty<Integer> addObjIntPropC(String comment, ArrayList<Config> arrayList, String key) {
        addComment(arrayList, comment);
        return addObjIntProp(arrayList, key);
    }

    public static synchronized ObjectProperty<Integer> addObjIntProp(ArrayList<Config> arrayList, String key) {
        StringProperty property = new SimpleStringProperty("");
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);

        ObjectProperty<Integer> objP = new SimpleObjectProperty<>();
        property.bindBidirectional(objP, new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return integer == null ? "" : integer + "";
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return 0;
                } else {
                    try {
                        return Integer.parseInt(string);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        });

        return objP;
    }

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

    public void init(ArrayList<Config> arrayList, String TAG) {
        this.arrayList = arrayList;
        this.TAG = TAG;
    }

    public void init(String TAG) {
        this.TAG = TAG;
    }

    public void init(ArrayList<Config> arrayList) {
        this.arrayList = arrayList;
    }
}
