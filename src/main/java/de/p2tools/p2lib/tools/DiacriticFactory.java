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

public class DiacriticFactory {
    private static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private DiacriticFactory() {
    }

    public static String flattenDiacritic(String string) {
        // todo, unterschiedlich lang!!! und kommt zu Fehlern -> aä
        // Weil wir aber f̶̶a̶̶u̶̶l̶̶ ̶̶s̶̶i̶̶n̶̶d̶̶ ̶̶u̶̶n̶̶d̶̶ ̶̶n̶̶i̶̶c̶̶h̶̶t̶̶ ̶̶i̶̶n̶̶ ̶̶d̶̶e̶̶r̶̶ ̶̶k̶̶ä̶̶l̶̶t̶̶e̶̶ ̶̶r̶̶u̶̶m̶̶s̶̶t̶̶e̶̶h̶̶e̶̶n̶̶ ̶̶m̶̶ö̶̶c̶̶h̶̶t̶̶e̶̶n̶ sparen müssen
        // Weil wir aber faul sind und nicht in der kalte rumstehen mochten sparen mussen

        try {
//           if (string.equals(strip(string))) {
//               //8,83s
//               return string;
//           }

            String to = strip(string);
            if (string.equals(to)) {
                //13,56 ohne 16,10
                //7,17 ohne 7,53
                //6,55s ohne 7,03
                return string;
            }

            char[] s = string.toCharArray();
            char[] t = to.toCharArray();

            for (int i = 0; i < s.length; ++i) {
                String st = s[i] + "";

//               if (string.substring(i, i + 1).equals("ä") || string.substring(i, i + 1).equals("ö") ||
//                       string.substring(i, i + 1).equals("ü") || string.substring(i, i + 1).equals("Ä") ||
//                       string.substring(i, i + 1).equals("Ö") || string.substring(i, i + 1).equals("Ü")) {
//                   //13,56s
//                   t[i] = s[i];
//               }

//               if (st.equals("ä") || st.equals("ö") || st.equals("ü") ||
//                       st.equals("Ä") || st.equals("Ö") || st.equals("Ü")) {
//                   //7,17s
//                   if (i == 0) {
//                       to = st + to.substring(i + 1);
//                   } else if (i == to.length() - 1) {
//                       to = to.substring(0, i) + st;
//                   } else {
//                       to = to.substring(0, i) + st + to.substring(i + 1);
//                   }
//               }

                if (st.equals("ä") || st.equals("ö") || st.equals("ü") ||
                        st.equals("Ä") || st.equals("Ö") || st.equals("Ü")) {
                    //6,55s
                    t[i] = s[i];
                }
            }

//           return to;
            return String.valueOf(t);

        } catch (Exception ex) {
            P2Log.errorLog(987451254, ex);
        }
        return string;
    }

    private static String strip(String str) {
//       return StringUtils.stripAccents(str); //7,9s

        str = Normalizer.normalize(str, Normalizer.Form.NFD); //6,5s
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }
}
