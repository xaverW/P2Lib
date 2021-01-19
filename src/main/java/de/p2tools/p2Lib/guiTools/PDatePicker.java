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

import de.p2tools.p2Lib.tools.date.PLocalDate;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDatePicker extends DatePicker {
    private PLocalDate pLocalDate;
    private final String pattern = "dd.MM.yyyy";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public PDatePicker() {
        pLocalDate = new PLocalDate();
        init();
    }

    public PDatePicker(PLocalDate pLocalDate) {
        this.pLocalDate = pLocalDate;
        init();
    }

    private void init() {
        setPLocalDatePickerConverter();
        this.setValue(this.pLocalDate.getLocalDate());
        this.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                System.out.println(oldValue + " - " + newValue);


                if (newValue == null || newValue.isEmpty()) {
                    setValue(null);
                } else {
                    final PLocalDate pLocalDate = new PLocalDate(newValue);
                    setValue(pLocalDate.getLocalDate());
                }
                setDate();


                System.out.println("this.getvalue: " + this.getValue());
//                setDate(newValue);

//                LocalDate ld = PLocalDate.getPLocalDate(newValue);
//                if (ld != null) {
//                    setDate(new PLocalDate(ld));
//                }

//                pLocalDate.setPLocalDate(newValue);
            }
        });
    }

    public void setDate(PLocalDate pLocalDate) {
        this.pLocalDate = pLocalDate;
        this.setValue(this.pLocalDate.getLocalDate());
    }

    public void setDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.setValue(null);
            this.pLocalDate.clearPDate();
        } else {
            this.pLocalDate.setPLocalDate(stringDate);
            this.setValue(pLocalDate.getLocalDate());
        }
    }

    private void setDate() {
        if (this.getValue() == null) {
            if (pLocalDate != null) {
                pLocalDate.clearPDate();
            }

        } else {
            pLocalDate.setPLocalDate(this.getValue());
        }
    }

    public PLocalDate getpLocalDate() {
        return pLocalDate;
    }

    public void clearDate() {
        this.pLocalDate.clearPDate();
        this.setValue(null);
    }

    public String getDate() {
        String ret = "";

        LocalDate date = getValue();
        if (date != null) {
            ret = dateFormatter.format(date);
        }

        return ret;
    }

    private void setPLocalDatePickerConverter() {
        setPromptText(pattern.toLowerCase());
        StringConverter converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (pLocalDate == null) {
                    return "";
                }

                if (date == null) {
                    pLocalDate.clearPDate();
                    return "";
                }

                pLocalDate.setPLocalDate(date);
                if (pLocalDate.isEmpty()) {
                    return "";
                } else {
                    return pLocalDate.toString();
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        setConverter(converter);
    }
}
