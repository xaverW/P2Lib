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

import de.p2tools.p2Lib.guiTools.PComboBoxObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PFilterComboBoxObject<E> extends HBox {


    private ObservableList<E> itemsList;
    private ObjectProperty<E> selValueProperty;
    private PComboBoxObject<E> pComboBoxObject = new PComboBoxObject<>();
    private Button btnClear = new Button("X");

    public PFilterComboBoxObject() {
        super();
        addCss();
        initHBox();
    }

    public PFilterComboBoxObject(ObservableList<E> itemsList, ObjectProperty<E> selValueProperty) {
        super();
        this.itemsList = itemsList;
        this.selValueProperty = selValueProperty;

        addCss();
        initHBox();
        setCombo();
    }

    public void init(ObservableList<E> itemsList) {
        this.itemsList = itemsList;

        selectElement();
        setCombo();
    }

    public void init(ObservableList<E> itemsList, ObjectProperty<E> selVaueProperty) {
        this.itemsList = itemsList;
        this.selValueProperty = selVaueProperty;

        selectElement();
        setCombo();
    }

    public void setEditable(boolean editable) {
        pComboBoxObject.setEditable(editable);
    }

    private void addCss() {
        getStyleClass().add("PFilterComboBoxObject");
    }

    public void bindSelValueProperty(ObjectProperty<E> stringProperty) {
        unbind();
        selValueProperty = stringProperty;
        bind();
    }

    public void unbindSelValueProperty() {
        unbind();
        this.selValueProperty = null;
        pComboBoxObject.getSelectionModel().clearSelection();
    }

    public E getSelValue() {
        E s = pComboBoxObject.getSelectionModel().getSelectedItem();
        return s;
    }

    public ReadOnlyObjectProperty<E> getSelValueProperty() {
        ReadOnlyObjectProperty<E> s = pComboBoxObject.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement() {
        if (selValueProperty == null) {
            pComboBoxObject.getSelectionModel().clearSelection();
        } else {
            pComboBoxObject.setValue(selValueProperty.getValue());
        }
    }

    public void clearSelection() {
        pComboBoxObject.getSelectionModel().clearSelection();
    }

    private void initHBox() {
        pComboBoxObject.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(pComboBoxObject, Priority.ALWAYS);
        this.getChildren().addAll(pComboBoxObject, btnClear);
        btnClear.setOnAction(a -> pComboBoxObject.getSelectionModel().clearSelection());
    }

    private void setCombo() {
        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                pComboBoxObject.getSelectionModel().clearSelection();
            }
        });

        if (itemsList == null) {
            return;
        }

        pComboBoxObject.setItems(itemsList);
        bind();
    }


    private void bind() {
        if (selValueProperty == null) {
            return;
        }

        pComboBoxObject.valueProperty().bindBidirectional(selValueProperty);
    }

    private void unbind() {
        if (selValueProperty == null) {
            pComboBoxObject.getSelectionModel().clearSelection();
            return;
        }

        pComboBoxObject.valueProperty().unbindBidirectional(selValueProperty);
    }

}
