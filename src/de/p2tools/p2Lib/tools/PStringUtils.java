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


package de.p2tools.p2Lib.tools;

import java.util.*;
import java.util.stream.Stream;

public class PStringUtils {

    /**
     * removes all empty Strings from the list
     *
     * @param list
     * @return
     */
    public static List<String> removeEmptyStrings(List<String> list) {
        if (list == null || list.size() == 0) {
            return list;
        }

        Iterator<String> it = list.listIterator();
        while (it.hasNext()) {
            String str = it.next();
            if (str.isEmpty()) {
                it.remove();
            }
        }
        return list;
    }

    /**
     * append all String from the list to one String
     * and append "append" between the elements
     *
     * @param list
     * @param separator
     * @return
     */
    public static String appendList(List<String> list, String separator) {
        return appendArray(list.toArray(new String[list.size()]), separator);
    }

    /**
     * append all String from the list to one String
     * and append "append" between the elements
     *
     * @param list
     * @param separator
     * @return
     */
    public static String appendArray(String[] list, String separator) {
        if (list == null || list.length == 0) {
            return "";
        }

        Stream<String> stream = Stream.of(list);
        Optional<String> optionalS = stream.reduce((s1, s2) -> s1 + separator + s2);

        return (optionalS.isPresent() ? optionalS.get() : "");

//        StringBuilder builder = new StringBuilder(list[0]);
//        for (int i = 1; i < list.length; ++i) {
//            builder.append(separator);
//            builder.append(list[i]);
//        }
//
//        return builder.toString();
    }

    /**
     * prefix the "prefix" to all Strings from the list
     *
     * @param list
     * @param prefix
     * @return
     */
    public static void appendString(ArrayList<String> list, String prefix) {
        appendString(list, prefix, "");
    }

    /**
     * prefix the "prefix" to all Strings from the list
     * without the strings, starts with "without"
     *
     * @param list
     * @param prefix
     * @param without
     * @return
     */
    public static void appendString(ArrayList<String> list, String prefix, String without) {
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!without.isEmpty() && next.startsWith(without)) {
                continue;
            }
            iterator.set(prefix + next);
        }
    }

}
