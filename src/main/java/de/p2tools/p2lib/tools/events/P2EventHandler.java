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


package de.p2tools.p2lib.tools.events;

import java.util.ArrayList;

public class P2EventHandler {

    private final ArrayList<P2Listener> listeners = new ArrayList<>();

    public P2EventHandler() {
    }

    public void addListener(P2Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(P2Listener listener) {
        listeners.remove(listener);
    }

    public <T extends P2Event> void notifyListener(T event) {
        listeners.stream()
                .filter(p2Listener -> p2Listener.getEventNo() == event.getEventNo())
                .forEach(p2Listener -> p2Listener.notify(event));
    }
}
