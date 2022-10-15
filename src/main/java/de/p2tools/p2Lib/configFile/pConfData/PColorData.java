/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.configFile.pConfData;

import de.p2tools.p2Lib.tools.PColorFactory;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public class PColorData extends PColorDataProps {

    public static double ODD_DIV = 0.16;

    private final double DIV = 0.4;
    private final double DIV_DARK = 0.6;
    private String text = "";
    private int mark = 0;
    private String cssFont = "";
    private String cssFontBold = "";
    private String cssBackground = "";
    private String cssBackgroundAndSel = "";

    private String cssFont_ODD = "";
    private String cssFontBold_ODD = "";
    private String cssBackground_ODD = "";
    private String cssBackgroundAndSel_ODD = "";

    private boolean dark = false;
    private Color resetColorLight;
    private Color resetColorDark;

    public PColorData() {
        changeMyColor();
    }

    public PColorData(String key, Color color, Color colorDark) {
        setKey(key);
        super.setColorLight(color);
        super.setColorDark(colorDark);
        this.resetColorLight = color;
        this.resetColorDark = colorDark;
        this.setUse(true);
        this.text = "";
        changeMyColor();
    }

    public PColorData(String key, Color color, Color colorDark, boolean use, String text) {
        setKey(key);
        super.setColorLight(color);
        super.setColorDark(colorDark);
        this.resetColorLight = color;
        this.resetColorDark = colorDark;
        this.setUse(use);
        this.text = text;
        changeMyColor();
    }

    @Override
    public int compareTo(PColorData data) {
        if (data == null) {
            return -1;
        }
        return this.getKey().compareTo(data.getKey());
    }

    public void setColorTheme(boolean dark) {
        this.dark = dark;
        changeMyColor();
    }

    public Color getColor() {
        if (dark) {
            return getColorDark();
        } else {
            return getColorLight();
        }
    }

    public void setColor(Color newColor) {
        if (dark) {
            super.setColorDark(newColor);
        } else {
            super.setColorLight(newColor);
        }
        changeMyColor();
    }

    public ObjectProperty<Color> colorProperty() {
        if (dark) {
            return colorDarkProperty();
        } else {
            return colorLightProperty();
        }
    }

    public Color getResetColorDark() {
        return resetColorDark;
    }

    public Color getResetColorLight() {
        return resetColorLight;
    }

    public Color getResetColor() {
        return (dark ? resetColorDark : resetColorLight);
    }

    public void setColorLight(Color newColor) {
        super.setColorLight(newColor);
        changeMyColor();
    }

    public void setColorDark(Color newColor) {
        super.setColorDark(newColor);
        changeMyColor();
    }

    public void resetColor() {
        if (dark) {
            super.setColorDark(resetColorDark);
        } else {
            super.setColorLight(resetColorLight);
        }
        changeMyColor();
    }

    public String getText() {
        return text;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    //==========================================================
    // sind die CSS Farben
    //==========================================================
    public String getCssFont() {
        return cssFont;
    }

    public String getCssFontBold() {
        return cssFontBold;
    }

    public String getCssBackground() {
        return cssBackground;
    }

    public String getCssBackgroundAndSel() {
        return cssBackgroundAndSel;
    }

    public String getCssFont_ODD() {
        return cssFont_ODD;
    }

    public String getCssFontBold_ODD() {
        return cssFontBold_ODD;
    }

    public String getCssBackground_ODD() {
        return cssBackground_ODD;
    }

    public String getCssBackgroundAndSel_ODD() {
        return cssBackgroundAndSel_ODD;
    }

    public String getCssFont(boolean odd) {
        return odd ? cssFont_ODD : cssFont;
    }

    public String getCssFontBold(boolean odd) {
        return odd ? cssFontBold_ODD : cssFontBold;
    }

    public String getCssBackground(boolean odd) {
        return odd ? cssBackground_ODD : cssBackground;
    }

    public String getCssBackgroundAndSel(boolean odd) {
        return odd ? cssBackgroundAndSel_ODD : cssBackgroundAndSel;
    }


    //==========================================================
    //CSS erstellen
    //==========================================================

    public String getColorSelectedToWeb() {
        return "#" + PColorFactory.getColorToHex(dark ? getColorDark() : getColorLight());
    }

    public String getColorSelectedToWeb(boolean odd) {
        if (odd) {
            return "#" + PColorFactory.getColorToHex(getColorOdd(dark ? getColorDark() : getColorLight()));
        } else {
            return "#" + PColorFactory.getColorToHex(dark ? getColorDark() : getColorLight());
        }
    }

    public String getColorLightToWeb() {
        return "#" + PColorFactory.getColorToHex(getColorLight());
    }

    public String getColorDarkToWeb() {
        return "#" + PColorFactory.getColorToHex(getColorDark());
    }

    public String getColorDarkerToWeb() {
        return "#" + PColorFactory.getColorToHex(dark ? getBrighterColor(getColorDark()) : getDarkerColor(getColorLight()));
    }

    private Color getDarkerColor(Color color) {
        Color c;
        double min = color.getRed() < color.getGreen() ? color.getRed() : color.getGreen();
        min = min < color.getBlue() ? min : color.getBlue();

        min = min < DIV ? min : DIV;
        double red;
        double green;
        double blue;
        double change = 0.99 * min;

        if (change > 0.2) {
            red = color.getRed() - change;
            green = color.getGreen() - change;
            blue = color.getBlue() - change;

        } else {
            // da ändert sich dann auch der Farbton
            red = color.getRed() > DIV ? (color.getRed() - DIV) : 0.0;
            green = color.getGreen() > DIV ? (color.getGreen() - DIV) : 0.0;
            blue = color.getBlue() > DIV ? (color.getBlue() - DIV) : 0.0;
        }
        c = new Color(red, green, blue, color.getOpacity());
        return c;
    }

    private Color getBrighterColor(Color color) {
        Color c;
        double max = color.getRed() > color.getGreen() ? color.getRed() : color.getGreen();
        max = max > color.getBlue() ? max : color.getBlue();

        max = max > DIV_DARK ? max : DIV_DARK;
        double red;
        double green;
        double blue;

        double change = 1.0 - max;
        change = 0.99 * change;

        if (change > 0.2) {
            red = color.getRed() + change;
            green = color.getGreen() + change;
            blue = color.getBlue() + change;

        } else {
            // da ändert sich dann auch der Farbton
            red = color.getRed() < DIV_DARK ? (color.getRed() + DIV) : 1;
            green = color.getGreen() < DIV_DARK ? (color.getGreen() + DIV) : 1;
            blue = color.getBlue() < DIV_DARK ? (color.getBlue() + DIV) : 1;
        }
        c = new Color(red, green, blue, color.getOpacity());
        return c;
    }

    private void changeMyColor() {
        // build the css for the color
        cssFont = ("-fx-text-fill: " + getColorSelectedToWeb() + ";").intern();
        cssFontBold = ("-fx-font-weight: bold; -fx-text-fill: " + getColorSelectedToWeb() + ";").intern();
        cssBackground = ("-fx-control-inner-background: " + getColorSelectedToWeb() + ";").intern();
        cssBackgroundAndSel = ("-fx-selection-bar: " + getColorSelectedToWeb() + ";" +
                " -fx-selection-bar-non-focused: " + getColorSelectedToWeb() + ";").intern();

        cssFont_ODD = ("-fx-text-fill: " + getColorSelectedToWeb(true) + ";").intern();
        cssFontBold_ODD = ("-fx-font-weight: bold; -fx-text-fill: " + getColorSelectedToWeb(true) + ";").intern();
        cssBackground_ODD = ("-fx-control-inner-background: " + getColorSelectedToWeb(true) + ";").intern();
        cssBackgroundAndSel_ODD = ("-fx-selection-bar: " + getColorSelectedToWeb(true) + ";" +
                " -fx-selection-bar-non-focused: " + getColorSelectedToWeb(true) + ";").intern();
    }

    private Color getColorOdd(Color c) {
        double ODD_1_DIV = 1 - ODD_DIV;
        if (c.getRed() < ODD_1_DIV && c.getGreen() < ODD_1_DIV && c.getBlue() < ODD_1_DIV) {
            return new Color(c.getRed() + ODD_DIV,
                    c.getGreen() + ODD_DIV,
                    c.getBlue() + ODD_DIV,
                    c.getOpacity());

        } else if (c.getRed() > ODD_DIV && c.getGreen() > ODD_DIV && c.getBlue() > ODD_DIV) {
            return new Color(c.getRed() - ODD_DIV,
                    c.getGreen() - ODD_DIV,
                    c.getBlue() - ODD_DIV,
                    c.getOpacity());

        } else {
            //MIST!
            return new Color(c.getRed() < ODD_1_DIV ? c.getRed() + ODD_DIV : 1.0,
                    c.getGreen() < ODD_1_DIV ? c.getGreen() + ODD_DIV : 1.0,
                    c.getBlue() < ODD_1_DIV ? c.getBlue() + ODD_DIV : 1.0,
                    c.getOpacity());
        }
    }
}
