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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.tools.PRegEx;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class PTextFieldRegEx extends TextField {
    StringProperty stringProperty = null;

    private boolean stateLabel = false;
    private ChangeListener changeListener;
    private Pattern pattern = null;

    public PTextFieldRegEx() {
        init();
    }

    public PTextFieldRegEx(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public PTextFieldRegEx(StringProperty property) {
        this.stringProperty = property;
        init();
    }

    public PTextFieldRegEx(StringProperty property, boolean stateLabel) {
        this.stringProperty = property;
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public PTextFieldRegEx(String regEx) {
        this.pattern = PRegEx.makePattern(regEx);
        init();
    }

    public PTextFieldRegEx(String regEx, boolean stateLabel) {
        this.pattern = PRegEx.makePattern(regEx);
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
            setStyle(PStyles.PTEXTFIELD_LABEL);
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
        pattern = PRegEx.makePattern(regEx);
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
        if (PRegEx.check(stringProperty.getValue(), pattern)) {
            setStyle(stateLabel ? PStyles.PTEXTFIELD_LABEL : "");

        } else {
            setStyle(stateLabel ? PStyles.PTEXTFIELD_LABEL_ERROR : PStyles.PTEXTFIELD_ERROR);
        }
    }
}
