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


package de.p2tools.p2lib.colordata;

import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

public class P2ColorList extends SimpleListProperty<P2ColorData> implements P2DataList<P2ColorData> {

    public static final String TAG = "pColorList";
    private static P2ColorList instance;

    public P2ColorList() {
        super(FXCollections.observableArrayList());
    }

    public synchronized static final P2ColorList getInst() {
        return instance == null ? instance = new P2ColorList() : instance;
    }

    public static void resetAllColor() {
        // die aktuell eingestellten Farben: dark oder light
        getInst().forEach(P2ColorData::resetColor);
    }

    public static void resetAllColorDarkLight() {
        // alle Farben: dark UND light
        getInst().forEach(P2ColorData::resetColorDarkLight);
    }

    public static P2ColorData get(String key) {
        return getInst().get(key);
    }

    public static synchronized P2ColorData addNewKey(String key, Color color, String text) {
        P2ColorData c = new P2ColorData(key, color, color, true, text);
        getInst().add(c);
        return c;
    }

    public static synchronized P2ColorData addNewKey(String key, Color color, Color colorDark, String text) {
        P2ColorData c = new P2ColorData(key, color, colorDark, true, text);
        getInst().add(c);
        return c;
    }

    public static synchronized P2ColorData addNewKey(String key, Color color, Color colorDark,
                                                     boolean use, String text) {
        P2ColorData c = new P2ColorData(key, color, colorDark, use, text);
        getInst().add(c);
        return c;
    }

    public static synchronized P2ColorData addNewKey(String key, Color color, Color colorDark,
                                                     boolean use, String text, int mark) {
        P2ColorData c = new P2ColorData(key, color, colorDark, use, text);
        c.setMark(mark);
        getInst().add(c);
        return c;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Farben";
    }

    @Override
    public P2Data getNewItem() {
        return new P2ColorData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(P2ColorData.class)) {
            stream().forEach(p -> {
                if (p.getKey().equals(((P2ColorData) obj).getKey())) {
                    //dann ist sie es :)
                    p.setColorLight(((P2ColorData) obj).getColorLight());
                    p.setColorDark(((P2ColorData) obj).getColorDark());
                    p.setUse(((P2ColorData) obj).isUse());
                }
            });
        }
    }
}
