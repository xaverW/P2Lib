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

import de.p2tools.p2lib.tools.log.P2Log;

public class P2ShutDown {
    public static final String SHUT_DOWN_LINUX = "shutdown -h now";
    public static final String SHUT_DOWN_WINDOWS = "shutdown.exe -s -t 0";

    public static String getShutDownCommand() {
        String osName = System.getProperty("os.name");
        String shutdownCommand = "";
        if (osName.startsWith("Win")) {
            shutdownCommand = SHUT_DOWN_WINDOWS;

        } else if (osName.startsWith("Linux") || osName.startsWith("Mac")) {
            shutdownCommand = SHUT_DOWN_LINUX;

        } else {
            System.err.println("Shutdown unsupported operating system ...");
        }
        return shutdownCommand;
    }

    public static void shutDown(String shutDown) {
        if (shutDown == null || shutDown.isEmpty()) {
            //dann auf herkömmliche Art
            shutDown();

        } else {
            //dann übergebenen Befehl ausführen
            try {
                Runtime.getRuntime().exec(shutDown);
            } catch (Exception ex) {
                P2Log.errorLog(953696974, ex.getMessage());
            }
        }
    }

    public static void shutDown() {
        //Rechner mit Standardmethode herunterfahren
        String shutdownCommand = getShutDownCommand();
        if (!shutdownCommand.isEmpty()) {
            try {
                Runtime.getRuntime().exec(shutdownCommand);
            } catch (Exception ex) {
                P2Log.errorLog(457892014, ex.getMessage());
            }
        }
    }
}
