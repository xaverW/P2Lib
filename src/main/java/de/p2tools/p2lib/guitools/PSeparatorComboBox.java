/*
 * P2Tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class PSeparatorComboBox<T> extends ComboBox<T> {
    public static final String SEPARATOR = "=====<>=====";
    public static final String SEPARATOR_OLD = "-----======-----";

    public PSeparatorComboBox() {
        super();
        setup();
    }

    public static boolean isSeparator(String str) {
        return str.equals(SEPARATOR) || str.equals(SEPARATOR_OLD);
    }

    private void setup() {
        SingleSelectionModel<T> model = new SingleSelectionModel<T>() {

            @Override
            public void select(T item) {
                if (PSeparatorComboBox.isSeparator(item.toString())) {
                    return;
                }
                super.select(item);
            }

            @Override
            public void select(int index) {
                final int itemCount = getItemCount();
                if (itemCount == 0 || index < 0 || index >= itemCount) return;

                T item = getItems().get(index);
                if (PSeparatorComboBox.isSeparator(item.toString())) {
                    return;
                }
                super.select(index);
            }

            @Override
            protected int getItemCount() {
                return getItems().size();
            }

            @Override
            protected T getModelItem(int index) {
                return getItems().get(index);
            }
        };

        Callback<ListView<T>, ListCell<T>> callback = new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                final ListCell<T> cell = new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.toString());
                            if (PSeparatorComboBox.isSeparator(item.toString())) {
                                setTextFill(Color.LIGHTGRAY);
                                setDisable(true);
                            } else {
                                setTextFill(Color.BLACK);
                                setDisable(false);
                            }
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        setSelectionModel(model);
        setCellFactory(callback);
    }
}