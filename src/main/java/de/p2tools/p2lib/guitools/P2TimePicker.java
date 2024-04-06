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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.tools.date.P2LTimeFactory;
import javafx.scene.control.ComboBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class P2TimePicker extends ComboBox<LocalTime> {
    private int addMinutes = 15;
    private boolean fromNow = false;

    public P2TimePicker() {
        LocalTime l = LocalTime.now().plusMinutes(addMinutes);
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public P2TimePicker(boolean fromNow) {
        this.fromNow = fromNow;
        LocalTime l = LocalTime.now().plusMinutes(addMinutes);
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public P2TimePicker(int addMinutes) {
        this.addMinutes = addMinutes;
        LocalTime l = LocalTime.now();
        LocalTime startTime = LocalTime.of(l.getHour(), l.getMinute());
        init(startTime);
    }

    public P2TimePicker(LocalTime startTime, int addMinutes) {
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

                if (!pl.isAfter(LocalTime.now()) && !fromNow) {
                    list.add(pl);
                } else if (pl.isAfter(LocalTime.now())) {
                    list.add(pl);
                }

                if (!pl.isAfter(startTime)) {
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

//    public String getTime() {
//        return P2LTimeFactory.toString(getValue());
//    }

    public String getTime() {
        return P2LTimeFactory.toString_HM(getValue());
    }

    public void setTime(LocalTime localTime) {
        this.getSelectionModel().select(localTime);
    }

    public void setTime(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(null);

        } else {
            final LocalTime pLocalTime;
            if (stringDate.length() == 5) {
                // 12:30
                pLocalTime = P2LTimeFactory.fromString_HM(stringDate);
            } else {
                // 12:30:00
                pLocalTime = P2LTimeFactory.fromString(stringDate);
            }
            this.getSelectionModel().select(pLocalTime);
        }
    }
}
