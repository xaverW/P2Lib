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

package de.p2tools.p2Lib;

import javafx.stage.Stage;

public class PInit {

    public static void initLib(Stage primaryStage, String progName, String cssFile, String userAgent, boolean debug) {
        PConst.primaryStage = primaryStage;
        PConst.progName = progName;
        PConst.cssFile = cssFile;
        PConst.userAgent = userAgent;
        PConst.debug = debug;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        PConst.primaryStage = primaryStage;
    }

}
