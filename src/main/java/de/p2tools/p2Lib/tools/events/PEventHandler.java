/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.tools.events;

import java.util.ArrayList;

public class PEventHandler {

    private final ArrayList<PListener> listeners = new ArrayList<>();

    public PEventHandler() {
    }

    public void addListener(PListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PListener listener) {
        listeners.remove(listener);
    }

    public <T extends PEvent> void notifyListener(T event) {
        listeners.stream()
                .filter(pListener -> pListener.getEventNo() == event.getEventNo())
                .forEach(pListener -> pListener.notify(event));
    }
}
