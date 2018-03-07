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


package de.p2tools.p2Lib.configFile.pConfData;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.tools.GermanStringSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PConfList {

    private static final HashMap<String, PConfData> HASHMAP = new HashMap<>();

    static String getTagName() {
        return "system";
    }

    public static PData getPData() {
        PData cd = new PData() {
            @Override
            public String getTag() {
                return PConfList.getTagName();
            }

            @Override
            public String getComment() {
                return "system settings";
            }

            @Override
            public ArrayList<Config> getConfigsArr() {

                final LinkedList<String[]> liste = new LinkedList<>();
                for (PConfData c : HASHMAP.values()) {
                    liste.add(new String[]{c.getKey(), c.getAktValue().getValueSafe()});
                }
                listeSort(liste, 0);

                ArrayList<Config> arr = new ArrayList<>(HASHMAP.size());
                for (String[] sArr : liste) {
                    arr.add(new ConfigsProg(sArr[0], sArr[1], sArr[1]));
                }

                return arr;
            }
        };
        return cd;
    }

//    static ArrayList<Config> getConfigsArr() {
//        final LinkedList<String[]> liste = new LinkedList<>();
//        for (PConfData c : HASHMAP.values()) {
//            liste.add(new String[]{c.getKey(), c.getAktValue().getValueSafe()});
//        }
//        listeSort(liste, 0);
//
//        ArrayList<Config> arr = new ArrayList<>(HASHMAP.size());
//        for (String[] sArr : liste) {
//            arr.add(new ConfigsProg(sArr[0], sArr[1], sArr[1]));
//        }
//
//        return arr;
//    }


    private static class ConfigsProg extends Config {


        public ConfigsProg(String key, String initValue, String actValue) {
            super(key, initValue, actValue);
        }

        public String getActValue() {
            return super.getActValueString();
        }

        public String getActValueString() {
            return super.getActValueString();
        }

        public void setActValue(String act) {
            super.setActValue(act);

            for (PConfData c : HASHMAP.values()) {
                if (c.getKey().equals(getKey())) {
                    c.setValue(getActValueString());
                    continue;
                }
            }

        }
    }

    public static PConfData get(String key) {
        return HASHMAP.get(key);
    }

    public static synchronized void check(PConfData pConfigs, int min, int max) {
        final int v = pConfigs.getInt();
        if (v < min || v > max) {
            pConfigs.setValue(pConfigs.getInitValue());
        }
    }

    public static synchronized PConfData addNewKey(String key) {
        PConfData c = new PConfData(key);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized PConfData addNewKey(String key, String value) {
        PConfData c = new PConfData(key, value == null ? "" : value);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized PConfData addNewKey(String key, int value) {
        PConfData c = new PConfData(key, value);
        HASHMAP.put(key, c);
        return c;
    }

    public static synchronized String[][] getAll() {
        final LinkedList<String[]> liste = new LinkedList<>();
        for (PConfData c : HASHMAP.values()) {
            liste.add(new String[]{c.getKey(), c.getAktValue().getValueSafe()});
        }

        listeSort(liste, 0);
        return liste.toArray(new String[][]{});
    }

    private static void listeSort(LinkedList<String[]> liste, int stelle) {
        // Stringliste alphabetisch sortieren
        final GermanStringSorter sorter = GermanStringSorter.getInstance();
        if (liste == null) {
            return;
        }

        for (int i = 1; i < liste.size(); ++i) {
            for (int k = i; k > 0; --k) {
                final String str1 = liste.get(k - 1)[stelle];
                final String str2 = liste.get(k)[stelle];
                if (sorter.compare(str1, str2) > 0) {
                    liste.add(k - 1, liste.remove(k));
                } else {
                    break;
                }
            }
        }
    }
}
