/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.P2ProgIcons;
import de.p2tools.p2lib.mtfilter.FilterCheckRegEx;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.function.BooleanSupplier;

public class P2CboString extends ComboBox<P2CboString.P2CboLabel> {
    public static final int MAX_FILTER_HISTORY = 15;
    private final ObservableList<String> storedFilterList;
    private final ObservableList<P2CboLabel> itemList = FXCollections.observableArrayList(new P2CboLabel(""));

    private final StringProperty strSearchProperty;
    private final BooleanSupplier doSomething; // Funktion, was bei Auswahl gemacht werden soll, Rückgabewert wird nicht gebraucht

    public P2CboString(ObservableList<String> storedFilterList, StringProperty strSearchProperty) {
        this.storedFilterList = storedFilterList;
        this.strSearchProperty = strSearchProperty;
        this.doSomething = () -> true;
        start();
    }

    public P2CboString(ObservableList<String> storedFilterList,
                       StringProperty strSearchProperty, BooleanSupplier doSomething) {
        this.storedFilterList = storedFilterList;
        this.strSearchProperty = strSearchProperty;
        this.doSomething = doSomething;
        start();
    }

    public void addValue(String add) {
        if (add.isEmpty()) {
            return;
        }

        getEditor().setText(add);
    }

    private void start() {
        storedFilterList.forEach(s -> {
            if (!s.isEmpty()) {
                P2CboLabel tf = itemList.stream().filter(pCboSearchLabel ->
                        pCboSearchLabel.getText().equals(s)).findFirst().orElse(null);
                if (tf == null) {
                    // sonst ist er schon drin
                    itemList.add(new P2CboLabel(s));
                }
            }
        });
        setItems(itemList);
        fillStoredList(); // zum Putzen, hatte den Fehler zu wachsen

        setEditable(true);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setVisibleRowCount(MAX_FILTER_HISTORY);
        init();
    }

    private void init() {
        FilterCheckRegEx regEx = new FilterCheckRegEx(getEditor());
        regEx.checkPattern();

        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (!this.isShowing()) {
                // dann ist's eine Auswahl aus der Combo
                addLastFilter(newValue);
            }
            strSearchProperty.setValue(getEditor().getText());
        });
        getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<Object>) (observable, oldValue, newValue) -> {
                    // kann auch ein String!!!! sein
//                    if (ProgConfig.SYSTEM_FILTER_RETURN.getValue()) { // nuss nicht sein
                    // dann melden
                    if (this.isShowing() ||
                            newValue != null &&
                                    newValue.getClass().equals(P2CboLabel.class) &&
                                    !strSearchProperty.getValueSafe().equals(((P2CboLabel) newValue).getText())) {
                        doSomething.getAsBoolean();
                    }
//                    }
                });

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doSomething.getAsBoolean();
            }
        });
        strSearchProperty.addListener((u, o, n) -> getEditor().setText(strSearchProperty.getValue()));
        getEditor().setText(strSearchProperty.getValue());

        setCellFactory(cell -> new ListCell<>() {
            final Button btnDel = new Button();
            final HBox hBox = new HBox();
            final Label lblFilter = new Label();

            {
                btnDel.setGraphic(P2ProgIcons.ICON_BUTTON_DEL_SW.getImageView());
                btnDel.getStyleClass().add("buttonVerySmall");
                btnDel.visibleProperty().bind(Bindings.size(itemList).greaterThan(1));

                hBox.setPadding(new Insets(0));
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(5);
                hBox.getChildren().addAll(btnDel, lblFilter);
            }

            @Override
            protected void updateItem(P2CboLabel searchLabel, boolean empty) {
                super.updateItem(searchLabel, empty);
                setVisibleRowCount(8);
                setVisibleRowCount(10);

                if (!empty && searchLabel != null) {
                    btnDel.setOnMousePressed(m -> {
                        if (searchLabel.getText().isEmpty()) {
                            // dann ist's der erste
                            if (itemList.size() > 1) {
                                itemList.remove(1, itemList.size());
                            }
                        } else {
                            itemList.remove(searchLabel);
                        }
                        getSelectionModel().select(0);
                        fillStoredList();
                    });
                    // lblFilter.setText(searchLabel.getText());
                    lblFilter.textProperty().bind(searchLabel.textProperty());
                    setGraphic(hBox);
                } else {
                    // Nothing to display here
                    setGraphic(null);
                }
            }
        });
    }

    private synchronized void addLastFilter(String addF) {
        // einen neuen Filter einfügen
        getSelectionModel().clearSelection();

        P2CboLabel addFilter = new P2CboLabel(addF);
        if (addFilter.getText().isEmpty()) {
            return;
        }

        if (getItems().size() <= 1) {
            // dann ist's der erste
            getItems().add(addFilter);
            return;
        }

        final P2CboLabel tmp = getItems().get(1);
        if (addF.contains(tmp.getText())) {
            // dann wird der erste damit ersetzt
            tmp.setText(addF);
        }

        P2CboLabel tf = itemList.stream().filter(pCboSearchLabel ->
                pCboSearchLabel.getText().equals(addF)).findFirst().orElse(null);

        if (tf == null) {
            // dann ist er nicht drin und kommt an Stelle 1
            while (itemList.size() > MAX_FILTER_HISTORY) {
                itemList.remove(itemList.size() - 1);
            }
            itemList.add(1, addFilter);

        } else {
            // dann nach vorne ziehen
            itemList.remove(tf);
            itemList.add(1, tf);
        }

        fillStoredList();
    }

    private void fillStoredList() {
        storedFilterList.clear();
        itemList.forEach(s -> {
            storedFilterList.add(s.toString());
        });
    }

    static class P2CboLabel extends Label implements Comparable<P2CboLabel> {

        public P2CboLabel(String value) {
            setText(value);
        }

        @Override
        public String toString() {
            return getText();
        }

        @Override
        public int compareTo(P2CboLabel arg0) {
            return getText().compareTo(arg0.getText());
        }
    }
}
