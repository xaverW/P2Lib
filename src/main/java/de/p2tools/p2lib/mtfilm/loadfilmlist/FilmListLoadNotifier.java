/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.mtfilm.loadfilmlist;

import de.p2tools.p2lib.tools.log.PLog;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;

public class FilmListLoadNotifier {
    public final EventListenerList listeners = new EventListenerList();

    public void notifyEvent(NOTIFY notify, ListenerFilmlistLoadEvent event) {
        try {
            Platform.runLater(() -> {
                for (final ListenerLoadFilmlist l : listeners.getListeners(ListenerLoadFilmlist.class)) {
                    switch (notify) {
                        case START -> l.start(event);
                        case PROGRESS -> l.progress(event);
                        case LOADED -> l.loaded(event);
                        case FINISHED -> l.finished(event);
                    }
                }
            });
        } catch (final Exception ex) {
            PLog.errorLog(912045120, ex);
        }
    }

    public void notifyFinishedOk() {
        notifyEvent(FilmListLoadNotifier.NOTIFY.FINISHED, ListenerFilmlistLoadEvent.getEmptyEvent());
    }

    public void addListenerLoadFilmlist(ListenerLoadFilmlist listener) {
        listeners.add(ListenerLoadFilmlist.class, listener);
        PLog.debugLog("======>" + listeners.getListenerCount());
    }

    public void removeListenerLoadFilmlist(ListenerLoadFilmlist listener) {
        listeners.remove(ListenerLoadFilmlist.class, listener);
        PLog.debugLog("======>" + listeners.getListenerCount());
    }

    public enum NOTIFY {START, PROGRESS, LOADED, FINISHED}
}
