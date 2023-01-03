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
import de.p2tools.p2Lib.tools.date.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Its a "PData" for the program configuration, also a list of "Config"
 * in whitch the programparameter are stored. This class exists only one time
 * in a programm!
 */
public class PDataProgConfig extends PDataSample<PDataProgConfig> {

    public String TAG = "progConfig";
    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public PDataProgConfig() {
    }

    public PDataProgConfig(String TAG) {
        this.TAG = TAG;
    }

    //========================================================================================================
    //Kommentar
    //========================================================================================================
    public static synchronized void addComment(String comment) {
        if (!comment.isEmpty()) {
            Config_comment c = new Config_comment(comment);
            arrayList.add(c);
        }
    }

    //========================================================================================================
    //Daten
    //========================================================================================================
    public static synchronized StringProperty addStrProp(String key) {
        StringProperty property = new SimpleStringProperty("");
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized StringProperty addStrProp(String key, String init) {
        StringProperty property = new SimpleStringProperty(init);
        Config_stringProp c = new Config_stringProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized StringProperty addStrPropC(String comment, String key) {
        addComment(comment);
        return addStrProp(key);
    }

    public static synchronized StringProperty addStrPropC(String comment, String key, String init) {
        addComment(comment);
        return addStrProp(key, init);
    }

    public static synchronized IntegerProperty addIntProp(String key) {
        IntegerProperty property = new SimpleIntegerProperty(0);
        Config_intProp c = new Config_intProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized IntegerProperty addIntProp(String key, int init) {
        IntegerProperty property = new SimpleIntegerProperty(init);
        Config_intProp c = new Config_intProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized IntegerProperty addIntPropC(String comment, String key, int init) {
        addComment(comment);
        return addIntProp(key, init);
    }

    public static synchronized LongProperty addLongProp(String key) {
        LongProperty property = new SimpleLongProperty(0);
        Config_longProp c = new Config_longProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized LongProperty addLongProp(String key, long init) {
        LongProperty property = new SimpleLongProperty(init);
        Config_longProp c = new Config_longProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized LongProperty addLongPropC(String comment, String key, long init) {
        addComment(comment);
        return addLongProp(key, init);
    }

    public static synchronized DoubleProperty addDoubleProp(String key) {
        DoubleProperty property = new SimpleDoubleProperty(0.0);
        Config_doubleProp c = new Config_doubleProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized DoubleProperty addDoubleProp(String key, double init) {
        DoubleProperty property = new SimpleDoubleProperty(init);
        Config_doubleProp c = new Config_doubleProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized DoubleProperty addDoublePropC(String comment, String key, double init) {
        addComment(comment);
        return addDoubleProp(key, init);
    }

    public static synchronized BooleanProperty addBoolProp(String key) {
        BooleanProperty property = new SimpleBooleanProperty(false);
        Config_boolProp c = new Config_boolProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized BooleanProperty addBoolProp(String key, boolean init) {
        BooleanProperty property = new SimpleBooleanProperty(init);
        Config_boolProp c = new Config_boolProp(key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized BooleanProperty addBoolPropC(String comment, String key, boolean init) {
        addComment(comment);
        return addBoolProp(key, init);
    }

    public static synchronized PDateProperty addPDateProp(String key, PDate pDate) {
        PDateProperty property = new PDateProperty(pDate);
        ConfigExtra_pDateProp c = new ConfigExtra_pDateProp(key, key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized PLDateProperty addPLocalDateProp(String key, LocalDate localDate) {
        PLDateProperty property = new PLDateProperty(localDate);
        ConfigExtra_lDateProp c = new ConfigExtra_lDateProp(key, key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized PLDateTimeProperty addPLocalDateTimeProp(String key, LocalDateTime localDateTime) {
        PLDateTimeProperty property = new PLDateTimeProperty(localDateTime);
        ConfigExtra_lDateTimeProp c = new ConfigExtra_lDateTimeProp(key, key, property);
        arrayList.add(c);
        return property;
    }

    public static synchronized PLTimeProperty addPLocalTimeProp(String key, LocalTime localTime) {
        PLTimeProperty property = new PLTimeProperty(localTime);
        ConfigExtra_lTimeProp c = new ConfigExtra_lTimeProp(key, key, property);
        arrayList.add(c);
        return property;
    }

    //========================================================================================================
    //Listen
    //========================================================================================================
    public static synchronized ObservableList<String> addListPropC(String comment, String key) {
        addComment(comment);
        return addListProp(key);
    }

    public static synchronized ObservableList<String> addListProp(String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ConfigStringList c = new ConfigStringList(key, list);
        arrayList.add(c);
        return list;
    }

    //========================================================================================================
    //ObjectProperty
    //========================================================================================================
    public static synchronized ObjectProperty<String> addObjStrPropC(String comment, String key) {
        addComment(comment);
        return addObjStrProp(key);
    }

    public static synchronized ObjectProperty<String> addObjStrProp(String key) {
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

    public static synchronized ObjectProperty<Integer> addObjIntPropC(String comment, String key) {
        addComment(comment);
        return addObjIntProp(key);
    }

    public static synchronized ObjectProperty<Integer> addObjIntProp(String key) {
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

    public void init(String TAG) {
        this.TAG = TAG;
    }
}
