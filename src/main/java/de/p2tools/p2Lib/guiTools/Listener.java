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

package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.EventListener;


public class Listener implements EventListener {

    static int count = 0;

    public static final int EREIGNIS_TIMER = count++;

    public int[] event = {-1};
    public String eventClass = "";
    private static final ArrayList<Listener> listeners = new ArrayList<>();

    public Listener(int event, String eventClass) {
        this.event = new int[]{event};
        this.eventClass = eventClass;
    }

    public Listener(int[] event, String eventClass) {
        this.event = event;
        this.eventClass = eventClass;
    }

    public void ping() {
    }

    public static synchronized void addListener(Listener listener) {
        listeners.add(listener);
    }

    public static synchronized void notify(int eventNotify, String eventClass) {

        listeners.stream().forEach(listener -> {
            for (final int event : listener.event) {

                if (event == eventNotify && !listener.eventClass.equals(eventClass)) {
                    // um einen Kreislauf zu verhindern
                    try {
                        listener.pingen();
                    } catch (final Exception ex) {
                        PLog.errorLog(512021043, ex);
                    }
                }

            }
        });

    }

    private void pingen() {
        try {
            Platform.runLater(() -> ping());
        } catch (final Exception ex) {
            PLog.errorLog(915421458, ex);
        }
    }

}
