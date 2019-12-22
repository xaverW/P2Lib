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

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class PColumnConstraints {


    /**
     * sets the minimunsize
     *
     * @return
     */
    public static ColumnConstraints getCcPrefSize() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        return cc;
    }

    public static ColumnConstraints getCcPrefMaxSize(int maxWidth) {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        cc.setMaxWidth(maxWidth);
        return cc;
    }

    /**
     * sets the minimunsize
     *
     * @return
     */
    public static ColumnConstraints getCcMinSize(int minWidth) {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(minWidth);
        return cc;
    }

    /**
     * sets the minimunsize and hgrow
     *
     * @return
     */
    public static ColumnConstraints getCcComputedSizeAndHgrow() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setMinWidth(Region.USE_COMPUTED_SIZE);
        cc.setHgrow(Priority.ALWAYS);
        return cc;
    }

    public static RowConstraints getRcPrefSize() {
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.NEVER);
        return row;
    }

    public static RowConstraints getRcVgrow() {
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
        return row;
    }
}
