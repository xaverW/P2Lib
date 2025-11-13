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

import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDateTime;

public class P2CellLocalDateTimeSecond<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<S, LocalDateTime>, TableCell<S, LocalDateTime>> cellFactory
            = (final TableColumn<S, LocalDateTime> param) -> {

        final TableCell<S, LocalDateTime> cell = new TableCell<>() {

            @Override
            public void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setGraphic(null);
                String date = P2LDateTimeFactory.toStringDate(item);
                String time = P2LDateTimeFactory.toStringTimeSecond(item);
                setText(date + (time.isEmpty() ? "" : "\n" + time));
            }
        };
        return cell;
    };
}
