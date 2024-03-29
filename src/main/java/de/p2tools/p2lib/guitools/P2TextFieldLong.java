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
import javafx.beans.property.LongProperty;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.util.Locale;

public class P2TextFieldLong extends TextField {

    private boolean stateLabel = false;
    private final Locale locale = Locale.GERMAN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    LongProperty longProperty = null;

    public P2TextFieldLong(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    public P2TextFieldLong() {
    }

    public P2TextFieldLong(LongProperty longProperty) {
        this.longProperty = longProperty;
        bindBidirectional();
    }

    public P2TextFieldLong(LongProperty longProperty, boolean stateLabel) {
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
            setStyle(P2Styles.PTEXTFIELD_LABEL);
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
        P2NumberStringConverter pnu = new P2NumberStringConverter(this, stateLabel);
        Bindings.bindBidirectional(textProperty(), longProperty, pnu);
        pnu.check(this.getText()); // damit "false" beim Wechsel wieder gelöscht wird
    }
}
