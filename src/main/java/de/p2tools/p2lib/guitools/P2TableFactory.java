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


package de.p2tools.p2lib.guitools;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;

public class P2TableFactory {

    public static final KeyCombination SPACE = new KeyCodeCombination(KeyCode.SPACE);
    public static final KeyCombination SPACE_SHIFT = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.SHIFT_DOWN);

    public static void refreshTable(TableView table) {
        for (int i = 0; i < table.getColumns().size(); i++) {
            if (((TableColumn) (table.getColumns().get(i))).isVisible()) {
                ((TableColumn) (table.getColumns().get(i))).setVisible(false);
                ((TableColumn) (table.getColumns().get(i))).setVisible(true);
            }
        }
    }

    public static void scrollVisibleRangeUp(TableView table) {
        if (table.getItems().size() == 0) {
            //leere Tabelle :)
            return;
        }

        if (table.getSelectionModel().getSelectedIndex() == 0) {
            //dann ist die erste Zeile markiert
            final int i = table.getItems().size() - 1;
            table.getSelectionModel().clearAndSelect(i);
            table.scrollTo(i);
            return;
        }

        TableViewSkin<?> skin = (TableViewSkin) table.getSkin();
        if (skin == null) {
            return;
        }

        int[] range = getVisibleRange(table);
        int count = range[1] - range[0];
        int n = range[0] + 2 - count;
        if (n < 0) {
            n = 0;
        }
        if (count >= 0 && n < table.getItems().size()) {
            table.getSelectionModel().clearAndSelect(n);
            table.scrollTo(n);
        }
    }

    public static void scrollVisibleRangeDown(TableView table) {
        if (table.getItems().size() == 0) {
            //leere Tabelle :)
            return;
        }

        if (table.getSelectionModel().getSelectedIndex() == table.getItems().size() - 1) {
            //dann ist die letzte Zeile markiert
            table.getSelectionModel().clearAndSelect(0);
            table.scrollTo(0);
            return;
        }

        TableViewSkin<?> skin = (TableViewSkin) table.getSkin();
        if (skin == null) {
            return;
        }

        int[] range = getVisibleRange(table);
        int count = range[1] - range[0];
        int n = range[0] + count;
        if (count >= 0 && n < table.getItems().size()) {
            table.getSelectionModel().clearAndSelect(n);
            table.scrollTo(n);
        }
    }

    public static int[] getVisibleRange(TableView table) {
        TableViewSkin<?> skin = (TableViewSkin) table.getSkin();
        if (skin == null) {
            return new int[]{0, 0};
        }

        VirtualFlow<?> flow = (VirtualFlow) skin.getChildren().get(1);
        int indexFirst;
        int indexLast;
        if (flow != null && flow.getFirstVisibleCell() != null
                && flow.getLastVisibleCell() != null) {
            indexFirst = flow.getFirstVisibleCell().getIndex();
            if (indexFirst >= table.getItems().size())
                indexFirst = table.getItems().size() - 1;

            indexLast = flow.getLastVisibleCell().getIndex();
            if (indexLast >= table.getItems().size())
                indexLast = table.getItems().size() - 1;

        } else {
            indexFirst = 0;
            indexLast = 0;
        }

        return new int[]{indexFirst, indexLast};
    }

    public static void selectNextRow(TableView tableView) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow < 0) {
            tableView.getSelectionModel().clearAndSelect(0);
            tableView.scrollTo(0);

        } else if (selectedTableRow >= 0 && selectedTableRow < tableView.getItems().size() - 1) {
            tableView.getSelectionModel().clearAndSelect(selectedTableRow + 1);
            tableView.scrollTo(selectedTableRow + 1);

        } else if (selectedTableRow == tableView.getItems().size() - 1) {
            tableView.getSelectionModel().clearAndSelect(0);
            tableView.scrollTo(0);
        }
    }

    public static void selectPreviousRow(TableView tableView) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow < 0) {
            tableView.getSelectionModel().clearAndSelect(0);
            tableView.scrollTo(0);

        } else if (selectedTableRow == 0) {
            tableView.getSelectionModel().clearAndSelect(tableView.getItems().size() - 1);
            tableView.scrollTo(tableView.getItems().size() - 1);

        } else if (selectedTableRow <= tableView.getItems().size() - 1) {
            tableView.getSelectionModel().clearAndSelect(selectedTableRow - 1);
            tableView.scrollTo(selectedTableRow - 1);
        }
    }

    public static void invertSelection_(TableView tableView) {
        for (int i = 0; i < tableView.getItems().size(); ++i) {
            if (tableView.getSelectionModel().isSelected(i)) {
                tableView.getSelectionModel().clearSelection(i);
            } else {
                tableView.getSelectionModel().select(i);
            }
        }
    }

    public static void invertSelection(TableView tableView) {
        final List<Integer> selArr = tableView.getSelectionModel().getSelectedIndices();
        final HashSet<Integer> selHash = new HashSet<>(selArr);

        tableView.getSelectionModel().selectAll();
        int sum = tableView.getItems().size();

        int sel = -1;
        for (int i = 0; i < sum; ++i) {
            if (selHash.contains(i)) {
                tableView.getSelectionModel().clearSelection(i);
            } else if (sel < 0) {
                sel = i;
            }
        }

        if (sel >= 0) {
            //damits auch gemeldet wird
            tableView.getSelectionModel().select(sel);
        }

    }

    public static <S> void addAutoScroll(final TableView<S> view) {
        if (view == null) {
            throw new NullPointerException();
        }
        ObservableList<S> list = view.getItems();
        list.addListener((ListChangeListener<S>) (c -> {
            c.next();
            final int size = view.getItems().size();
            if (size > 0 && c.wasAdded()) {
                S element = list.get(c.getFrom());

                view.getSelectionModel().clearSelection();
                view.scrollTo(element);
                view.getSelectionModel().select(element);
            }
        }));
    }

    static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public static class PCellMoney<A, V> extends TableCell<A, V> {
        boolean showNull = true;

        public PCellMoney() {
            setAlignment(Pos.CENTER_RIGHT);
            setPadding(new Insets(0, 5, 0, 0));
        }

        public PCellMoney(boolean showNull) {
            this.showNull = showNull;
            setAlignment(Pos.CENTER_RIGHT);
            setPadding(new Insets(0, 5, 0, 0));
        }

        @Override
        protected void updateItem(V value, boolean empty) {
            super.updateItem(value, empty);
            if (empty) {
                setText(null);
            } else {
                if (value.getClass().equals(Long.class)) {
                    double d = (Long) value;
                    if (!showNull && d == 0) {
                        setText("");
                    } else {
                        setText(currencyFormat.format(d / 100));
                    }
                } else {
                    setText(currencyFormat.format(value)); //todo
                }
            }
        }
    }
}
