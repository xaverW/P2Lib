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
import java.util.Collections;
import java.util.List;

public class PRandom {

    /**
     * get a random int from 0 ... max-1
     *
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    /**
     * get a random int between begin ... end
     *
     * @param begin
     * @param end
     * @return
     */
    public static int getRandom(int begin, int end) {
        return (int) (Math.random() * (end + 1 - begin)) + begin;
    }


    /**
     * get a randomized list
     * containing Interger from 0 .. listSize-1
     *
     * @param listSize
     * @return
     */
    public static List<Integer> getShuffleList(int listSize) {
        List<Integer> arrayList = new ArrayList<>(listSize);
        if (listSize <= 0) {
            return arrayList;
        }

        for (int i = 0; i < listSize; ++i) {
            arrayList.add(i);
        }

        Collections.shuffle(arrayList);
        return arrayList;
    }

    /**
     * get a randomized list with size=listSize
     * containing Interger from 0 .. maxInt
     *
     * @param listSize
     * @param maxInt
     * @return
     */
    public static List<Integer> getShuffleList(int listSize, int maxInt) {

        List<Integer> arrayList = new ArrayList<>(listSize);
        if (listSize <= 0) {
            return arrayList;
        }

        if (listSize <= maxInt) {
            return getShuffleList(listSize);
        }

        int max = 0;
        for (int i = 0; i < listSize; ++i) {
            arrayList.add(max);
            ++max;
            if (max > maxInt) {
                max = 0;
            }
        }

        Collections.shuffle(arrayList);
        return arrayList;
    }
}
