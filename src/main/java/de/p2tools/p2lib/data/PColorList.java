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


package de.p2tools.p2lib.data;

import de.p2tools.p2lib.configfile.pdata.PData;
import de.p2tools.p2lib.configfile.pdata.PDataList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

public class PColorList extends SimpleListProperty<PColorData> implements PDataList<PColorData> {

    public static final String TAG = "pColorList";
    private static PColorList instance;

    public PColorList() {
        super(FXCollections.observableArrayList());
    }

    public synchronized static final PColorList getInst() {
        return instance == null ? instance = new PColorList() : instance;
    }

    public static void resetAllColor() {
        getInst().stream().forEach(c -> c.resetColor());
    }

    public static PColorData get(String key) {
        return getInst().get(key);
    }

    public static synchronized PColorData addNewKey(String key, Color color, String text) {
        PColorData c = new PColorData(key, color, color, true, text);
        getInst().add(c);
        return c;
    }

    public static synchronized PColorData addNewKey(String key, Color color, Color colorDark, String text) {
        PColorData c = new PColorData(key, color, colorDark, true, text);
        getInst().add(c);
        return c;
    }

    public static synchronized PColorData addNewKey(String key, Color color, Color colorDark,
                                                    boolean use, String text) {
        PColorData c = new PColorData(key, color, colorDark, use, text);
        getInst().add(c);
        return c;
    }

    public static synchronized PColorData addNewKey(String key, Color color, Color colorDark,
                                                    boolean use, String text, int mark) {
        PColorData c = new PColorData(key, color, colorDark, use, text);
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
    public PData getNewItem() {
        return new PColorData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(PColorData.class)) {
            stream().forEach(p -> {
                if (p.getKey().equals(((PColorData) obj).getKey())) {
                    //dann ist sie es :)
                    p.setColorLight(((PColorData) obj).getColorLight());
                    p.setColorDark(((PColorData) obj).getColorDark());
                    p.setUse(((PColorData) obj).isUse());
                }
            });
        }
    }
}
