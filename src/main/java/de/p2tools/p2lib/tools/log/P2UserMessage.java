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

package de.p2tools.p2lib.tools.log;

import de.p2tools.p2lib.P2LibConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * diese Meldungen können in einem Tab "Meldungen" angesehen werden
 * und sind für die User gedacht (werden aber auch im PLog eingetragen)
 */
public class P2UserMessage {

    public static ObservableList<String> msgList = FXCollections.observableArrayList();

    private static final int MAX_SIZE_1 = 50000;
    private static final int MAX_SIZE_2 = 30000;
    private static int lineNo = 0;

    static synchronized void userMsg(ArrayList<String> text) {
        message(text.toArray(new String[]{}));
    }

    static synchronized void userMsg(String[] text) {
        message(text);
    }

    static synchronized void userMsg(String text) {
        message(new String[]{text});
    }

    private static void message(String[] text) {
        if (text.length <= 1) {
            notify(text[0]);

        } else {
            String line = "----------------------------------------                    ";
            notify(line);

            for (int i = 0; i < text.length; ++i) {
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
        lineNo = 0;
        msgList.clear();
    }

    private static void notify(String line) {
        addText(msgList, "[" + getNr(lineNo++) + "]   " + line);
    }

    private static String getNr(int no) {
        final int MAX_STELLEN = 5;
        final String FUELL_ZEICHEN = "0";
        String str = String.valueOf(no);
        while (str.length() < MAX_STELLEN) {
            str = FUELL_ZEICHEN + str;
        }
        return str;
    }

    private synchronized static void addText(ObservableList<String> text, String texte) {
        if (text.size() > MAX_SIZE_1) {
            text.remove(0, MAX_SIZE_2);
        }
        text.add(texte + P2LibConst.LINE_SEPARATOR);
    }

    public synchronized static String getText() {
        // wegen synchronized hier
        return String.join("", msgList);
    }
}
