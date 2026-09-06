/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.configfile.pdata.P2DataProgConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public class ProgConfig extends P2DataProgConfig {

    private static ProgConfig instance;

    private ProgConfig() {
        super("ProgConfig");
    }

    public static ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static BooleanProperty SYSTEM_BLACK_WHITE_ICON = addBoolProp("system-black-white-icon", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DARK_THEME = addBoolProp("system-dark-theme", Boolean.FALSE); // DARK oder LIGHT
    public static StringProperty SYSTEM_CSS_ADDER = addStrProp("system-css-adder");

    public static BooleanProperty SYSTEM_THEME_CHANGED = addBoolProp("system-theme-changed"); // hat sich geändert
    public static StringProperty SYSTEM_ICON_COLOR = addStrProp("system-icon-color", ProgConst.ICON_COLOR_LIGHT_1); // die aktuelle Icon-Farbe
}
