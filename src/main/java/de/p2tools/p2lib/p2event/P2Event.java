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


package de.p2tools.p2lib.p2event;

public class P2Event {

    private int eventNo = 0;
    private String text = "";
    private boolean error = false;

    private double min = 0;
    private double act = 0;
    private double max = 0;

    public P2Event(int eventNo) {
        this.eventNo = eventNo;
    }

    public P2Event(int eventNo, String text) {
        this.eventNo = eventNo;
        this.text = text;
    }

    public P2Event(int eventNo, String text, double act) {
        this.eventNo = eventNo;
        this.text = text;
        this.act = act;
    }

    public P2Event(int eventNo, String text, double act, double max) {
        this.eventNo = eventNo;
        this.text = text;
        this.act = act;
        this.max = max;
    }

    public P2Event(int eventNo, String text, double min, double act, double max) {
        this.eventNo = eventNo;
        this.text = text;
        this.min = min;
        this.act = act;
        this.max = max;
    }

    public P2Event(int eventNo, String text, double min, double act, double max, boolean error) {
        this.eventNo = eventNo;
        this.text = text;
        this.min = min;
        this.act = act;
        this.max = max;
        this.error = error;
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

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAct() {
        return act;
    }

    public void setAct(double act) {
        this.act = act;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
