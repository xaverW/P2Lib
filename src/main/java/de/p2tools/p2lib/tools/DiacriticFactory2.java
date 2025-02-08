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


package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.log.P2Log;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class DiacriticFactory2 {
    private static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private DiacriticFactory2() {
    }

    public static String flattenDiacritic(String string) {
        // todo, unterschiedlich lang!!!
        // Weil wir aber f̶̶a̶̶u̶̶l̶̶ ̶̶s̶̶i̶̶n̶̶d̶̶ ̶̶u̶̶n̶̶d̶̶ ̶̶n̶̶i̶̶c̶̶h̶̶t̶̶ ̶̶i̶̶n̶̶ ̶̶d̶̶e̶̶r̶̶ ̶̶k̶̶ä̶̶l̶̶t̶̶e̶̶ ̶̶r̶̶u̶̶m̶̶s̶̶t̶̶e̶̶h̶̶e̶̶n̶̶ ̶̶m̶̶ö̶̶c̶̶h̶̶t̶̶e̶̶n̶ sparen müssen
        // Weil wir aber faul sind und nicht in der kalte rumstehen mochten sparen mussen

        try {
            if (string.contains("Asyl für syrischen Folterchef")) {
                System.out.println("Mist");
            }

            String to1 = strip1(string);
            if (string.equals(to1)) {
                return string;
            }

            String to2 = strip2(string);
            if (to1.equals(to2)) {
                return to1;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < to1.length() && i < to2.length(); ++i) {
                // kann sich auch verschieben und dauert länger
                char c2 = to2.charAt(i);
                if (c2 == 'ä' || c2 == 'ö' ||
                        c2 == 'ü' || c2 == 'Ä' ||
                        c2 == 'Ö' || c2 == 'Ü') {
                    sb.append(c2);
                } else {
                    sb.append(to1.charAt(i));
                }
            }
            return sb.toString();
        } catch (Exception ex) {
            P2Log.errorLog(987451254, ex);
        }
        return string;
    }

    private static String strip1(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD); //6,5s
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    private static String strip2(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFC); //6,5s
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }
}
