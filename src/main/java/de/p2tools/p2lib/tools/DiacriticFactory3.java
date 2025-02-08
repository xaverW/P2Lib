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

public class DiacriticFactory3 {
    private static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private DiacriticFactory3() {
    }

    public static String flattenDiacritic(String string) {
        try {
//            if (string.contains("Tagebuch")) {
//                System.out.println("Mist");
//            }

            if (!string.contains("ä") && !string.contains("ö") && !string.contains("ü") &&
                    !string.contains("Ä") && !string.contains("Ö") && !string.contains("Ü")) {
                // spart ~10% Zeit
                return strip1(string);
            }

            // dann halt durchgehen
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < string.length(); ++i) {
                char c = string.charAt(i);
                if (c == 'ä' || c == 'ö' || c == 'ü' ||
                        c == 'Ä' || c == 'Ö' || c == 'Ü') {
                    sb1.append(strip1(sb2.toString()));
                    sb2.setLength(0);
                    sb1.append(c);
                } else {
                    sb2.append(c);
                }
            }
            sb1.append(strip1(sb2.toString()));
            return sb1.toString();

        } catch (Exception ex) {
            P2Log.errorLog(958587452, ex);
        }
        return string;
    }

    private static String strip1(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }
}
