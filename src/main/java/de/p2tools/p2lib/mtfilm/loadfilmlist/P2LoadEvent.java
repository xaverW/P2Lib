/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.mtfilm.loadfilmlist;

public class P2LoadEvent {

    public String text;
    public double max;
    public double progress;
    public boolean error;
    public int countFoundFilms;

    public P2LoadEvent(String text, double max, double progress, int countFoundFilms, boolean error) {
        this.text = text;
        this.max = max;
        this.progress = progress;
        this.countFoundFilms = countFoundFilms;
        this.error = error;
    }

    public P2LoadEvent(String text, double progress, int countFoundFilms, boolean error) {
        this.text = text;
        this.max = 0;
        this.progress = progress;
        this.countFoundFilms = countFoundFilms;
        this.error = error;
    }

    public static P2LoadEvent getEmptyEvent() {
        return new P2LoadEvent("", 0, 0, false);
    }
}
