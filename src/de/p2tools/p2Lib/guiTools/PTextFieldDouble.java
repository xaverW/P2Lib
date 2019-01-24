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


package de.p2tools.p2Lib.guiTools;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class PTextFieldDouble extends TextField {
    DoubleProperty doubleProperty = null;

    private boolean stateLikeLabel = false;
    private final Locale locale = Locale.GERMAN;
    private final NumberFormat nf = NumberFormat.getNumberInstance(locale);
    private final DecimalFormat df = new DecimalFormat("###,##0.00");

    public PTextFieldDouble() {
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

    public PTextFieldDouble(boolean stateLikeLabel) {
        this.stateLikeLabel = stateLikeLabel;
        setStateLikeLabel();
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

    public PTextFieldDouble(DoubleProperty doubleProperty) {
        this.doubleProperty = doubleProperty;
        bindBidirectional();
    }

    public PTextFieldDouble(DoubleProperty doubleProperty, boolean stateLikeLabel) {
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
            setStyle(PStyles.PTEXTFIELD_LABEL);
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
        this.setText(df.format(text));
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

                    ret = nf.parse(value);
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
        setStyle(stateLikeLabel ? PStyles.PTEXTFIELD_LABEL : "");

        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            nf.parse(value.trim());
        } catch (ParseException ex) {
            setStyle(stateLikeLabel ? PStyles.PTEXTFIELD_LABEL_ERROR : PStyles.PTEXTFIELD_ERROR);
        }
    }
}
