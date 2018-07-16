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
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.util.Locale;

public class PTextFieldInteger extends TextField {

    private boolean stateLabel = false;
    IntegerProperty integerProperty = null;

    private final Locale locale = Locale.GERMAN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);


    public PTextFieldInteger() {
    }

    public PTextFieldInteger(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    public PTextFieldInteger(IntegerProperty integerProperty) {
        this.integerProperty = integerProperty;
        bind();
    }

    public PTextFieldInteger(IntegerProperty integerProperty, boolean stateLabel) {
        this.integerProperty = integerProperty;
        this.stateLabel = stateLabel;
        setStateLabel();
        bind();
    }

    public void setStateLabel(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    private void setStateLabel() {
        setEditable(!stateLabel);
        if (stateLabel) {
            setStyle(PStyles.PTEXTFIELD_LABEL);
        }
    }

    public IntegerProperty getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(IntegerProperty integerProperty) {
        unBind();
        this.integerProperty = integerProperty;
        bind();
    }

    public int getInt() {
        return integerProperty.get();
    }

    public void bind(IntegerProperty integerProperty) {
        unBind();
        this.integerProperty = integerProperty;
        bind();
    }

    public void setPattern(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        unBind();
        bind();
    }

    public void unBind() {
        if (integerProperty == null) {
            return;
        }

        textProperty().unbindBidirectional(integerProperty);
    }

    private void bind() {
        if (integerProperty == null) {
            return;
        }

        Bindings.bindBidirectional(textProperty(), integerProperty, new PNumberStringConverter(this, stateLabel));

    }
}
