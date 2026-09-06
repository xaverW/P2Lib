/*
 * MTViewer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
package de.p2tools.p2lib.gui;


import de.p2tools.p2lib.guitools.P2LinuxJavaFx;
import javafx.application.Platform;

public class Main {

    public static void main(final String args[]) {
        new Main().start(args);
    }

    private void start(String... args) {
        if (P2LinuxJavaFx.hasJavaFx()) {
            startGuiFxMode(args);
        }
    }


    private void startGuiFxMode(final String[] args) {
        Platform.setImplicitExit(false);
        P2.main(args);
    }
}
