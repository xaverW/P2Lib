/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.tools;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class DiacriticFactory {
    private static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private DiacriticFactory() {
    }

    public static String flattenDiacritic(String string) {
        try {
            if (!string.contains("ä") && !string.contains("ö") && !string.contains("ü") &&
                    !string.contains("Ä") && !string.contains("Ö") && !string.contains("Ü")) {
                return strip(string);

            } else {
                String to = "";
                for (char c : string.toCharArray()) {
                    String s = c + "";
                    if (s.equals("ä") || s.equals("ö") || s.equals("ü") ||
                            s.equals("Ä") || s.equals("Ö") || s.equals("Ü")) {
                        to += s;
                    } else {
                        to += strip(s);
                    }
                }
                return to;
            }
        } catch (Exception ex) {
        }
        return string;
    }

    private static String strip(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }
}
