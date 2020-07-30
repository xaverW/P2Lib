/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.tools.shortcut;

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class PShortcutWorker {
    private PShortcutWorker() {

    }

    public static void addShortCut(MenuItem menuItem, PShortcut shortcut) {
        setShortcut(menuItem, shortcut);
        shortcut.actShortcutProperty().addListener(c -> setShortcut(menuItem, shortcut));
    }

    private static void setShortcut(MenuItem menuItem, PShortcut shortcut) {
        try {
            final KeyCombination keyComb = KeyCodeCombination.valueOf(shortcut.getActShortcut());
            menuItem.setAccelerator(keyComb);
        } catch (Exception ex) {
            shortcut.resetShortcut();
            final KeyCombination keyComb = KeyCodeCombination.valueOf(shortcut.getActShortcut());
            menuItem.setAccelerator(keyComb);
            PLog.errorLog(915252687, ex);
        }
    }
}
