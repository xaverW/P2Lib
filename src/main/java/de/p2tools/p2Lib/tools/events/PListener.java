/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2Lib.tools.events;

import javafx.application.Platform;

public class PListener {
    private final int eventNo;

    public PListener(int eventNo) {
        this.eventNo = eventNo;
    }

    public int getEventNo() {
        return eventNo;
    }

    public synchronized <T extends PEvent> void notify(T event) {
        ping(event);
        Platform.runLater(() -> pingGui(event));
    }

    /**
     * @param event
     */
    public <T extends PEvent> void ping(T event) {
    }

    /**
     * @param event
     */
    public <T extends PEvent> void pingGui(T event) {
    }
}
