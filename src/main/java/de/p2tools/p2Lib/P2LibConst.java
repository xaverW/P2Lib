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

import java.util.ArrayList;
import java.util.List;

public class P2LibConst {
    public static final String URL_WEBSITE_DONATE = "https://www.p2tools.de/donate.html";
    public static Stage primaryStage = null;
    public static String progName = "P2Tools";
    public static List<String> cssFileList = new ArrayList();
    public static final String CSS_GUI = "de/p2tools/p2Lib/p2-gui.css";
    public static final String CSS_GUI_DARK = "de/p2tools/p2Lib/p2-gui-dark.css";

    public static String userAgent = "";
    public static String logfile = "P2Tools_%g.log";
    public static String logfile_0 = "P2Tools_0.log";
    public static String logdir = "Log";
    public static String styleFile = "";

    public static int MIN_BUTTON_WIDTH = 100;

    public static boolean debug = false; //Debugmodus
    public static boolean duration = false; //Duration ausgeben

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String LINE_SEPARATORx2 = LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String LINE_SEPARATORx3 = LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String LINE_SEPARATORx4 = LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR;


}
