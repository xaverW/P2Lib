/*
 * MTViewer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.gui;

import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.css.P2CssFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProgStartBeforeGui {

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui() {
        initP2lib();

    }

    private static void initP2lib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAM_NAME, "",
                ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConfig.SYSTEM_DARK_THEME,
                ProgConfig.SYSTEM_BLACK_WHITE_ICON,
                ProgConfig.SYSTEM_ICON_COLOR,
                ProgConfig.SYSTEM_CSS_ADDER,

                new String[]{},
                new String[]{},

                new SimpleObjectProperty<>(P2CssFactory.CSS.CSS_2),
                null,

                new SimpleBooleanProperty(false),
                "", "",
                ProgData.debug, ProgData.duration);
    }
}
