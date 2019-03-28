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


import javafx.scene.control.TableView;

import java.util.HashSet;
import java.util.List;

public class PTableViewTools {

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
            // damits auch gemeldet wird
            tableView.getSelectionModel().select(sel);
        }

    }
}
