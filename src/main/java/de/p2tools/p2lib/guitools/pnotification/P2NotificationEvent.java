/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.guitools.pnotification;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class P2NotificationEvent extends Event {
    public static final EventType<P2NotificationEvent> NOTIFICATION_PRESSED = new EventType<>(ANY, "NOTIFICATION_PRESSED");
    public static final EventType<P2NotificationEvent> SHOW_NOTIFICATION = new EventType<>(ANY, "SHOW_NOTIFICATION");
    public static final EventType<P2NotificationEvent> HIDE_NOTIFICATION = new EventType<>(ANY, "HIDE_NOTIFICATION");

    public final P2Notify NOTIFICATION;

    public P2NotificationEvent(final P2Notify NOTIFICATION,
                               final Object SOURCE,
                               final EventTarget TARGET,
                               EventType<P2NotificationEvent> TYPE) {

        super(SOURCE, TARGET, TYPE);
        this.NOTIFICATION = NOTIFICATION;
    }
}
