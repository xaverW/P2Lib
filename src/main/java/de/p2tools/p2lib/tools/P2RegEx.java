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

import java.util.regex.Pattern;

public class P2RegEx {

    public static boolean checkPattern(String regEx) {
        Pattern p;
        try {
            p = Pattern.compile(regEx);
        } catch (final Exception ex) {
            p = null;
        }
        return p != null;
    }

    public static Pattern makePattern(String regEx) {
        Pattern p;
        try {
            p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
        } catch (final Exception ex) {
            p = null;
        }
        return p;
    }

    public static boolean check(String value, Pattern pattern) {
        if (value == null || pattern == null) {
            return true;
        }

        value = value.trim();
        if (value.isEmpty()) {
            return true;
        }

        if (pattern.matcher(value).matches()) {
            return true;
        }

        return false;
    }
}
