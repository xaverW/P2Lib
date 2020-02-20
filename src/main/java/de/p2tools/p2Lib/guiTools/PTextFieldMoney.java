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
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class PTextFieldMoney extends TextField {

    private static final Locale locale = Locale.GERMAN;
    private static final NumberFormat NF;
    private static final DecimalFormat DF;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00 €");
    }

    private LongProperty longProperty = null; // Wert in Cent
    private ChangeListener lChangeListener = null;
    private DoubleProperty doubleProperty = null; // Wert in €
    private ChangeListener dChangeListener = null;
    private DoubleBinding db = null;
    private boolean onlyLabel = false; // damit ist es IMMER nur ein Label
    private boolean labelLike = false; // damit kann man den Status "Label" umschalten

    public PTextFieldMoney() {
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

    public PTextFieldMoney(boolean onlyLabel) {
        this.onlyLabel = onlyLabel;
        this.labelLike = onlyLabel;
        setLabelLike();
        textProperty().addListener((observable, oldValue, newValue) -> setTextStyle(getText()));
    }

//    public PTextFieldMoney(LongProperty longProperty) {
//        this.longProperty = longProperty;
//        bindBidirectional();
//    }

    public PTextFieldMoney(LongProperty longProperty, boolean onlyLabel) {
        this.longProperty = longProperty;
        this.onlyLabel = onlyLabel;
        this.labelLike = onlyLabel;
        setLabelLike();
        bindBidirectional();
    }

    public void setLabelLike(boolean stateLikeLabel) {
        this.labelLike = stateLikeLabel;
        setLabelLike();
    }

    private void setLabelLike() {
        setEditable(!labelLike);
        setStyle(labelLike ? PStyles.PTEXTFIELD_LABEL : "");
    }

    public void bindBidirectional(LongProperty longProperty) {
        this.longProperty = longProperty;
        bindBidirectional();
    }

    public void bindBidirectional() {
        if (longProperty == null) {
            unBind();
            return;
        }

        lChangeListener = (observable, oldValue, newValue) -> {
            doubleProperty.set(0.01 * longProperty.get());
//            System.out.println("PTextFieldMoney--doubleProperty.set: " + 0.01 * longProperty.get());
        };
        longProperty.addListener(lChangeListener);

        doubleProperty = new SimpleDoubleProperty();
        doubleProperty.set(0.01 * longProperty.get());
        if (!onlyLabel) {
            // kann man sich dann sparen
            dChangeListener = (observable, oldValue, newValue) -> {
                Double d = doubleProperty.get() * 100;
                longProperty.set(d.longValue());
//                System.out.println("PTextFieldMoney--longProperty.set: " + d.longValue());
            };
            doubleProperty.addListener(dChangeListener);
        }


        Bindings.bindBidirectional(textProperty(), doubleProperty, new NumberStringConverter(DF) {
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

                    ret = PTextFieldMoney.NF.parse(value);
                } catch (ParseException ignored) {
                }
                return ret;
            }
        });

    }

    public void unBind() {
        if (doubleProperty != null) {
            textProperty().unbindBidirectional(doubleProperty);
            if (dChangeListener != null) {
                doubleProperty.removeListener(dChangeListener);
            }
        }

        if (longProperty != null && lChangeListener != null) {
            longProperty.removeListener(lChangeListener);
        }

        doubleProperty = null;
        dChangeListener = null;
        lChangeListener = null;
    }

//    public void setValue(String text) {
//        super.setText(text);
//    }

    private void setTextStyle(String value) {
        setStyle(labelLike ? PStyles.PTEXTFIELD_LABEL : "");

        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            NF.parse(value.trim());
        } catch (ParseException ex) {
            setStyle(labelLike ? PStyles.PTEXTFIELD_LABEL_ERROR : PStyles.PTEXTFIELD_ERROR);
        }
    }
}
