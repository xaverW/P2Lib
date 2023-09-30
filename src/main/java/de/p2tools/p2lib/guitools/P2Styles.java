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

public class P2Styles {
    public static String PCOMBO_ERROR = " -fx-background-color: #ff0000, #ffbbbb;" +
            " -fx-background-insets: 0, 1;";

    public static String PTEXTFIELD_ERROR = " -fx-background-color: #ff0000, #ffbbbb;" +
            " -fx-background-insets: 0, 1;";

    public static String PTEXTFIELD_LABEL =
            "-fx-background-insets: 0, 1;" +
                    "-fx-background-color: -fx-text-box-border, -fx-background;";

    public static String PTEXTFIELD_LABEL_ERROR = " -fx-border-color: gray;" +
            " -fx-border-width: 1;" +
            PTEXTFIELD_ERROR;

}
