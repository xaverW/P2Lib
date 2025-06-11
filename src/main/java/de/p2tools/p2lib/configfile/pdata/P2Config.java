/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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


package de.p2tools.p2lib.configfile.pdata;

import de.p2tools.p2lib.tools.GermanStringSorter;

import java.util.HashMap;
import java.util.LinkedList;

public class P2Config {

    private static final HashMap<String, P2Configs> HASHMAP = new HashMap<>();

    public static P2Configs get(String key) {
        return HASHMAP.get(key);
    }

    public static synchronized void check(P2Configs p2Configs, int min, int max) {
        final int v = p2Configs.getInt();
        if (v < min || v > max) {
            p2Configs.setValue(p2Configs.getInitValue());
        }
    }

    public static synchronized P2Configs addNewKey(String key) {
        P2Configs c = new P2Configs(key);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized P2Configs addNewKey(String key, String value) {
        P2Configs c = new P2Configs(key, value == null ? "" : value);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized P2Configs addNewKey(String key, int value) {
        P2Configs c = new P2Configs(key, value);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized String[][] getAll() {
        final LinkedList<String[]> list = new LinkedList<>();
        for (P2Configs c : HASHMAP.values()) {
            list.add(new String[]{c.getKey(), c.getActValue().getValueSafe()});
        }

        listSort(list, 0);
        return list.toArray(new String[][]{});
    }

    private static void listSort(LinkedList<String[]> list, int index) {
        // Stringliste alphabetisch sortieren
        final GermanStringSorter sorter = GermanStringSorter.getInstance();
        if (list == null) {
            return;
        }

        for (int i = 1; i < list.size(); ++i) {
            for (int k = i; k > 0; --k) {
                final String str1 = list.get(k - 1)[index];
                final String str2 = list.get(k)[index];
                if (sorter.compare(str1, str2) > 0) {
                    list.add(k - 1, list.remove(k));
                } else {
                    break;
                }
            }
        }
    }
}
