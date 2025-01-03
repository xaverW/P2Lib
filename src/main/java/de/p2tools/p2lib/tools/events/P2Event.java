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

public class P2Event {
    private int eventNo = 0;
    private String text = "";
    private int number = 0;

    public P2Event(int eventNo) {
        this.eventNo = eventNo;
    }

    public P2Event(int eventNo, String text) {
        this.eventNo = eventNo;
        this.text = text;
    }

    public P2Event(int eventNo, int number) {
        this.eventNo = eventNo;
        this.number = number;
    }

    public P2Event(int eventNo, String text, int number) {
        this.eventNo = eventNo;
        this.text = text;
        this.number = number;
    }

    public int getEventNo() {
        return eventNo;
    }

    public void setEventNo(int eventNo) {
        this.eventNo = eventNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
