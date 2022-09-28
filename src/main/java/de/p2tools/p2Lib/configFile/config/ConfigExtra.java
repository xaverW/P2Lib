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


package de.p2tools.p2Lib.configFile.config;

/**
 * this is the class for one configuration
 * for example: the configsData for a USER(name,age,size)
 * then it has 3 Config: one for the "name", "age", "size"
 * a config can store the info in a STRING or a PROPERTY
 */

public abstract class ConfigExtra extends Config {

    String name;
    String regEx = ""; // damit kann eine Usereingebe als *falsch* markiert werden z.B. PLT muss 5 Zahlen haben

    public ConfigExtra() {
        super();
        this.name = "";
    }

    public ConfigExtra(String key, String name) {
        super(key);
        this.name = name;
    }

    public ConfigExtra(String key, String name, Object actValue) {
        super(key, actValue);
        this.name = name;
    }

    public ConfigExtra(String key, String name, String regEx, Object actValue) {
        super(key, actValue);
        this.name = name;
        this.regEx = regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


