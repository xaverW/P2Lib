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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class P2LibInit {

    public static void initLib(Stage primaryStage, String progName, String userAgent,
                               BooleanProperty darkMode, BooleanProperty blackWhite, BooleanProperty themeChanged,
                               String cssFile, String cssFileDark, IntegerProperty fontSize,
                               boolean debug, boolean duration) {
        P2LibConst.primaryStage = primaryStage;
        P2LibConst.progName = progName;
        P2LibConst.userAgent = userAgent;

        P2LibConst.darkMode = darkMode == null ? new SimpleBooleanProperty(false) : darkMode;
        P2LibConst.blackWhite = blackWhite == null ? new SimpleBooleanProperty(false) : blackWhite;
        P2LibConst.fontSize = fontSize == null ? new SimpleIntegerProperty(0) : fontSize;
        P2LibConst.themeChanged = themeChanged == null ? new SimpleBooleanProperty(false) : themeChanged; // ist eine Info

        P2LibConst.cssFile = cssFile;
        P2LibConst.cssFileDark = cssFileDark;

        P2LibConst.debug = debug;
        P2LibConst.duration = duration;

        P2LibConst.darkMode.addListener((u, o, n) -> {
            addP2CssToScene(primaryStage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
        P2LibConst.blackWhite.addListener((u, o, n) -> {
            addP2CssToScene(primaryStage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
        P2LibConst.fontSize.addListener((u, o, n) -> {
            addP2CssToScene(primaryStage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
    }

    public static void initProxy(boolean useProxy, String proxyHost, String proxyPort,
                                 String proxyUser, String proxyPwd) {
        P2LibConst.useProxy.setValue(useProxy);
        P2LibConst.proxyHost.setValue(proxyHost);
        P2LibConst.proxyPort.setValue(proxyPort);
        P2LibConst.proxyUser.setValue(proxyUser);
        P2LibConst.proxyPwd.setValue(proxyPwd);
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

        if (P2LibConst.blackWhite.getValue()) {
            if (P2LibConst.darkMode.getValue()) {
                list.add("de/p2tools/p2lib/p2Css_bw_b.css");
            } else {
                list.add("de/p2tools/p2lib/p2Css_bw_w.css");
            }
        }

        // und die vom Programm
        list.add(P2LibConst.cssFile);
        if (P2LibConst.darkMode.getValue()) {
            list.add(P2LibConst.cssFileDark);
        }

        if (scene != null) {
            scene.getStylesheets().setAll(list);

            // entweder Größe oder 0 zum löschen
            scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
        }
    }
}
