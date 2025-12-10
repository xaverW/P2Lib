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

import de.p2tools.p2lib.guitools.pcbo.P2CboObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class P2FilterComboBoxObject<E> extends HBox {


    private ObservableList<E> itemsList;
    private ObjectProperty<E> selValueProperty;
    private P2CboObject<E> p2CboObject = new P2CboObject<>();
    private Button btnClear = new Button("X");

    public P2FilterComboBoxObject() {
        super();
        addCss();
        initHBox();
    }

    public P2FilterComboBoxObject(ObservableList<E> itemsList, ObjectProperty<E> selValueProperty) {
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
        p2CboObject.setEditable(editable);
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
        p2CboObject.getSelectionModel().clearSelection();
    }

    public E getSelValue() {
        E s = p2CboObject.getSelectionModel().getSelectedItem();
        return s;
    }

    public ReadOnlyObjectProperty<E> getSelValueProperty() {
        ReadOnlyObjectProperty<E> s = p2CboObject.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement() {
        if (selValueProperty == null) {
            p2CboObject.getSelectionModel().clearSelection();
        } else {
            p2CboObject.setValue(selValueProperty.getValue());
        }
    }

    public void clearSelection() {
        p2CboObject.getSelectionModel().clearSelection();
    }

    private void initHBox() {
        p2CboObject.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(p2CboObject, Priority.ALWAYS);
        this.getChildren().addAll(p2CboObject, btnClear);
        btnClear.setOnAction(a -> p2CboObject.getSelectionModel().clearSelection());
    }

    private void setCombo() {
        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                p2CboObject.getSelectionModel().clearSelection();
            }
        });

        if (itemsList == null) {
            return;
        }

        p2CboObject.setItems(itemsList);
        bind();
    }


    private void bind() {
        if (selValueProperty == null) {
            return;
        }

        p2CboObject.valueProperty().bindBidirectional(selValueProperty);
    }

    private void unbind() {
        if (selValueProperty == null) {
            p2CboObject.getSelectionModel().clearSelection();
            return;
        }

        p2CboObject.valueProperty().unbindBidirectional(selValueProperty);
    }

}
