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

package de.p2tools.p2Lib.tools.log;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * diese Meldungen können in einem Tab "Meldungen" angesehen werden
 * und sind für die User gedacht (werden aber auch im PLog eingetragen)
 */
public class SysMsg {

    public static ObservableList<String> textSystem = FXCollections.observableArrayList();

    private static final int MAX_LAENGE_1 = 50000;
    private static final int MAX_LAENGE_2 = 30000;
    private static int lineNrSystem = 0;

    static synchronized void sysMsg(ArrayList<String> text) {
        systemmeldung(text.toArray(new String[]{}));
    }

    static synchronized void sysMsg(String[] text) {
        systemmeldung(text);
    }

    static synchronized void sysMsg(String text) {
        systemmeldung(new String[]{text});
    }

    private static void systemmeldung(String[] text) {
        if (text.length <= 1) {
            notify(text[0]);

        } else {
            String line = "----------------------------------------                    ";
            String txt;
            notify(line);

            for (int i = 0; i < text.length; ++i) {
                txt = "| " + text[i];
                if (i == 0) {
                    notify(text[i]);
                } else {
                    notify("    " + text[i]);
                }
            }
            notify(" ");
        }
    }

    public static void clearText() {
        lineNrSystem = 0;
        textSystem.clear();
    }

    private static void notify(String line) {
        addText(textSystem, "[" + getNr(lineNrSystem++) + "]   " + line);
    }

    private static String getNr(int nr) {
        final int MAX_STELLEN = 5;
        final String FUELL_ZEICHEN = "0";
        String str = String.valueOf(nr);
        while (str.length() < MAX_STELLEN) {
            str = FUELL_ZEICHEN + str;
        }
        return str;
    }

    private synchronized static void addText(ObservableList<String> text, String texte) {
        if (text.size() > MAX_LAENGE_1) {
            text.remove(0, MAX_LAENGE_2);
        }
        text.add(texte + System.getProperty("line.separator"));
    }

    public synchronized static String getText() {
        // wegen synchronized hier
        return String.join("", textSystem);
    }

}
