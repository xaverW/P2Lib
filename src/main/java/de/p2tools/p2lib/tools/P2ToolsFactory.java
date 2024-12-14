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

package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import org.apache.commons.lang3.SystemUtils;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class P2ToolsFactory {

    private static final String VERSION = "version"; //ist der Name des Propertyfile: version.properties

    public enum OperatingSystemType {
        UNKNOWN(""),
        WIN("Windows"), WIN32("Windows"), WIN64("Windows"),
        LINUX("Linux"),
        MAC("Mac");
        private final String name;

        OperatingSystemType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    /**
     * Detect and return the currently used operating system.
     *
     * @return The enum for supported Operating Systems.
     */
    public static OperatingSystemType getOs() {
        OperatingSystemType os = OperatingSystemType.UNKNOWN;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (System.getenv("ProgramFiles(x86)") != null) {
                // win 64Bit
                os = OperatingSystemType.WIN64;
            } else if (System.getenv("ProgramFiles") != null) {
                // win 32Bit
                os = OperatingSystemType.WIN32;
            }

        } else if (SystemUtils.IS_OS_LINUX) {
            os = OperatingSystemType.LINUX;
        } else if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
            os = OperatingSystemType.LINUX;

        } else if (SystemUtils.IS_OS_MAC_OSX) {
            os = OperatingSystemType.MAC;
        }
        return os;
    }

    public static String getOsString() {
        return getOs().toString();
    }

    public static String getProgVersionString() {
        return " [Vers.: " + getProgVersion() + " - " + getBuildNo() + " ]";
    }

    public static String[] getJavaVersion() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Vendor:  " + System.getProperty("java.vendor"));
        list.add("VM-Name: " + System.getProperty("java.vm.name"));

        list.add("Version: " + System.getProperty("java.version"));
        list.add("         Feature: " + Runtime.version().feature() +
                "  Interim: " + Runtime.version().interim() +
                "  Update: " + Runtime.version().update() +
                "  Patch: " + Runtime.version().patch());
        list.add("         Runtimeversion: " + System.getProperty("java.runtime.version"));

        list.add("JavaFX:  " + System.getProperty("javafx.runtime.version"));
        return list.toArray(new String[0]);
    }

    public static String getBuildDate() {
        // Datum umdrehen
        // DATE=2024.12.14
        final String propToken = "DATE";
        String msg = "";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(propToken)) {
                msg = rb.getString(propToken);
                msg = P2LDateFactory.toString(P2LDateFactory.fromStringR(msg));
            }
        } catch (final Exception e) {
            P2Log.errorLog(807293847, e);
        }
        return msg;
    }

    public static String getBuildDateR() {
        // so ist es gespeichert
        // DATE=2024.12.14
        final String propToken = "DATE";
        String msg = "";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(propToken)) {
                msg = rb.getString(propToken);
            }
        } catch (final Exception e) {
            P2Log.errorLog(807293847, e);
        }
        return msg;
    }

    public static String getProgVersion() {
        // VERSION=17
        final String TOKEN_VERSION = "VERSION";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(TOKEN_VERSION)) {
                return rb.getString(TOKEN_VERSION);
            }
        } catch (final Exception e) {
            P2Log.errorLog(936251478, e);
        }
        return "";
    }

    public static int getProgVersionInt() {
        try {
            return Integer.parseInt(getProgVersion());
        } catch (final Exception e) {
            P2Log.errorLog(951203647, e);
        }
        return 0;
    }

    public static String getBuildNo() {
        // BUILD=15
        final String TOKEN_VERSION = "BUILD";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(TOKEN_VERSION)) {
                return rb.getString(TOKEN_VERSION);
            }
        } catch (final Exception e) {
            P2Log.errorLog(987012549, e);
        }
        return "0";
    }

    public static int getBuildNoInt() {
        try {
            return Integer.parseInt(getBuildNo());
        } catch (final Exception e) {
            P2Log.errorLog(951203647, e);
        }
        return 0;
    }
}
