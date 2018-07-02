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

public class PColumnConstraints {

    public static ColumnConstraints getCcLabelSize() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        return cc;
    }

    public static ColumnConstraints getCcPrefSize() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        return cc;
    }

    public static ColumnConstraints getCcComputedSize() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setMinWidth(Region.USE_COMPUTED_SIZE);
        cc.setHgrow(Priority.ALWAYS);
        return cc;
    }
}
