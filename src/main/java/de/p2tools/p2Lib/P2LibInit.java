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

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class P2LibInit {

    public static void initLib(Stage primaryStage, String progName, String cssFile, String userAgent, boolean debug, boolean duration) {
        P2LibConst.primaryStage = primaryStage;
        P2LibConst.progName = progName;
        addCssFile(cssFile);
        P2LibConst.userAgent = userAgent;
        P2LibConst.debug = debug;
        P2LibConst.duration = duration;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        P2LibConst.primaryStage = primaryStage;
    }

    public static void addCssFile(String cssFile) {
        if (!P2LibConst.cssFileList.contains(cssFile)) {
            P2LibConst.cssFileList.add(cssFile);
        }
    }

    public static void removeCssFile(String cssFile) {
        P2LibConst.cssFileList.removeIf(cssF -> cssF.equals(cssFile));
    }

    public static void xaddP2LibCssToScene(Scene scene) {
        List<String> list = new ArrayList<>();
        list.add("de/p2tools/p2Lib/p2Lib.css");
        list.add("de/p2tools/p2Lib/pMaskerPane.css");
        list.add("de/p2tools/p2Lib/pToggleSwitch.css");
        list.add("de/p2tools/p2Lib/notifier.css");
        for (String s : P2LibConst.cssFileList) {
            list.add(s);
        }
        scene.getStylesheets().setAll(list);
    }
}
