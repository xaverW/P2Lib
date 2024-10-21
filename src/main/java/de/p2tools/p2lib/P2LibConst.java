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

package de.p2tools.p2lib;

import javafx.beans.property.*;
import javafx.stage.Stage;

public class P2LibConst {
    //sind die Einstellungen des Programms

    public static boolean debug = false; //Debugmodus
    public static boolean duration = false; //Duration ausgeben

    public static final String MAIL_XAVER = "w.xaver@googlemail.com";
    public static final String URL_WEBSITE_DONATE = "https://www.p2tools.de/donate/";
    public static final String URL_WEBSITE_FORUM = "https://p2forum.de";
    public static final String CONFIG_XML_START = "P2Tools";

    public static Stage primaryStage = null;
    public static Stage actStage = null;
    public static String progName = "P2Tools";
    public static String userAgent = "";

    public static String logDir = "Log";
    public static String logFile = "P2Tools_%g.log";
    public static String logFile_0 = "P2Tools_0.log";

    public static BooleanProperty darkMode = new SimpleBooleanProperty(false);
    public static BooleanProperty blackWhite = new SimpleBooleanProperty(false);
    public static IntegerProperty fontSize = new SimpleIntegerProperty(0); // für die Anpassung der Schriftgröße
    public static BooleanProperty themeChanged = new SimpleBooleanProperty(false); // eine Info nach Änderungen
    public static String cssFile = "";
    public static String cssFileDark = "";

    public static BooleanProperty useProxy = new SimpleBooleanProperty(false);
    public static StringProperty proxyHost = new SimpleStringProperty("");
    public static StringProperty proxyPort = new SimpleStringProperty("");
    public static StringProperty proxyUser = new SimpleStringProperty("");
    public static StringProperty proxyPwd = new SimpleStringProperty("");

    //Projektweite Abstände, ...
    public static final int NUMBER_NOT_STARTED = Integer.MAX_VALUE;
    public static final int NUMBER_NULL = 0;
    public static final int NUMBER_ALL_OR_MIN = 0;

    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String LINE_SEPARATORx2 = LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String LINE_SEPARATORx3 = LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String LINE_SEPARATORx4 = LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR;

    public final static int WINDOW_ICON_WIDTH = 58;
    public final static int WINDOW_ICON_HEIGHT = 58;
    public final static int MIN_BUTTON_WIDTH = 100;
    public final static int DIST_BUTTON = 5;//Abstand zwischen den Button
    public final static int DIST_BUTTON_BLOCK = 10;//Abstand zwischen den Button-Blöcken
    public final static int DIST_BUTTON_BLOCK_BIG = 20;//Abstand zwischen den Button-Blöcken

    public final static int DIST_GRIDPANE_HGAP = 5;
    public final static int DIST_GRIDPANE_VGAP = 5;
    public final static int PADDING_GRIDPANE = 10;
    public final static int PADDING_GRIDPANE_SMALL = 5;

    public final static int PADDING = 10;//Abstand zum Rand
    public final static int PADDING__SMALL = 5;//Abstand zum Rand

    public final static int SPACING_VBOX = 10;
    public final static int SPACING_VBOX_SMALL = 5;
    public final static int SPACING_HBOX = 10;
    public final static int SPACING_HBOX_SMALL = 5;

    public final static int PADDING_VBOX = 10;//Abstand zum Rand
    public final static int PADDING_VBOX_SMALL = 5;//Abstand zum Rand
    public final static int PADDING_HBOX = 10;//Abstand zum Rand
    public final static int PADDING_HBOX_SMALL = 5;//Abstand zum Rand
}
