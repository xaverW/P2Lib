/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.log.P2Log;

import java.util.ArrayList;
import java.util.Arrays;

public class P2RuntimeExecPlay {
    private final String[] arrProgCallArray;

    public P2RuntimeExecPlay(String[] arrProgCallArray) {
        this.arrProgCallArray = arrProgCallArray;
    }

    //===================================
    // Public
    //===================================
    public void exec() {
        if (arrProgCallArray == null || arrProgCallArray.length == 0) {
            logMsg(null);
            return;
        }

        Process process = null;
        try {
            P2Log.sysLog("=====================");
            P2Log.sysLog("Starte Array: ");
            P2Log.sysLog(" -> " + Arrays.toString(arrProgCallArray));
            P2Log.sysLog("=====================");
            process = new ProcessBuilder(arrProgCallArray).inheritIO().start();
        } catch (final Exception ex) {
            P2Log.errorLog(915256325, ex, "Fehler beim Starten");
        }

        logMsg(process);
    }

    private void logMsg(Process process) {
        final ArrayList<String> list = new ArrayList<>();
        if (process != null) {
            // dann läuft er
            list.add("Film wurde gestartet");

        } else {
            // nicht gestartet
            list.add("Film konnte nicht gestartet werden");
            P2Alert.showErrorAlert("Film starten", "Kann den Film mit dem Aufruf nicht starten:" +
                    "\n" +
                    "\n" +
                    "------------------------------------------------" +
                    "\n" +
                    Arrays.toString(arrProgCallArray) +
                    "\n" +
                    "------------------------------------------------" +
                    "\n" +
                    "\n" +
                    "Bitte in den Programm-Menü\n" +
                    "die Einstellungen zum Abspielen von Filmen " +
                    "prüfen.");
        }
        list.add(P2Log.LILNE3);
        P2Log.sysLog(list.toArray(new String[0]));
    }
}
