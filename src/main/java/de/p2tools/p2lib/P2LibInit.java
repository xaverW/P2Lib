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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class P2LibInit {

    public static void initLib(Stage primaryStage, String progName, String userAgent,
                               BooleanProperty darkMode, BooleanProperty blackWhite, boolean debug, boolean duration) {
        P2LibConst.primaryStage = primaryStage;
        P2LibConst.progName = progName;
        P2LibConst.userAgent = userAgent;
        P2LibConst.darkMode = darkMode == null ? new SimpleBooleanProperty(false) : darkMode;
        P2LibConst.blackWhite = blackWhite == null ? new SimpleBooleanProperty(false) : blackWhite;
        P2LibConst.debug = debug;
        P2LibConst.duration = duration;
    }

    public static void initProxy(boolean useProxy,
                                 String proxyHost, String proxyPort, String proxyUser, String proxyPwd) {
        P2LibConst.useProxy.setValue(useProxy);
        P2LibConst.proxyHost.setValue(proxyHost);
        P2LibConst.proxyPort.setValue(proxyPort);
        P2LibConst.proxyUser.setValue(proxyUser);
        P2LibConst.proxyPwd.setValue(proxyPwd);
    }

    public static void setPrimaryStage(Stage primaryStage) {
        P2LibConst.primaryStage = primaryStage;
    }

    public static void setStyleFile(String styleFile) {
        //z.B. für die Anpassung der Schriftgröße
        P2LibConst.styleFile = styleFile;
    }

    public static void addCssFile(String cssFile) {
        //ist für die css des Programms
        if (!P2LibConst.cssFileList.contains(cssFile)) {
            P2LibConst.cssFileList.add(cssFile);
        }
    }

    public static void removeCssFile(String cssFile) {
        P2LibConst.cssFileList.removeIf(cssF -> cssF.equals(cssFile));
    }

    public static void addP2CssToScene(Scene scene) {
        List<String> list = new ArrayList<>();
        list.add("de/p2tools/p2lib/p2Css_button.css");
        list.add("de/p2tools/p2lib/p2Css_maskerPane.css");
        list.add("de/p2tools/p2lib/p2Css_toggleSwitch.css");
        list.add("de/p2tools/p2lib/p2Css_p2Notify.css");
        list.add("de/p2tools/p2lib/p2Css_table.css");
        list.add("de/p2tools/p2lib/p2Css.css");
        list.add("de/p2tools/p2lib/p2Css_toolButton.css");
        list.add("de/p2tools/p2lib/p2Css_smallGui.css");
        list.add("de/p2tools/p2lib/p2Css_dialog.css");
        list.add("de/p2tools/p2lib/p2Css_gui.css");

        if (P2LibConst.darkMode.getValue()) {
            list.add("de/p2tools/p2lib/p2Css_dark.css");
            list.add("de/p2tools/p2lib/p2Css_darkTable.css");
        }

        //und dann noch die vom Programm
        list.addAll(P2LibConst.cssFileList);

        if (scene != null) {
            scene.getStylesheets().setAll(list);
        }
    }
}
