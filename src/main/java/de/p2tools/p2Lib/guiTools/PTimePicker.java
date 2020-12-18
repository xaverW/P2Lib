/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.tools.date.PLocalTime;
import javafx.scene.control.ComboBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PTimePicker extends ComboBox<PLocalTime> {
    private PLocalTime pLocalTime;
    private int addMinutes = 15;

    public PTimePicker() {
        LocalTime l = LocalTime.now();
        pLocalTime = new PLocalTime(LocalTime.of(l.getHour(), l.getMinute()));
        init();
    }

    public PTimePicker(int addMinutes) {
        this.addMinutes = addMinutes;
        LocalTime l = LocalTime.now();
        pLocalTime = new PLocalTime(LocalTime.of(l.getHour(), l.getMinute()));
        init();
    }

    public PTimePicker(PLocalTime pLocalTime, int addMinutes) {
        this.pLocalTime = pLocalTime;
        this.addMinutes = addMinutes;
        init();
    }

    private void init() {
        PLocalTime pl = new PLocalTime(LocalTime.ofSecondOfDay(0));
        List<PLocalTime> list = new ArrayList<>();
        PLocalTime plSelect = pl;

        list.add(pl);
        for (int h = 0; h < 24; ++h) {
            for (int m = 0; m < 60; m += addMinutes) {
                pl = new PLocalTime(LocalTime.of(h, m));
                list.add(pl);
                if (pl.compareTo(pLocalTime) <= 0) {
                    plSelect = pl;
                }
            }
        }
        this.getItems().addAll(list);
        this.getSelectionModel().select(plSelect);
        pLocalTime.setPLocalTime(plSelect.getLocalTime());

        this.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && !oldValue.isEmpty() &&
                    newValue != null && !oldValue.equals(newValue)) {
                //System.out.println(oldValue + " - " + newValue);
                pLocalTime.setPLocalTime(newValue);
            }
        });
    }

    public void removeTime() {
        this.pLocalTime = null;
        this.setValue(null);
    }

    public void setTime(PLocalTime localTime) {
        this.pLocalTime = localTime;
//        this.setValue(this.pLocalTime);
        this.getSelectionModel().select(this.pLocalTime);
    }

    public void setTime(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(null);
            this.pLocalTime.clearPLocalTime();
        } else {
            pLocalTime.setPLocalTime(stringDate);
//            this.setValue(pLocalTime);
            this.getSelectionModel().select(pLocalTime);
        }
    }

    public PLocalTime getpLocalTime() {
        return pLocalTime;
    }

    public void clearTime() {
        if (this.pLocalTime != null) {
            this.pLocalTime.clearPLocalTime();
        }
        this.setValue(null);
    }

    public String getTime() {
        String ret = "";

        PLocalTime date = getValue();
        if (date != null) {
            ret = getTime();
        }

        return ret;
    }

}
