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

import javafx.scene.Node;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class P2NumberStringConverter extends NumberStringConverter {

    private final Locale locale = Locale.GERMAN;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    private final Node node;
    private boolean stateLabel = false;

    public P2NumberStringConverter(Node node) {
        this.node = node;
    }

    public P2NumberStringConverter(Node node, boolean stateLabel) {
        this.stateLabel = stateLabel;
        this.node = node;
        setStateLabel();
    }

    private void setStateLabel() {
        if (stateLabel) {
            node.setStyle(P2Styles.PTEXTFIELD_LABEL);
        }
    }

    @Override
    public Number fromString(String value) {
        return check(value);
    }

    public Number check(String value) {
        Number ret = 0;
        try {
            setNodeStyle(stateLabel ? P2Styles.PTEXTFIELD_LABEL : "");
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                setNodeStyle("");
                return null;
            }

            ret = numberFormat.parse(value);
            String s = numberFormat.format(ret);
            if (!s.equals(value) && !value.equals(ret + "")) {
                // decimal point!!
                setNodeStyle(stateLabel ? P2Styles.PTEXTFIELD_LABEL_ERROR : P2Styles.PTEXTFIELD_ERROR);
            }

//            ret = numberFormat.parse(value);
//            if (!value.equals(ret + "")) {
//                setNodeStyle(stateLabel ? PStyles.PTEXTFIELD_LABEL_ERROR : PStyles.PTEXTFIELD_ERROR);
//            }
        } catch (ParseException ex) {
            setNodeStyle(stateLabel ? P2Styles.PTEXTFIELD_LABEL_ERROR : P2Styles.PTEXTFIELD_ERROR);
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
