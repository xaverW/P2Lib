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


package de.p2tools.p2lib.tools;

import javafx.scene.paint.Color;

public class P2ColorFactory {
    private P2ColorFactory() {
    }

    public static String getMaxColor(String color) {
        try {
            Color c = Color.valueOf(color);
            double r = c.getRed();
            double g = c.getGreen();
            double b = c.getBlue();
            double sum = r + g + b;
            if (sum > 1.5) {
                sum = 0;
            } else {
                sum = 1;
            }
            double opacity = c.getOpacity();

            Color cNew = new Color(sum, sum, sum, opacity);
            return P2ColorFactory.getColor(cNew);
        } catch (Exception ignore) {
            return "";
        }
    }

    public static String changeColor(String color, double change) {
        try {
            Color c = Color.valueOf(color);
            double r = c.getRed() * change;
            double g = c.getGreen() * change;
            double b = c.getBlue() * change;
            double opacity = c.getOpacity();

            Color cNew = new Color(r > 1 ? 1 : r,
                    g > 1 ? 1 : g,
                    b > 1 ? 1 : b,
                    opacity);
            return P2ColorFactory.getColor(cNew);
        } catch (Exception ignore) {
            return "";
        }
    }

    public static String getColor(String color) {
        try {
            Color c = Color.valueOf(color);
            return P2ColorFactory.getColor(c);
        } catch (Exception ignore) {
            return "";
        }
    }

    public static String getColor(Color color) {
        return "#" + colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue()
        );
    }

    public static String getColorToWeb(Color color) {
        return "#" + colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue())
                + colorChanelToHex(color.getOpacity()
        );
    }

    public static String getColorToHex(Color color) {
        return colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue())
                + colorChanelToHex(color.getOpacity()
        );
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }
}
