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
    private ObservableList<String> dataList = null;

    public PComboBox() {
        this.setEditable(true);
    }

    public void init(ObservableList<String> dataList, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.dataList = dataList;

        selectElement(stringProperty.getValueSafe());
        setCombo();
    }

    public void init(ObservableList<String> dataList, String init, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.dataList = dataList;

        selectElement(init);
        setCombo();
    }

    public void bindProperty(StringProperty stringProperty) {
        unbind();
        this.stringProperty = stringProperty;
        selectElement(stringProperty.getValueSafe());
        bind();
    }

    public void unbindProperty() {
        unbind();
        this.stringProperty = null;
        this.selectElement("");
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
        if (dataList == null) {
            return;
        }

        Collections.sort(dataList);
        this.setItems(dataList);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !dataList.contains(newValue)) {
                dataList.add(newValue);
            }
        });

        getEditor().setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                getEditor().setContextMenu(getMenu());
            }
        });

        dataList.addListener((ListChangeListener<String>) c -> {
                    if (dataList.size() == 1) {
                        getSelectionModel().selectFirst();
                    }
                }
        );

        bind();
        reduceList();
    }

    private void bind() {
        if (stringProperty == null) {
            return;
        }

//        valueProperty().bindBidirectional(stringProperty);
        stringProperty.bind(Bindings.createStringBinding(() ->
                        getSelectionModel().selectedItemProperty().isNull().get() ?
                                "" : getSelectionModel().selectedItemProperty().get(),
                getSelectionModel().selectedItemProperty()));
    }

    private void unbind() {
        if (stringProperty == null) {
            return;
        }

//        valueProperty().unbindBidirectional(stringProperty);
        stringProperty.unbind();
    }

    private void delList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        dataList.setAll(list);
        getSelectionModel().selectFirst();
    }

    private void reduceList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");

        dataList.stream().forEach(d -> {
            if (!list.contains(d) && list.size() < maxElements) {
                list.add(d);
            }
        });
        dataList.setAll(list);
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delEntrys = new MenuItem("Einträge löschen");
        delEntrys.setOnAction(a -> delList());
        contextMenu.getItems().addAll(delEntrys);
        return contextMenu;
    }
}
