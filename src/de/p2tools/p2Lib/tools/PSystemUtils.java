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


package de.p2tools.p2Lib.tools;

import de.p2tools.p2Lib.tools.file.PFileUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class PSystemUtils {

    public static void copyToClipboard(String s) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
    }


    /**
     * Liefert den Standardpfad für Downloads.
     *
     * @return Standardpfad zu den Downloads.
     */
    public static String getStandardDownloadPath() {
        return PFileUtils.addsPath(PFileUtils.getHomePath(), "Downloads");
    }
}
