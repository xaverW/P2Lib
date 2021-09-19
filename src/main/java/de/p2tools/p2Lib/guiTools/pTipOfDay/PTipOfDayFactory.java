/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.guiTools.pToolTip;

import java.util.List;

public class PTipOfDayFactory {
    public static final String TOOL_TIP_WAS_SHOWN = "S";
    public static final String TOOL_TIP_WAS_NOT_SHOWN = "N";

    public static String getToolTipShownString(List<PTipOfDay> list) {
        String shown = "";
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).isWasShown()) {
                shown += PTipOfDayFactory.TOOL_TIP_WAS_SHOWN;
            } else {
                shown += PTipOfDayFactory.TOOL_TIP_WAS_NOT_SHOWN;
            }
        }
        return shown;
    }

    public static boolean containsToolTipNotShown(String shown, int listSize) {
        if (shown.isEmpty()) {
            return true;
        }

        if (shown.length() < listSize) {
            //dann hat sich die Anzahl der ToolTips geändert
            return true;
        }

        for (int i = 0; i < shown.length(); ++i) {
            if (shown.startsWith(PTipOfDayFactory.TOOL_TIP_WAS_NOT_SHOWN, i)) {
                return true;
            }
        }

        return false;
    }

    public static void setToolTipsFromShownString(List<PTipOfDay> list, String shown) {
        while (shown.length() < list.size()) {
            shown += PTipOfDayFactory.TOOL_TIP_WAS_NOT_SHOWN;
        }

        for (int i = 0; i < list.size(); ++i) {
            if (shown.startsWith(PTipOfDayFactory.TOOL_TIP_WAS_SHOWN, i)) {
                list.get(i).setWasShown(true);
            } else {
                list.get(i).setWasShown(false);
            }
        }
    }
}
