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

import de.p2tools.p2Lib.tools.PRegEx;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class PTextFieldRegEx extends TextField {
    StringProperty stringProperty = null;
    ChangeListener changeListener;
    Pattern pattern = null;

    public PTextFieldRegEx() {
        init();
    }

    public PTextFieldRegEx(StringProperty property) {
        this.stringProperty = property;
        init();
    }

    public PTextFieldRegEx(String regEx) {
        init();
        pattern = PRegEx.makePattern(regEx);
    }

    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(StringProperty property) {
        unBind();
        this.stringProperty = property;
        bind();
    }

    public void setRegEx(String regEx) {
        pattern = PRegEx.makePattern(regEx);
    }

    public void bind(StringProperty property) {
        this.stringProperty = property;
        bind();
    }

    public void bind() {
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
        bind();
        changeListener = (observable, oldValue, newValue) -> check();
    }

    private void check() {
        setStyle("");
        if (!PRegEx.check(stringProperty.getValue(), pattern)) {
            setStyle("-fx-control-inner-background: #FF0000;");
        }
    }
}
