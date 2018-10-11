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


package de.p2tools.p2Lib.guiTools;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.time.FastDateFormat;

import java.awt.*;
import java.util.Date;

public class PGuiTools {
    private static final String STR = "__";
    private static final String FORMATTER_ddMMyyyy_str = STR + "yyyyMMdd";
    private static final String FORMATTER_ddMMyyyyHHmmss_str = STR + "yyyyMMdd_HHmmss";
    private static final FastDateFormat FORMATTER_ddMMyyyy = FastDateFormat.getInstance(FORMATTER_ddMMyyyy_str);
    private static final FastDateFormat FORMATTER_ddMMyyyyHHmmss = FastDateFormat.getInstance(FORMATTER_ddMMyyyyHHmmss_str);

    public static Region getHBoxGrower() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public static Region getVBoxGrower() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    /**
     * Center a component (e.g. Dialog) on screen
     *
     * @param component The reference component
     * @param absolute  if true, use absolute position, otherwise relative
     */
    public static void centerOnScreen(final Component component, final boolean absolute) {
        final int width = component.getWidth();
        final int height = component.getHeight();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (height / 2);
        if (!absolute) {
            x /= 2;
            y /= 2;
        }
        component.setLocation(x, y);
    }

    public static String getNextName(String name, String suffix) {
        String ret = name;
        String suff = suffix.startsWith(".") ? suffix : "." + suffix;

        final String date1 = FORMATTER_ddMMyyyy.format(new Date());
        final String date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date());

        final String s1 = getTime(name, suff, FORMATTER_ddMMyyyy);
        final String s2 = getTime(name, suff, FORMATTER_ddMMyyyyHHmmss);

        if (!name.endsWith(suff)) {
            ret = name + suff;

        } else if (!s1.isEmpty()) {
            ret = cleanName(name, suff);
            ret = ret.replace(s1, date2) + suff;

        } else if (!s2.isEmpty()) {
            ret = cleanName(name, suff);
            ret = ret.replace(s2, suff);

        } else if (name.endsWith(suff)) {
            ret = cleanName(name, suff);
            ret = ret + date1 + suff;
        }

        return ret;
    }

    private static String cleanName(String name, String suffix) {
        // falls sich da mehre MD5 angesammelt haben
        while (!name.equals(suffix) && name.endsWith(suffix)) {
            name = name.substring(0, name.lastIndexOf(suffix));
        }
        return name;
    }

    private static String getTime(String name, String suffix, FastDateFormat format) {
        String ret = "";
        Date d = null;
        if (name.contains(STR) && name.endsWith(suffix)) {
            try {
                ret = name.substring(name.lastIndexOf(STR)).replaceAll(suffix, "");
                d = new Date(format.parse(ret).getTime());
            } catch (Exception ignore) {
                d = null;
            }
        }

        if (d != null && format.getPattern().length() == ret.length()) {
            return ret;
        }

        return "";
    }
}
