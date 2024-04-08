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


package de.p2tools.p2lib.configfile.config;

import de.p2tools.p2lib.tools.P2ColorFactory;
import javafx.scene.paint.Color;

public abstract class Config_color extends Config {

    private Color actValue;

    public Config_color(String key, String actValue) {
        super(key);
        this.actValue = Color.web(actValue);
    }

    public Config_color(String key, Color actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_color(String key, String actValue, boolean intern) {
        super(key, intern);
        this.actValue = Color.web(actValue);
    }

    @Override
    public Color getActValue() {
        return actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue = (Color) act;
        setUsedValue(actValue);
    }

    public void setActValue(Color act) {
        actValue = act;
        setUsedValue(actValue);
    }

    @Override
    public void setActValue(String act) {
        actValue = Color.web(act);
    }

    @Override
    public String getActValueString() {
        return P2ColorFactory.getColorToWeb(actValue);
    }

    public abstract void setUsedValue(Color value);
}
