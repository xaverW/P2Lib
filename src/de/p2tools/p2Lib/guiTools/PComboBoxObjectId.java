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

import de.p2tools.p2Lib.configFile.pData.PDataId;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;


public class PComboBoxObjectId<E extends PDataId> extends ComboBox<E> {
    public static final long ID_NOT_SELECTED = -1;

    private ObservableList<E> itemsList;
    private ObjectProperty<E> selVaueProperty;
    private LongProperty idProperty;
    private ChangeListener<E> selValueListener = null;

    public PComboBoxObjectId() {
        super();
    }

    public PComboBoxObjectId(ObservableList<E> itemsList, LongProperty idProperty) {
        super();
        this.itemsList = itemsList;
        this.selVaueProperty = new SimpleObjectProperty<>();
        this.idProperty = idProperty;

        this.selVaueProperty.setValue(
                itemsList.stream().filter(data -> data.getId() == idProperty.get()).findFirst().orElse(null));

        selectElement();
        setCombo();
    }

    public void init(ObservableList<E> itemsList, LongProperty idProperty) {
        this.itemsList = itemsList;
        this.selVaueProperty = new SimpleObjectProperty<>();
        this.idProperty = idProperty;

        this.selVaueProperty.setValue(
                itemsList.stream().filter(data -> data.getId() == idProperty.get()).findFirst().orElse(null));

        selectElement();
        setCombo();
    }

    public E getSelValue() {
        E s = this.getSelectionModel().getSelectedItem();
        return s;
    }

    public void clearSelection() {
        this.getSelectionModel().clearSelection();
    }

    private void selectElement() {
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

        bind();
    }


    private void bind() {
        if (selVaueProperty == null) {
            return;
        }

        valueProperty().bindBidirectional(selVaueProperty);
        selValueListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                idProperty.setValue(newValue.getId());
            } else {
                idProperty.setValue(ID_NOT_SELECTED);
            }
        };

        selVaueProperty.addListener(selValueListener);
    }

    private void unbind() {

        if (selVaueProperty == null) {
            this.getSelectionModel().clearSelection();
            return;
        }

        valueProperty().unbindBidirectional(selVaueProperty);
        if (selValueListener != null) {
            selVaueProperty.removeListener(selValueListener);
        }

    }

}
