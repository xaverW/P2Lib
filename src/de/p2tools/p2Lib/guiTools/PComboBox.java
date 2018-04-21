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

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Collections;

public class PComboBox extends ComboBox<String> {

    public final int MAX_ELEMENTS = 15;

    private int maxElements = MAX_ELEMENTS;
    private StringProperty stringProperty = null;
    private ObservableList<String> data = null;

    public PComboBox() {
        this.setEditable(true);
    }

    public void init(ObservableList<String> data, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.data = data;

        if (!getItems().contains(stringProperty.getValueSafe())) {
            getItems().add(stringProperty.getValueSafe());
        }
        getSelectionModel().select(stringProperty.getValueSafe());

        setCombo();
    }

    public void init(ObservableList<String> data, String init, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.data = data;

        if (!getItems().contains(init)) {
            getItems().add(init);
        }
        getSelectionModel().select(init);

        setCombo();
    }

    public String getSel() {
        String s = this.getSelectionModel().getSelectedItem();
        if (s == null) {
            s = "";
        }
        return s;
    }

    public ReadOnlyObjectProperty<String> getSelProperty() {
        ReadOnlyObjectProperty<String> s = this.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement(String element) {
        if (!getItems().contains(element)) {
            getItems().add(element);
        }
        getSelectionModel().select(element);
    }

    public void setEditAble(boolean editAble) {
        super.setEditable(editAble);
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    private void setCombo() {
        if (data == null || stringProperty == null) {
            return;
        }

        Collections.sort(data);
        setItems(data);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !data.contains(newValue)) {
                data.add(newValue);
            }
        });

        getEditor().setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                getEditor().setContextMenu(getMenu());
            }
        });

        data.addListener((ListChangeListener<String>) c -> {
                    if (data.size() == 1) {
                        getSelectionModel().selectFirst();
                    }
                }
        );

        stringProperty.bind(Bindings.createStringBinding(() ->
                        getSelectionModel().selectedItemProperty().isNull().get() ?
                                "" : getSelectionModel().selectedItemProperty().get(),
                getSelectionModel().selectedItemProperty()));


        reduceList();
    }

    private void delList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        data.setAll(list);
        getSelectionModel().selectFirst();
    }

    private void reduceList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");

        data.stream().forEach(d -> {
            if (!list.contains(d) && list.size() < maxElements) {
                list.add(d);
            }
        });
        data.setAll(list);
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delEntys = new MenuItem("Einträge löschen");
        delEntys.setOnAction(a -> delList());
        contextMenu.getItems().addAll(delEntys);
        return contextMenu;
    }
}
