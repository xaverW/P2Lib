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

import de.p2tools.p2lib.tools.P2RegEx;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class P2TextFieldRegEx extends TextField {
    StringProperty stringProperty = null;

    private boolean stateLabel = false;
    private ChangeListener changeListener;
    private Pattern pattern = null;

    public P2TextFieldRegEx() {
        init();
    }

    public P2TextFieldRegEx(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public P2TextFieldRegEx(StringProperty property) {
        this.stringProperty = property;
        init();
    }

    public P2TextFieldRegEx(StringProperty property, boolean stateLabel) {
        this.stringProperty = property;
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public P2TextFieldRegEx(String regEx) {
        this.pattern = P2RegEx.makePattern(regEx);
        init();
    }

    public P2TextFieldRegEx(String regEx, boolean stateLabel) {
        this.pattern = P2RegEx.makePattern(regEx);
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public void setStateLabel(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
    }

    private void setStateLabel() {
        setEditable(!stateLabel);
        if (stateLabel) {
            setStyle(P2Styles.PTEXTFIELD_LABEL);
        }
    }

    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(StringProperty property) {
        unBind();
        this.stringProperty = property;
        bindBidirectional();
    }

    public void setRegEx(String regEx) {
        pattern = P2RegEx.makePattern(regEx);
    }

    public void bindBidirectional(StringProperty property) {
        this.stringProperty = property;
        bindBidirectional();
    }

    public void bindBidirectional() {
        if (stringProperty == null) {
            return;
        }

        stringProperty.addListener(changeListener);
        textProperty().bindBidirectional(stringProperty);
        check();
    }

    public void unBind() {
        if (stringProperty == null) {
            return;
        }

        stringProperty.removeListener(changeListener);
        textProperty().unbindBidirectional(stringProperty);
    }

    private void init() {
        bindBidirectional();
        changeListener = (observable, oldValue, newValue) -> check();
    }

    private void check() {
        if (P2RegEx.check(stringProperty.getValue(), pattern)) {
            setStyle(stateLabel ? P2Styles.PTEXTFIELD_LABEL : "");

        } else {
            setStyle(stateLabel ? P2Styles.PTEXTFIELD_LABEL_ERROR : P2Styles.PTEXTFIELD_ERROR);
        }
    }
}
