/*
 * P2Tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.tools.log.PLog;

public class PShutDown {

    public static void shutDown() {
        String osName = System.getProperty("os.name");
        String shutdownCommand = "";

        if (osName.startsWith("Win")) {
            shutdownCommand = "shutdown.exe -s -t 0";

        } else if (osName.startsWith("Linux") || osName.startsWith("Mac")) {
            shutdownCommand = "shutdown -h now";

        } else {
            System.err.println("Shutdown unsupported operating system ...");
        }

        if (!shutdownCommand.isEmpty()) {
            try {
                Runtime.getRuntime().exec(shutdownCommand);
            } catch (Exception ex) {
                PLog.errorLog(457892014, ex.getMessage());
            }
        }
    }
}
