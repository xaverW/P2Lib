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
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PColorList {

    private static final HashMap<String, PColorData> HASHMAP = new HashMap<>();

    static String getTagName() {
        return "system-color";
    }

    public static PData getPData() {
        PData cd = new PData() {
            @Override
            public String getTag() {
                return PColorList.getTagName();
            }

            @Override
            public String getComment() {
                return "system color";
            }

            @Override
            public ArrayList<Config> getConfigsArr() {
                return PColorList.getConfigsArr();
            }
        };
        return cd;
    }

    static ArrayList<Config> getConfigsArr() {
        final LinkedList<String[]> liste = new LinkedList<>();
        for (PColorData c : HASHMAP.values()) {
            liste.add(new String[]{c.getKey(), PColorData.getColorToWeb(c.getColorReset()), c.getColorToWeb()});
        }
        listeSort(liste, 0);

        ArrayList<Config> arr = new ArrayList<>(HASHMAP.size());
        for (String[] sArr : liste) {
            arr.add(new ConfigColor(sArr[0], sArr[1], sArr[2]));
        }

        return arr;
    }


    private static class ConfigColor extends Config {
        public ConfigColor(String key, String initValue, String actValue) {
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

            for (PColorData c : HASHMAP.values()) {
                if (c.getKey().equals(getKey())) {
                    c.setColorFromHex(getActValueString());
                    continue;
                }
            }

        }
    }

    public static PColorData get(String key) {
        return HASHMAP.get(key);
    }

    public static synchronized PColorData addNewKey(String key, Color color, String text) {
        PColorData c = new PColorData(key, color, text);
        HASHMAP.put(key, c);
        return c;
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
