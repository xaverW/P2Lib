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


package de.p2tools.p2lib.guitools;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class P2TextFieldDouble extends TextField {
    private static final Locale locale = Locale.GERMAN;
    private static final NumberFormat NF;
    private static final DecimalFormat DF;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private DoubleProperty doubleProperty = null;
    private boolean stateLikeLabel = false;

    public P2TextFieldDouble() {
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

    public P2TextFieldDouble(boolean stateLikeLabel) {
        this.stateLikeLabel = stateLikeLabel;
        setStateLikeLabel();
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

    public P2TextFieldDouble(DoubleProperty doubleProperty) {
        this.doubleProperty = doubleProperty;
        bindBidirectional();
    }

    public P2TextFieldDouble(DoubleProperty doubleProperty, boolean stateLikeLabel) {
        this.doubleProperty = doubleProperty;
        this.stateLikeLabel = stateLikeLabel;
        setStateLikeLabel();
        bindBidirectional();
    }

    public void setStateLikeLabel(boolean stateLikeLabel) {
        this.stateLikeLabel = stateLikeLabel;
        setStateLikeLabel();
    }

    private void setStateLikeLabel() {
        setEditable(!stateLikeLabel);
        if (stateLikeLabel) {
            setStyle(P2Styles.PTEXTFIELD_LABEL);
        }
    }

    public DoubleProperty getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(DoubleProperty doubleProperty) {
        unBind();
        this.doubleProperty = doubleProperty;
        bindBidirectional();
    }

    public double getDouble() {
        return doubleProperty.get();
    }


    public void setText(Double text) {
        this.setText(DF.format(text));
    }


    public void bindBidirectional(DoubleProperty doubleProperty) {
        this.doubleProperty = doubleProperty;
        bindBidirectional();
    }

    public void bindBidirectional() {
        if (doubleProperty == null) {
            return;
        }

        Bindings.bindBidirectional(textProperty(), doubleProperty, new NumberStringConverter(new DecimalFormat("###,##0.00")) {
            @Override
            public Number fromString(String value) {
                Number ret = 0;

                setTextStyle(value);
                try {
                    if (value == null) {
                        return null;
                    }

                    value = value.trim();
                    if (value.length() < 1) {
                        return null;
                    }

                    ret = NF.parse(value);
                } catch (ParseException ex) {
                }
                return ret;
            }
        });

    }

    public void unBind() {
        if (doubleProperty == null) {
            return;
        }

        textProperty().unbindBidirectional(doubleProperty);
    }

    private void setTextStyle(String value) {
        setStyle(stateLikeLabel ? P2Styles.PTEXTFIELD_LABEL : "");

        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            NF.parse(value.trim());
        } catch (ParseException ex) {
            setStyle(stateLikeLabel ? P2Styles.PTEXTFIELD_LABEL_ERROR : P2Styles.PTEXTFIELD_ERROR);
        }
    }
}
