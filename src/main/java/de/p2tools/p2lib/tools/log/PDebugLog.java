/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.PStringUtils;

import java.util.List;

public class PDebugLog {

    public static final String LILNE1 = "############################################################";
    public static final String LILNE2 = "============================================================";
    public static final String LILNE3 = "------------------------------------------------------------";
    public static final String LILNE_EMPTY = "  ";

    public static final String START_MSG = "==> DEBUG: ";

    /*    empty line     */
    public static synchronized void emptyLine() {
        if (!P2LibConst.debug) {
            return;
        }

        PLogger.LogInfo("");
    }

    /*    Systeminfos     */
    public static synchronized void sysLog(String text) {
        if (!P2LibConst.debug) {
            return;
        }

        PLogger.LogInfo(START_MSG + text);
    }

    public static synchronized void sysLog(String text[]) {
        if (!P2LibConst.debug) {
            return;
        }

        String log = PStringUtils.appendArray(text, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }

        PLogger.LogInfo(START_MSG + log);
    }

    public static synchronized void sysLog(List<String> list) {
        if (!P2LibConst.debug) {
            return;
        }

        String log = PStringUtils.appendList(list, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }

        PLogger.LogInfo(START_MSG + log);
    }
}
