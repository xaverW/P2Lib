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

import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDatePropertyPicker extends DatePicker {
    private PLocalDateProperty pLocalDateProperty = new PLocalDateProperty();
    private static final String pattern = "dd.MM.yyyy";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public PDatePropertyPicker() {
        setDatePickerConverter();
        setDate("");
//        this.getEditor().setOnAction(a -> System.out.println("TEST"));
    }

    public PDatePropertyPicker(PLocalDateProperty pDateProperty) {
        if (pDateProperty == null) {
            PException.throwPException(978450201, this.getClass().toString());
        }

        this.pLocalDateProperty = pDateProperty;
        setDatePickerConverter();
        setDate();
    }

    public void setpDateProperty(PLocalDateProperty pDateProperty) {
        if (pDateProperty == null) {
            PException.throwPException(978450201, this.getClass().toString());
        }

        this.pLocalDateProperty = pDateProperty;
        setDate();
    }

    private void setDate() {
        if (this.pLocalDateProperty.getValue() == null) {
            this.setValue(null);
        } else {
            this.setValue(this.pLocalDateProperty.getValue().getLocalDate());
        }
    }

    public void setDate(PLocalDate pDate) {
        this.pLocalDateProperty.setValue(pDate);
        setDate();
    }


    public void setDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            this.pLocalDateProperty.setValue(null);
        } else {
            final PLocalDate pLocalDate = new PLocalDate(LocalDate.parse(stringDate, dateFormatter));
            this.pLocalDateProperty.setValue(pLocalDate);
        }

        setDate();
    }

    public void clearDate() {
        this.pLocalDateProperty = null;
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

    private void setDatePickerConverter() {
        getStyleClass().add("PDatePropertyPicker");

        setPromptText(pattern.toLowerCase());
        StringConverter converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (pLocalDateProperty == null) {
                    return "";
                }

                if (date == null) {
                    pLocalDateProperty.clearPLocalDate();
                    return "";
                }

                pLocalDateProperty.setPLocalDate(date);
                if (pLocalDateProperty.getValue().isEmpty()) {
                    return "";
                } else {
                    return dateFormatter.format(date);
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;

                } else {
                    return LocalDate.parse(string, dateFormatter);
                }
            }
        };
        setConverter(converter);
    }
}
