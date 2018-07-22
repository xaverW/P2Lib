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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
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
            final String str = it.next();
            if (str == null || str.isEmpty()) {
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
        return appendList(list, separator, false);
    }

    public static String appendList(List<String> list, String separator, boolean removeEmpty) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return appendStream(list.stream(), separator, removeEmpty);
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
        return appendArray(list, separator, false);
    }

    public static String appendArray(String[] list, String separator, boolean removeEmpty) {
        if (list == null || list.length == 0) {
            return "";
        }

        Stream<String> stream = Stream.of(list);
        return appendStream(stream, separator, removeEmpty);
    }

    private static String appendStream(Stream<String> stream, String separator, boolean removeEmpty) {
        if (removeEmpty) {
            return stream.filter(s -> s != null).filter(s -> !s.isEmpty()).collect(Collectors.joining(separator));
        } else {
            return stream.filter(s -> s != null).collect(Collectors.joining(separator));
        }
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
        if (list == null || list.size() == 0) {
            return;
        }

        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) {
            final String next = it.next();

            if (next == null) {
                it.remove();
                continue;
            }

            if (!without.isEmpty() && next.startsWith(without)) {
                continue;
            }
            it.set(prefix + next);
        }
    }

}
