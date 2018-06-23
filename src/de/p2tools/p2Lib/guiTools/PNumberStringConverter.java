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

import javafx.scene.Node;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class PNumberStringConverter extends NumberStringConverter {

    private final Locale locale = Locale.GERMAN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    private final Node node;

    public PNumberStringConverter(Node node) {
        this.node = node;
    }


    @Override
    public Number fromString(String value) {
        Number ret = 0;
        try {
            setNodeStyle("");
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                setNodeStyle("");
                return null;
            }

            ret = numberFormat.parse(value);
            if (!value.equals(ret + "")) {
                setNodeStyle("-fx-control-inner-background: #FF0000;");
            }
        } catch (ParseException ex) {
            setNodeStyle("-fx-control-inner-background: #FF0000;");
        }
        return ret;
    }

    private void setNodeStyle(String style) {
        if (node == null) {
            return;
        }

        node.setStyle(style);
    }
}
