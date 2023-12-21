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

import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.date.P2DateFactory;
import de.p2tools.p2lib.tools.date.P2DateProperty;

public class Config_pDateProp extends Config {

    private P2DateProperty actValue;

    public Config_pDateProp(String key, P2DateProperty actValue) {
        super(key);
        this.actValue = actValue;
    }

    public Config_pDateProp(String key, String name, P2DateProperty actValue) {
        super(key, name);
        this.actValue = actValue;
    }

    @Override
    public void setActValue(Object act) {
        actValue.setValue((P2Date) act);
    }

    public void setActValue(P2Date act) {
        actValue.setValue(act);
    }

    @Override
    public void setActValue(String act) {
        try {
            actValue.setValue(new P2Date(act));
        } catch (Exception ex) {
            actValue.setValue(new P2Date());
        }
    }

    @Override
    public P2Date getActValue() {
        return actValue.getValue();
    }

    @Override
    public String getActValueString() {
        return getActValue() == null ? "" : P2DateFactory.toString(getActValue());
    }

    @Override
    public P2DateProperty getProperty() {
        return actValue;
    }
}
