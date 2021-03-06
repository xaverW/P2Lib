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
import javafx.beans.property.LongProperty;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.util.Locale;

public class PTextFieldLong extends TextField {

    private boolean stateLabel = false;
    private final Locale locale = Locale.GERMAN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    LongProperty longProperty = null;

    public PTextFieldLong(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    public PTextFieldLong() {
    }

    public PTextFieldLong(LongProperty longProperty) {
        this.longProperty = longProperty;
        bindBidirectional();
    }

    public PTextFieldLong(LongProperty longProperty, boolean stateLabel) {
        this.longProperty = longProperty;
        this.stateLabel = stateLabel;
        setStateLabel();
        bindBidirectional();
    }

    public void setStateLabel(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    private void setStateLabel() {
        setEditable(!stateLabel);
        if (stateLabel) {
            setStyle(PStyles.PTEXTFIELD_LABEL);
        } else {
            setStyle(null);
        }
    }

    public LongProperty getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(LongProperty longProperty) {
        unBind();
        this.longProperty = longProperty;
        bindBidirectional();
    }

    public long getLong() {
        return longProperty.get();
    }

    public void bindBidirectional(LongProperty longProperty) {
        unBind();
        this.longProperty = longProperty;
        bindBidirectional();
    }

    public void setPattern(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        unBind();
        bindBidirectional();
    }

    public void unBind() {
        if (longProperty == null) {
            return;
        }

        textProperty().unbindBidirectional(longProperty);
    }

    private void bindBidirectional() {
        if (longProperty == null) {
            return;
        }
        PNumberStringConverter pnu = new PNumberStringConverter(this, stateLabel);
        Bindings.bindBidirectional(textProperty(), longProperty, pnu);
        pnu.check(this.getText()); // damit "false" beim Wechsel wieder gelöscht wird
    }
}
