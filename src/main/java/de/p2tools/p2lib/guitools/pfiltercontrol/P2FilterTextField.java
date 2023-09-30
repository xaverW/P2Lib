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


package de.p2tools.p2lib.guitools.pfiltercontrol;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class P2FilterTextField extends HBox {


    private final TextField textField = new TextField();
    private final Button btnClear = new Button("X");
    private StringProperty textProperty;

    public P2FilterTextField() {
        super();

        initHBox();
    }

    public P2FilterTextField(StringProperty textProperty) {
        super();
        this.textProperty = textProperty;

        initHBox();
        initTextField();
    }

    public void init(StringProperty selValueProperty) {
        this.textProperty = selValueProperty;

        initTextField();
    }

    public void bindTextProperty(StringProperty stringProperty) {
        unbind();
        textProperty = stringProperty;
        bind();
    }

    public void unbindTextProperty() {
        unbind();
        this.textProperty = null;
    }

    public String getText() {
        return textField.getText();
    }

    public void clearText() {
        textField.setText("");
    }

    private void initHBox() {
        getStyleClass().add("PFilterTextField");
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
        this.getChildren().addAll(textField, btnClear);
        btnClear.setOnAction(a -> clearText());
    }

    private void initTextField() {
        textField.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                clearText();
            }
        });

        bind();
    }


    private void bind() {
        if (textProperty == null) {
            clearText();
            return;
        }

        textField.textProperty().bindBidirectional(textProperty);
    }

    private void unbind() {
        if (textProperty == null) {
            clearText();
            return;
        }

        textField.textProperty().unbindBidirectional(textProperty);
    }

}
