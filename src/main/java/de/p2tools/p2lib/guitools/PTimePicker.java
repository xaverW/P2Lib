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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.tools.date.PLTimeFactory;
import javafx.scene.control.ComboBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PTimePicker extends ComboBox<LocalTime> {
    private int addMinutes = 15;
    private boolean fromNow = false;

    public PTimePicker() {
        LocalTime l = LocalTime.now().plusMinutes(addMinutes);
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public PTimePicker(boolean fromNow) {
        this.fromNow = fromNow;
        LocalTime l = LocalTime.now().plusMinutes(addMinutes);
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public PTimePicker(int addMinutes) {
        this.addMinutes = addMinutes;
        LocalTime l = LocalTime.now();
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public PTimePicker(LocalTime startTime, int addMinutes) {
        this.addMinutes = addMinutes;
        init(startTime);
    }

    private void init(LocalTime startTime) {
        LocalTime pl = LocalTime.ofSecondOfDay(0);
        List<LocalTime> list = new ArrayList<>();
        LocalTime plSelect = pl;

        for (int h = 0; h < 24; ++h) {
            for (int m = 0; m < 60; m += addMinutes) {
                pl = LocalTime.of(h, m);

                if (pl.compareTo(LocalTime.now()) <= 0 && !fromNow) {
                    list.add(pl);
                } else if (pl.compareTo(LocalTime.now()) > 0) {
                    list.add(pl);
                }

                if (pl.compareTo(startTime) <= 0) {
                    plSelect = pl;
                }
            }
        }
        this.getItems().addAll(list);
        this.getSelectionModel().select(plSelect);
    }

    public void removeTime() {
        this.setValue(null);
    }

    public LocalTime getLocalTime() {
        return getValue();
    }

    public void clearTime() {
        this.setValue(null);
    }

    public String getTime() {
        return PLTimeFactory.toString(getValue());
    }

    public void setTime(LocalTime localTime) {
        this.getSelectionModel().select(localTime);
    }

    public void setTime(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(null);
        } else {
            LocalTime pLocalTime = PLTimeFactory.fromString(stringDate);
            this.getSelectionModel().select(pLocalTime);
        }
    }
}
