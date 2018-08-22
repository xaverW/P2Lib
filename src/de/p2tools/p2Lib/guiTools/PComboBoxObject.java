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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;

public class PComboBoxObject<E> extends ComboBox<E> {


    private ObservableList<E> itemsList;
    private ObjectProperty<E> selVaueProperty;

    public PComboBoxObject() {
        super();
    }

    public PComboBoxObject(ObservableList<E> itemsList, ObjectProperty<E> selVaueProperty) {
        super();
        this.itemsList = itemsList;
        this.selVaueProperty = selVaueProperty;
        setCombo();
    }

    public void init(ObservableList<E> itemsList) {
        this.itemsList = itemsList;

        selectElement();
        setCombo();
    }

    public void init(ObservableList<E> itemsList, ObjectProperty<E> selVaueProperty) {
        this.itemsList = itemsList;
        this.selVaueProperty = selVaueProperty;

        selectElement();
        setCombo();
    }

    public void bindSelValueProperty(ObjectProperty<E> stringProperty) {
        unbind();
        selVaueProperty = stringProperty;
        bind();
    }

    public void unbindSelValueProperty() {
        unbind();
        this.selVaueProperty = null;
        this.getSelectionModel().clearSelection();
    }

    public E getSelValue() {
        E s = this.getSelectionModel().getSelectedItem();
        return s;
    }

    public ReadOnlyObjectProperty<E> getSelValueProperty() {
        ReadOnlyObjectProperty<E> s = this.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement() {
        if (selVaueProperty == null) {
            this.getSelectionModel().clearSelection();
        } else {
            this.setValue(selVaueProperty.getValue());
        }
    }

    private void setCombo() {
        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                this.getSelectionModel().clearSelection();
            }
        });
        
        if (itemsList == null) {
            return;
        }

        this.setItems(itemsList);

//        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null && !itemsList.contains(newValue)) {
//                // itemsList.add(newValue);
//                // oder
//                itemsList.add(1, newValue);
//                setValue(newValue);
//            }
//        });
//
//        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
////            if (newValue != null && !itemsList.contains(newValue)) {
////                itemsList.add(0, newValue); todo da landen sonst alle Tippser in der Liste ??
////                setValue(newValue);
////            }
//            check();
//        });
//
//        getEditor().setOnMousePressed(m -> {
//            if (m.getButton().equals(MouseButton.SECONDARY)) {
//                getEditor().setContextMenu(getMenu());
//            }
//        });

//        itemsList.addListener((ListChangeListener<E>) c -> {
//                    if (itemsList.size() == 1) {
//                        getSelectionModel().selectFirst(); // ist: ""
//                    }
//                }
//        );

        bind();
    }


    private void bind() {
        if (selVaueProperty == null) {
            return;
        }

        valueProperty().bindBidirectional(selVaueProperty);
    }

    private void unbind() {
        if (selVaueProperty == null) {
            this.getSelectionModel().clearSelection();
            return;
        }

        valueProperty().unbindBidirectional(selVaueProperty);
    }

}
