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


package de.p2tools.p2lib.p2event;

public class P2Events {
    // P2Lib zählt zurück
    private static int count = 0;
    public static final int EVENT_TIMER_SECOND = --count;
    public static final int EVENT_TIMER_HALF_SECOND = --count;

    public static final int EVENT_FILMLIST_LOAD_START = --count;
    public static final int EVENT_FILMLIST_LOAD_PROGRESS = --count;
    public static final int EVENT_FILMLIST_LOAD_LOADED = --count;
    public static final int EVENT_FILMLIST_LOAD_FINISHED = --count;

    public static final int LOAD_AUDIO_LIST_START = --count;
    public static final int LOAD_AUDIO_LIST_PROGRESS = --count;
    public static final int LOAD_AUDIO_LIST_LOADED = --count;
    public static final int LOAD_AUDIO_LIST_FINISHED = --count;
}
