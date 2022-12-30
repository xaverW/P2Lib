/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.configFile.pConfData;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigColorProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PColorDataProps extends PDataSample<PColorData> {

    public static final String TAG = "colorData";

    private StringProperty key = new SimpleStringProperty("");
    private BooleanProperty use = new SimpleBooleanProperty(true);
    private ObjectProperty<Color> colorLight = new SimpleObjectProperty<>(this, "color", Color.WHITE);
    private ObjectProperty<Color> colorDark = new SimpleObjectProperty<>(this, "color", Color.WHITE);

    public PColorDataProps() {
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("key", "key", key));
        list.add(new ConfigBoolPropExtra("use", "use", use));
        list.add(new ConfigColorProp("colorLight", "colorLight", colorLight));
        list.add(new ConfigColorProp("colorDark", "colorDark", colorDark));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public boolean isUse() {
        return use.get();
    }

    public BooleanProperty useProperty() {
        return use;
    }

    public void setUse(boolean use) {
        this.use.set(use);
    }

    public Color getColorLight() {
        return colorLight.get();
    }

    public ObjectProperty<Color> colorLightProperty() {
        return colorLight;
    }

    public void setColorLight(Color colorLight) {
        this.colorLight.set(colorLight);
    }

    public Color getColorDark() {
        return colorDark.get();
    }

    public ObjectProperty<Color> colorDarkProperty() {
        return colorDark;
    }

    public void setColorDark(Color colorDark) {
        this.colorDark.set(colorDark);
    }
}


