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


package de.p2tools.p2lib.guitools.ptable;

import de.p2tools.p2lib.tools.file.P2FileSize;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class P2CellPFileSize<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<S, P2FileSize>, TableCell<S, P2FileSize>> cellFactory
            = (final TableColumn<S, P2FileSize> param) -> {

        final TableCell<S, P2FileSize> cell = new TableCell<>() {

            @Override
            public void updateItem(P2FileSize item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setGraphic(null);
                setText(item.getSizeStr());
            }
        };
        return cell;
    };
}
