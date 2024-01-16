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

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class P2ColumnConstraints {


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

    /**
     * sets the minimunsize
     *
     * @return
     */
    public static ColumnConstraints getCcPrefSizeCenter() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        cc.setHalignment(HPos.CENTER);
        return cc;
    }

    /**
     * sets the minimunsize
     *
     * @return
     */
    public static ColumnConstraints getCcPrefSizeLeft() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        cc.setHalignment(HPos.LEFT);
        return cc;
    }

    public static ColumnConstraints getCcPrefSizeRight() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        cc.setHalignment(HPos.RIGHT);
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

    /**
     * sets the minimunsize and hgrow
     *
     * @return
     */
    public static ColumnConstraints getCcComputedSizeAndHgrowRight() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setMinWidth(Region.USE_COMPUTED_SIZE);
        cc.setHgrow(Priority.ALWAYS);
        cc.setHalignment(HPos.RIGHT);
        return cc;
    }

    public static ColumnConstraints getCcComputedSizeAndHgrowCenter() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setMinWidth(Region.USE_COMPUTED_SIZE);
        cc.setHgrow(Priority.ALWAYS);
        cc.setHalignment(HPos.CENTER);
        return cc;
    }

    public static RowConstraints getRcPrefSizeTop() {
        RowConstraints row = new RowConstraints();
        row.setValignment(VPos.TOP);
        row.setVgrow(Priority.NEVER);
        return row;
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
