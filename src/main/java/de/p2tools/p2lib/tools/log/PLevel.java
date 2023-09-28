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

public class PLevel extends Level {
    public static final Level DURATION = new PLevel("DURATION", Level.INFO.intValue() + 1);
    public static final Level DEBUG = new PLevel("DEBUG", Level.INFO.intValue() + 2);
    public static final Level EXT_TOOL_MSG = new PLevel("EXT_TOOL", Level.INFO.intValue() + 3);

    public PLevel(String name, int value) {
        super(name, value);
    }
}
