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

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class PTextField extends TextField {
    StringProperty stringProperty = null;

    private boolean stateLabel = false;

    public PTextField() {
        init();
    }

    public PTextField(boolean stateLabel) {
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public PTextField(StringProperty property) {
        this.stringProperty = property;
        init();
    }

    public PTextField(StringProperty property, boolean stateLabel) {
        this.stringProperty = property;
        this.stateLabel = stateLabel;
        setStateLabel();
        init();
    }

    public PTextField(String text, boolean stateLabel) {
        this.setText(text);
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
        } else {
            setStyle(null);
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

    public void bindBidirectional(StringProperty property) {
        unBind();
        this.stringProperty = property;
        bindBidirectional();
    }

    public void bindBidirectional() {
        if (stringProperty == null) {
            return;
        }

        textProperty().bindBidirectional(stringProperty);
    }

    public void unBind() {
        if (stringProperty == null) {
            return;
        }

        textProperty().unbindBidirectional(stringProperty);
    }

    private void init() {
        bindBidirectional();
    }
}
