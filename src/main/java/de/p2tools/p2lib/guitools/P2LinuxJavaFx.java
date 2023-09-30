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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.PLog;

import java.awt.*;

public class P2LinuxJavaFx {
    private static final String X11_AWT_APP_CLASS_NAME = "awtAppClassName";
    private static final String JAVAFX_CLASSNAME_APPLICATION_PLATFORM = "javafx.application.Platform";
    private static final String ERROR_NO_JAVAFX_INSTALLED = "JavaFX wurde nicht im Klassenpfad gefunden. " + P2LibConst.LINE_SEPARATOR +
            "Stellen Sie sicher, dass Sie ein Java JRE ab Version 8 benutzen. " + P2LibConst.LINE_SEPARATOR +
            "Falls Sie Linux nutzen, installieren Sie das openjfx-Paket ihres " + P2LibConst.LINE_SEPARATOR +
            "Package-Managers, oder nutzen Sie eine eigene JRE-Installation.";
    private static final String TEXT_LINE = "===========================================";

    /**
     * Tests if javafx is in the classpath by loading a well known class.
     */
    public static boolean hasJavaFx() {
        try {
            Class.forName(JAVAFX_CLASSNAME_APPLICATION_PLATFORM);
            return true;

        } catch (final ClassNotFoundException e) {
            PLog.errorLog(487651240, new String[]{TEXT_LINE, ERROR_NO_JAVAFX_INSTALLED, TEXT_LINE});
            return false;
        }
    }

    /**
     * Setup the X11 window manager WM_CLASS hint. Enables e.g. GNOME to determine application name
     * and to enable app specific functionality.
     */
    public static void setupX11WindowManagerClassName(String progName) {
        //WARNING: An illegal reflective access operation has occurred
        //todo braucht gnome das noch???
        try {
            final Toolkit xToolkit = Toolkit.getDefaultToolkit();
            final java.lang.reflect.Field awtAppClassNameField = xToolkit.getClass().getDeclaredField(X11_AWT_APP_CLASS_NAME);
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(xToolkit, progName);
        } catch (final Exception ignored) {
            System.err.println("Couldn't set awtAppClassName");
        }
    }

//    WARNING: An illegal reflective access operation has occurred
//    WARNING: Illegal reflective access by LinuxJavaFx (file:/home/emil/daten/software/javaFx/MTInfo/MTInfo/dist/MTInfo.jar) to field sun.awt.X11.XToolkit.awtAppClassName
//    WARNING: Please consider reporting this to the maintainers of LinuxJavaFx
//    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
//    WARNING: All illegal access operations will be denied in a future release
}
