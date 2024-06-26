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

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class GermanStringIntSorter implements Comparator<String> {

    private static Collator collator;
    private static GermanStringIntSorter instance;

    /**
     * can be used, as one Comperator (static) or
     * many, a new one in every location
     */
    public GermanStringIntSorter() {
        super();
        // ignore lower/upper case, but accept special characters in localised alphabetical order
        collator = Collator.getInstance(Locale.GERMANY);
        collator.setStrength(Collator.SECONDARY);
    }

    public static GermanStringIntSorter getInstance() {
        if (instance == null) {
            instance = new GermanStringIntSorter();
        }
        return instance;
    }

    @Override
    public int compare(String o1, String o2) {
        try {
            if (o1 != null && o2 != null) {
                o1 = o1.toLowerCase(Locale.GERMANY);
                o2 = o2.toLowerCase(Locale.GERMANY);

                int l1 = o1.length();
                int l2 = o2.length();
                int count1 = 0, count2 = 0;
                char c1, c2;

                String digit = "";
                int digit1, digit2;

                for (int i = 0; i < o1.length() && i < o2.length(); ++i) {

                    if (count1 >= l1 && count2 < l2) {
                        return -1;
                    } else if (count1 < l1 && count2 >= l2) {
                        return 1;
                    } else if (count1 >= l1 && count2 >= l2) {
                        //dann hats nicht geklappt
                        break;
                    }

                    c1 = o1.charAt(count1);
                    c2 = o2.charAt(count2);

                    if (Character.isDigit(c1) && Character.isDigit(c2)) {
                        //beide Character sind Digits

                        //Zahl1
                        while (count1 < l1 && Character.isDigit(o1.charAt(count1))) {
                            c1 = o1.charAt(count1);
                            digit += c1;
                            ++count1;
                        }
                        digit1 = Integer.valueOf(digit + "");
                        digit = "";

                        //Zahl2
                        while (count2 < l2 && Character.isDigit(o2.charAt(count2))) {
                            c2 = o2.charAt(count2);
                            digit += c2;
                            ++count2;
                        }
                        digit2 = Integer.valueOf(digit + "");
                        digit = "";

                        //Vergleich
                        ++count1;
                        ++count2;
                        if (digit1 == digit2 && count1 >= l1) {
                            return 1;
                        } else if (digit1 == digit2 && count2 >= l2) {
                            return -1;

                        } else if (digit1 == digit2) {
                            continue;

                        } else {
                            final int ret = digit1 == digit2 ? 0 : (digit1 < digit2 ? -1 : 1);
                            return ret;
                        }

                    }

                    if (c1 == c2) {
                        ++count1;
                        ++count2;
                        continue;

                    } else if (!Character.isDigit(c1) && !Character.isDigit(c2)) {
                        return collator.compare(String.valueOf(c1), String.valueOf(c2));

                    } else if (Character.isDigit(c1) && !Character.isDigit(c2)) {
                        return -1;

                    } else if (!Character.isDigit(c1) && Character.isDigit(c2)) {
                        return 1;
                    }

                }//for

                return collator.compare(o1, o2);
            }
        } catch (Exception ex) {
            P2Log.errorLog(915247789, ex, new String[]{"GermanStringSorter:", o1, o2});
        }

        return 0;
    }

}
