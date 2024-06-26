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
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public class Config_colorProp extends Config {

    private ObjectProperty<Color> actValue;

    public Config_colorProp(String key, ObjectProperty<Color> actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_colorProp(String key, String name, ObjectProperty<Color> actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((Color) act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(Color.web(act));
        } catch (Exception ex) {
        }
    }

    @Override
    public Color getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return P2ColorFactory.getColorToWeb(actValue.getValue());
    }

    @Override
    public ObjectProperty getProperty() {
        return actValue;
    }
}
