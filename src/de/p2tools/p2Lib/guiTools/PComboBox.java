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
    private StringProperty selValueStringProperty = null;
    private ObservableList<String> itemsList = null;

    public PComboBox() {
        this.setEditable(true);
    }

    public void init(ObservableList<String> dataList) {
        this.itemsList = dataList;

        setCombo();
    }

    public void init(ObservableList<String> dataList, StringProperty selValueStringProperty) {
        this.selValueStringProperty = selValueStringProperty;
        this.itemsList = dataList;

        selectElement(selValueStringProperty.getValueSafe());
        setCombo();
    }

    public void init(ObservableList<String> dataList, String init, StringProperty selValueStringProperty) {
        this.selValueStringProperty = selValueStringProperty;
        this.itemsList = dataList;

        selectElement(init);
        setCombo();
    }

    public void bindSelValueProperty(StringProperty stringProperty) {
        unbind();
        selValueStringProperty = stringProperty;
        bind();
    }

    public void unbindSelValueProperty() {
        unbind();
        this.selValueStringProperty = null;
        this.selectElement("");
    }

    public String getSelValue() {
        String s = this.getSelectionModel().getSelectedItem();
        if (s == null) {
            s = "";
        }
        return s;
    }

    public ReadOnlyObjectProperty<String> getSelValueProperty() {
        ReadOnlyObjectProperty<String> s = this.getSelectionModel().selectedItemProperty();
        return s;
    }

    public void selectElement(String element) {
        setValue(element);
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    private void setCombo() {
        if (itemsList == null) {
            return;
        }

        reduceList();
        Collections.sort(itemsList); // todo?? immer das zueltzt verwendete vorne???
        this.setItems(itemsList);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !itemsList.contains(newValue)) {
                // itemsList.add(newValue);
                // oder
                itemsList.add(0, newValue);
                setValue(newValue);
            }
        });

        getEditor().setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                getEditor().setContextMenu(getMenu());
            }
        });

        itemsList.addListener((ListChangeListener<String>) c -> {
                    if (itemsList.size() == 1) {
                        getSelectionModel().selectFirst(); // ist: ""
                    }
                }
        );

        bind();
    }

    private void bind() {
        if (selValueStringProperty == null) {
            return;
        }

        valueProperty().bindBidirectional(selValueStringProperty);
    }

    private void unbind() {
        if (selValueStringProperty == null) {
            setValue("");
            return;
        }

        valueProperty().unbindBidirectional(selValueStringProperty);
    }

    private void delList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        itemsList.setAll(list);
        getSelectionModel().selectFirst();
    }

    private void reduceList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");

        itemsList.stream().forEach(d -> {
            if (!list.contains(d) && list.size() < maxElements) {
                list.add(d);
            }
        });
        itemsList.setAll(list);
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delEntries = new MenuItem("Einträge löschen");
        delEntries.setOnAction(a -> delList());
        contextMenu.getItems().addAll(delEntries);
        return contextMenu;
    }
}
