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

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;

public class Config_all extends Config {

    private final String key;
    private final String init;
    private final StringProperty aktValue;

    public Config_all(String key) {
        this.key = key;
        init = "";
        aktValue = new SimpleStringProperty(init);
    }

    public Config_all(String key, String init) {
        this.key = key;
        this.init = init;
        aktValue = new SimpleStringProperty(this.init);
    }

    public Config_all(String key, int init) {
        this.key = key;
        this.init = String.valueOf(init);
        aktValue = new SimpleStringProperty(this.init);
    }

    public Config_all(String key, boolean init) {
        this.key = key;
        this.init = String.valueOf(init);
        aktValue = new SimpleStringProperty(this.init);
    }

    @Override
    public String getKey() {
        return key;
    }

    public StringProperty getAktValue() {
        return aktValue;
    }

    // ====================================================
    // set value
    public synchronized void setValue(String value) {
        getAktValue().setValue(value == null ? "" : value);
    }

    public synchronized void setValue(boolean value) {
        getAktValue().setValue(String.valueOf(value));
    }

    public synchronized void setValue(int value) {
        getAktValue().setValue(String.valueOf(value));
    }

    public synchronized void setValue(long value) {
        getAktValue().setValue(String.valueOf(value));
    }

    public synchronized void setValue(double value) {
        getAktValue().setValue(String.valueOf(value));
    }

    // ====================================================
    // get value
    public synchronized String get() {
        return aktValue.getValue();
    }

    public synchronized boolean getBool() {
        return Boolean.parseBoolean(aktValue.getValue());
    }

    public synchronized int getInt() {
        int ret;
        try {
            ret = Integer.parseInt(aktValue.getValue());
        } catch (final Exception ignore) {
            aktValue.setValue("0");
            ret = 0;
        }
        return ret;
    }

    public synchronized long getLong() {
        long ret;
        try {
            ret = Long.parseLong(aktValue.getValue());
        } catch (final Exception ignore) {
            aktValue.setValue("0");
            ret = 0;
        }
        return ret;
    }

    public synchronized double getDouble() {
        double ret;
        try {
            final String s = aktValue.getValue();
            ret = Double.parseDouble(s);
        } catch (final Exception ignore) {
            aktValue.setValue("0");
            ret = 0;
        }
        return ret;
    }

    // ================================================
    // get property
    public synchronized StringProperty getStringProperty() {
        return aktValue;
    }

    public synchronized BooleanProperty getBooleanProperty() {
        final StringProperty sp = getStringProperty();
        final BooleanProperty ip = new SimpleBooleanProperty();
        try {
            ip.setValue(Boolean.parseBoolean(sp.getValue()));
        } catch (final Exception ex) {
            sp.setValue(Boolean.TRUE.toString());
            ip.setValue(true);
        }
        final StringConverter<Boolean> converter = new BooleanStringConverter();
        Bindings.bindBidirectional(sp, ip, converter);
        return ip;
    }

    public IntegerProperty getIntegerProperty() {
        final StringProperty sp = getStringProperty();
        final IntegerProperty ip = new SimpleIntegerProperty();
        try {
            ip.setValue(Integer.parseInt(sp.getValue()));
        } catch (final Exception ex) {
            sp.setValue("0");
            ip.setValue(0);
        }
        final StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("##"));
        Bindings.bindBidirectional(sp, ip, converter);
        return ip;
    }

    public LongProperty getLongProperty() {
        final StringProperty sp = getStringProperty();
        final LongProperty lp = new SimpleLongProperty();
        try {
            lp.setValue(Long.parseLong(sp.getValue()));
        } catch (final Exception ex) {
            sp.setValue("0");
            lp.setValue(0);
        }
        final StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("##"));
        Bindings.bindBidirectional(sp, lp, converter);
        return lp;
    }

    public synchronized DoubleProperty getDoubleProperty() {
        final StringProperty sp = getStringProperty();
        final DoubleProperty dp = new SimpleDoubleProperty();
        try {
            dp.setValue(Double.parseDouble(sp.getValue()));
        } catch (final Exception ex) {
            sp.setValue("0");
            dp.setValue(0);
        }
        final StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("##"));
        Bindings.bindBidirectional(sp, dp, converter);
//        final Locale locale = new Locale("en", "US");
//        final NumberFormat nf = NumberFormat.getNumberInstance(locale);
//        final DecimalFormat df = (DecimalFormat) nf;
//        Bindings.bindBidirectional(sp, dp, new StringConverter<Number>() {
//            @Override
//            public String toString(Number object) {
//                return object == null ? "0" : df.format(object);
//            }
//
//            @Override
//            public Number fromString(String string) {
//                return (string != null && !string.isEmpty()) ? Double.valueOf(string) : 0;
//            }
//        });
        return dp;
    }
}


