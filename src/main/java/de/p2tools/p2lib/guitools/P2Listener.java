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

import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EventListener;


public class P2Listener implements EventListener {

    private static final ArrayList<P2Listener> P_2_LISTENERS = new ArrayList<>();
    static int count = 0;
    public static final int EVENT_TIMER = count++;

    public int[] event = {-1};
    public String eventClass = "";

    public P2Listener(int event, String eventClass) {
        this.event = new int[]{event};
        this.eventClass = eventClass;
    }

    public P2Listener(int[] event, String eventClass) {
        this.event = event;
        this.eventClass = eventClass;
    }

    public static synchronized void addListener(P2Listener p2Listener) {
        P2Log.debugLog("Anz. Listener: " + P_2_LISTENERS.size());
        P_2_LISTENERS.add(p2Listener);
    }

    public static synchronized void removeListener(P2Listener p2Listener) {
        P_2_LISTENERS.remove(p2Listener);
    }

    public static synchronized void notify(int eventNotify, String eventClass) {
        P_2_LISTENERS.stream().forEach(p2Listener -> {
            for (final int event : p2Listener.event) {
                // um einen Kreislauf zu verhindern
                if (event == eventNotify && !p2Listener.eventClass.equals(eventClass)) {
                    p2Listener.pingen();
                }
            }
        });
    }

    public void pingFx() {
        // das passiert im application thread
    }

    public void ping() {
        // das ist asynchron zum application thread
    }

    private void pingen() {
        try {
            ping();
            Platform.runLater(() -> pingFx());
        } catch (final Exception ex) {
            P2Log.errorLog(945120973, ex);
        }
    }
}
