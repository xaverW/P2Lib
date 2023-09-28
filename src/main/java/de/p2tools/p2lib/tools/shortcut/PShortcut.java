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


package de.p2tools.p2lib.tools.shortcut;

import javafx.beans.property.StringProperty;

public class PShortcut {
    private final StringProperty actShortcut;
    private String orgShortcut = "";
    private String description = "";
    private String longDescription = "";

    public PShortcut(StringProperty actShortcut, String orgShortcut, String description, String longDescription) {
        this.actShortcut = actShortcut;
        this.orgShortcut = orgShortcut;
        this.description = description;
        this.longDescription = longDescription;
    }

    public String getActShortcut() {
        return actShortcut.get();
    }

    public StringProperty actShortcutProperty() {
        return actShortcut;
    }

    public void setActShortcut(String actShortcut) {
        this.actShortcut.set(actShortcut);
    }

    public String getOrgShortcut() {
        return orgShortcut;
    }

    public void setOrgShortcut(String orgShortcut) {
        this.orgShortcut = orgShortcut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void resetShortcut() {
        this.actShortcut.setValue(this.orgShortcut);
    }
}
