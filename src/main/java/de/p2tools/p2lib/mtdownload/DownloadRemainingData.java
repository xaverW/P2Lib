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

package de.p2tools.p2lib.mtdownload;

public class DownloadRemainingData implements Comparable<DownloadRemainingData> {
    public static final int REMAINING_NOT_STARTET = Integer.MAX_VALUE; // Restzeit wenn noch nicht gestartet
    Integer l;
    String s;

    public DownloadRemainingData() {
        setValue(REMAINING_NOT_STARTET);
    }

    /**
     * damit wird nach long l sortiert und der Text s angezeigt
     *
     * @param l
     */
    public DownloadRemainingData(int l) {
        setValue(l);
    }

    public void setValue(int l) {
        this.l = l;
        set();
    }

    public int getValue() {
        return this.l;
    }

    private void set() {
        if (l == REMAINING_NOT_STARTET) {
            // dann ist er noch nicht gestartet
            this.s = "";

        } else if (l < 0) {
            // dann ist er fertig
            final int dauer = (int) (l / 60);
            if (dauer == 0) {
                this.s = "Dauer: " + -1 * l + " s";
            } else {
                this.s = "Dauer: " + -1 * dauer + " Min";
            }

        } else {
            // dann läuft er noch
            this.s = getTextTimeLeft(l);
        }
    }

    public static String getTextTimeLeft(long timeLeftSeconds) {
        if (timeLeftSeconds > 300) {
            return Math.round(timeLeftSeconds / 60.0) + " Min.";

        } else if (timeLeftSeconds > 230) {
            return "5 Min.";
        } else if (timeLeftSeconds > 170) {
            return "4 Min.";
        } else if (timeLeftSeconds > 110) {
            return "3 Min.";
        } else if (timeLeftSeconds > 60) {
            return "2 Min.";
        } else if (timeLeftSeconds > 30) {
            return "1 Min.";
        } else if (timeLeftSeconds > 20) {
            return "30 s";
        } else if (timeLeftSeconds > 10) {
            return "20 s";
        } else if (timeLeftSeconds > 0) {
            return "10 s";
        } else {
            return "";
        }
    }

    @Override
    public int compareTo(DownloadRemainingData d) {
        return d.l.compareTo(l);
    }

    @Override
    public String toString() {
        return s;
    }
}
