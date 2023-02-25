/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.tools.log.PLog;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PStringUtils {
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

    private PStringUtils() {
    }

    /**
     * get formatted String from an integer
     *
     * @param number
     * @return
     */
    public static String getFormattedString(int number) {
        return numberFormat.format(number);
    }

    /**
     * Convert a string from Java´s native UTF-16 to US-ASCII character encoding.
     *
     * @param string The UTF-16 string.
     * @return US-ASCII encoded string for the OS.
     */
    public static String convertToASCIIEncoding(String string) {
        String ret = string;

        ret = ret.replace("ä", "ae");
        ret = ret.replace("ö", "oe");
        ret = ret.replace("ü", "ue");
        ret = ret.replace("Ä", "Ae");
        ret = ret.replace("Ö", "Oe");
        ret = ret.replace("Ü", "Ue");
        ret = ret.replace("ß", "ss");

        //convert our filename to OS encoding...
        try {
            final CharsetEncoder charsetEncoder = Charset.forName("US-ASCII").newEncoder();
            charsetEncoder.onMalformedInput(CodingErrorAction.REPLACE); // otherwise breaks on first unconvertable char
            charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            charsetEncoder.replaceWith(new byte[]{'_'});

            final ByteBuffer buf = charsetEncoder.encode(CharBuffer.wrap(ret));
            if (buf.hasArray()) {
                ret = new String(buf.array());
            }

            //remove NUL character from conversion...
            ret = ret.replaceAll("\\u0000", "");
        } catch (CharacterCodingException ex) {
            PLog.errorLog(945120201, ex);
        }

        return ret;
    }


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
        return appendList(list, separator, false, false);
    }

    public static String appendList(List<String> list, String separator, boolean removeEmpty, boolean removeDouble) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return appendStream(list.stream(), separator, removeEmpty, removeDouble);
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
        return appendArray(list, separator, false, false);
    }

    public static String appendArray(String[] list, String separator, boolean removeEmpty, boolean removeDouble) {
        if (list == null || list.length == 0) {
            return "";
        }

        Stream<String> stream = Stream.of(list);
        return appendStream(stream, separator, removeEmpty, removeDouble);
    }

    private static String appendStream(Stream<String> stream, String separator, boolean removeEmpty, boolean removeDouble) {
        HashSet set = (removeDouble ? new HashSet() : null);

        return stream
                .filter(s -> s != null)
                .filter(s -> !removeEmpty || !s.isEmpty())
                .filter(s -> !removeDouble || set.add(s))
                .collect(Collectors.joining(separator));

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

    /**
     * shorten a string to "max" chars
     *
     * @param max
     * @param text
     * @param center
     * @param addInFront
     * @return
     */
    public static String shortenString(int max, String text, boolean center, boolean addInFront) {
        if (text.length() > max) {
            if (center && max > 32) {
                text = text.substring(0, 25) + " .... " + text.substring(text.length() - (max - 31));
            } else {
                text = text.substring(0, max - 1);
            }
        }
        while (text.length() < max) {
            if (addInFront) {
                text = ' ' + text;
            } else {
                text = text + ' ';
            }
        }
        return text;
    }

    /**
     * increase string to "max" length
     *
     * @param max
     * @param text
     * @return
     */
    public static String increaseString(int max, String text) {
        return increaseString(max, false, text);
    }

    public static String increaseString(int max, boolean front, String text) {
        while (text.length() < max) {
            if (front) {
                text = ' ' + text;
            } else {
                text = text + ' ';
            }
        }
        return text;
    }
}
