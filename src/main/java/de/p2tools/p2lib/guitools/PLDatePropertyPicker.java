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

import de.p2tools.p2lib.tools.PException;
import de.p2tools.p2lib.tools.date.PLDateFactory;
import de.p2tools.p2lib.tools.date.PLDateProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PLDatePropertyPicker extends DatePicker {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private PLDateProperty boundPLocalDateProperty = null;
    private ChangeListener<LocalDate> changeListener = null;

    public PLDatePropertyPicker() {
        genChangeListener();
        init();
    }

    public PLDatePropertyPicker(PLDateProperty pDateProperty) {
        if (pDateProperty == null) {
            PException.throwPException(978450201, this.getClass().toString());
        }
        genChangeListener();
        bindPDateProperty(pDateProperty);
        init();
    }

    private void init() {
        setDate();
        this.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                setDate(newValue);
            }
        });
    }

    private void genChangeListener() {
        this.changeListener = (observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (this.getValue() == null ||
                    this.getValue() != null && !this.getValue().equals(newValue)) {
                setDate(newValue);
            }
        };
    }

    private void setDate() {
        if (this.getValue() == null) {
            if (boundPLocalDateProperty != null) {
                boundPLocalDateProperty.setPLocalDate(LocalDate.MIN);
            }

        } else {
            if (boundPLocalDateProperty != null) {
                boundPLocalDateProperty.setPLocalDate(this.getValue());
            }
        }
    }

    public void bindPDateProperty(PLDateProperty pDateProperty) {
        if (pDateProperty == null) {
            PException.throwPException(978450201, this.getClass().toString());
        }
        if (boundPLocalDateProperty != null) {
            //todo
            boundPLocalDateProperty.removeListener(changeListener);
        }

        //todo
        this.boundPLocalDateProperty = pDateProperty;
        this.boundPLocalDateProperty.addListener(changeListener);
        this.setValue(boundPLocalDateProperty.get());
        this.setDisable(boundPLocalDateProperty == null);
    }

    public void unbindPDateProperty() {
        if (boundPLocalDateProperty != null) {
            //todo
            boundPLocalDateProperty.removeListener(changeListener);
        }

        this.boundPLocalDateProperty = null;
        this.setValue(null);
        this.setDisable(true);
    }

    public String getDate() {
        String ret = "";
        LocalDate date = getValue();
        if (date != null) {
            ret = dateFormatter.format(date);
        }

        return ret;
    }

    public void setDate(LocalDate localDate) {
        setValue(localDate);
        setDate();
    }

    public void setDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            setValue(null);
        } else {
            setValue(PLDateFactory.fromString(stringDate));
        }
        setDate();
    }
}
