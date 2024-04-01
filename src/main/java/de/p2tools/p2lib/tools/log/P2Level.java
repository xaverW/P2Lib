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


package de.p2tools.p2lib.tools.log;

import java.util.logging.Level;

public class P2Level extends Level {
    public static final Level DURATION = new P2Level("DURATION", Level.INFO.intValue() + 1);
    public static final Level DEBUG = new P2Level("DEBUG", Level.INFO.intValue() + 2);
    public static final Level EXT_TOOL_MSG = new P2Level("EXT_TOOL", Level.INFO.intValue() + 3);

    public P2Level(String name, int value) {
        super(name, value);
    }
}
