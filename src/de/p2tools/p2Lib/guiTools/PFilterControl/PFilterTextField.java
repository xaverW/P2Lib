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


package de.p2tools.p2Lib.guiTools.PFilterControl;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PFilterTextField extends HBox {


    private StringProperty selValueProperty;
    private TextField textField = new TextField();
    private Button btnClear = new Button("X");

    public PFilterTextField() {
        super();

        initHBox();
    }

    public PFilterTextField(StringProperty selValueProperty) {
        super();
        this.selValueProperty = selValueProperty;

        initHBox();
        initTextField();
    }

    public void init(StringProperty selValueProperty) {
        this.selValueProperty = selValueProperty;

        initTextField();
    }

    public void bindSelValueProperty(StringProperty stringProperty) {
        unbind();
        selValueProperty = stringProperty;
        bind();
    }

    public void unbindSelValueProperty() {
        unbind();
        this.selValueProperty = null;
    }

    public String getText() {
        return textField.getText();
    }

    public void clearText() {
        textField.setText("");
    }

    private void initHBox() {
        getStyleClass().add("PFilterTextField");
        final String CSS_FILE = "de/p2tools/p2Lib/p2Lib.css";
        getStylesheets().add(CSS_FILE);

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
        if (selValueProperty == null) {
            clearText();
            return;
        }

        textField.textProperty().bindBidirectional(selValueProperty);
    }

    private void unbind() {
        if (selValueProperty == null) {
            clearText();
            return;
        }

        textField.textProperty().unbindBidirectional(selValueProperty);
    }

}
