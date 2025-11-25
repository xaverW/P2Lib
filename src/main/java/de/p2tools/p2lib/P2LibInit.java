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

import de.p2tools.p2lib.css.P2CssFactory;
import de.p2tools.p2lib.guitools.P2WindowIcon;
import de.p2tools.p2lib.mediathek.filter.Filter;
import javafx.beans.property.*;
import javafx.stage.Stage;

public class P2LibInit {

    public static void initLib(Stage stage, String progName, String userAgent,
                               BooleanProperty themeChanged,
                               BooleanProperty darkMode,
                               BooleanProperty blackWhite,

                               String[] cssFile,
                               String[] cssFileDark,
                               ObjectProperty<P2CssFactory.CSS> cssProp,
                               IntegerProperty fontSize,

                               BooleanProperty regExOnlyCompare,
                               String orgIcon /* de/p2tools/p2lib/icons/icon.png */,
                               String ownIcon /* /tmp/path/icon.png */,
                               boolean debug, boolean duration) {
        P2LibConst.primaryStage = stage;
        P2LibConst.progName = progName;
        P2LibConst.userAgent = userAgent;

        P2LibConst.themeChanged = themeChanged == null ? new SimpleBooleanProperty(false) : themeChanged; // ist eine Info
        P2LibConst.darkMode = darkMode == null ? new SimpleBooleanProperty(false) : darkMode;
        P2LibConst.blackWhite = blackWhite == null ? new SimpleBooleanProperty(false) : blackWhite;
        P2LibConst.cssFile = cssFile;
        P2LibConst.cssFileDark = cssFileDark;
        P2LibConst.cssProp = cssProp == null ? new SimpleObjectProperty<>(P2CssFactory.CSS.CSS_0) : cssProp;
        P2LibConst.fontSize = fontSize == null ? new SimpleIntegerProperty(0) : fontSize;

        if (regExOnlyCompare != null) {
            regExOnlyCompare.addListener((u, o, n) -> Filter.REG_EX_ONLY_CONTAIN = regExOnlyCompare.get());
        }

        P2LibConst.debug = debug;
        P2LibConst.duration = duration;

        P2WindowIcon.setOrgIcon(orgIcon);
        P2WindowIcon.setStageIcon(ownIcon);

        P2LibConst.cssProp.addListener((u, o, n) -> {
            P2CssFactory.addP2CssToScene(stage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
        P2LibConst.darkMode.addListener((u, o, n) -> {
            P2CssFactory.addP2CssToScene(stage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
        P2LibConst.blackWhite.addListener((u, o, n) -> {
            P2CssFactory.addP2CssToScene(stage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
        P2LibConst.fontSize.addListener((u, o, n) -> {
            P2CssFactory.addP2CssToScene(stage.getScene());
            P2LibConst.themeChanged.set(!P2LibConst.themeChanged.get());
        });
    }

    public static void setActStage(Stage stage) {
        P2LibConst.primaryStage = stage;
    }

    public static void initProxy(boolean useProxy, String proxyHost, String proxyPort,
                                 String proxyUser, String proxyPwd) {
        P2LibConst.useProxy.setValue(useProxy);
        P2LibConst.proxyHost.setValue(proxyHost);
        P2LibConst.proxyPort.setValue(proxyPort);
        P2LibConst.proxyUser.setValue(proxyUser);
        P2LibConst.proxyPwd.setValue(proxyPwd);
    }


}
