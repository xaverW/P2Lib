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

import java.util.Iterator;

public class PInit {

    public static void initLib(Stage primaryStage, String progName, String cssFile, String userAgent, boolean debug, boolean duration) {
        PConst.primaryStage = primaryStage;
        PConst.progName = progName;
        PConst.cssFile.add(cssFile);
        PConst.userAgent = userAgent;
        PConst.debug = debug;
        PConst.duration = duration;
    }

    public static void addCssFile(String cssFile) {
        boolean found = false;
        for (String s : PConst.cssFile) {
            if (s.equals(cssFile)) {
                found = true;
            }
        }
        if (!found) {
            PConst.cssFile.add(cssFile);
        }
    }

    public static void removeCssFile(String cssFile) {
        Iterator<String> it = PConst.cssFile.listIterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals(cssFile)) {
                it.remove();
            }
        }
    }

    public static void setPrimaryStage(Stage primaryStage) {
        PConst.primaryStage = primaryStage;
    }

    public static void addP2LibCss(Scene scene) {
        scene.getStylesheets().addAll("de/p2tools/p2Lib/p2Lib.css");

        scene.getStylesheets().add("de/p2tools/p2Lib/pCheckComboBox.css");
        scene.getStylesheets().add("de/p2tools/p2Lib/pMaskerPane.css");
        scene.getStylesheets().add("de/p2tools/p2Lib/pRangeBox.css");
        scene.getStylesheets().add("de/p2tools/p2Lib/pToggleSwitch.css");

        for (String s : PConst.cssFile) {
            scene.getStylesheets().addAll(s);
        }
    }
}
