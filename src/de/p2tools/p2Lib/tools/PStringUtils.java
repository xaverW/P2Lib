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

public class PStringUtils {

    /**
     * append all String from the list to one String,
     *
     * @param list
     * @param add
     * @return
     */
    public static String appendList(ArrayList<String> list, String add) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder log = new StringBuilder(list.get(0));
        for (int i = 1; i < list.size(); ++i) {
            log.append("\n");
            log.append(list.get(i));
        }

        return log.toString();
    }

    /**
     * append all String from the list to one String,
     *
     * @param list
     * @param add
     * @return
     */
    public static String appendArray(String[] list, String add) {
        if (list == null || list.length == 0) {
            return "";
        }

        StringBuilder log = new StringBuilder(list[0]);
        for (int i = 1; i < list.length; ++i) {
            log.append("\n");
            log.append(list[i]);
        }

        return log.toString();
    }


}
