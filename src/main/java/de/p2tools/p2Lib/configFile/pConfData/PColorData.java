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

    private final double DIV = 0.4;
    private final double DIV_DARK = 0.6;
    public static final String SEPARATOR = "X";

    private String text = "";
    private String cssFontBold = "";
    private String cssFont = "";
    private String cssBackground = "";
    private String cssBackgroundAndSel = "";

    private boolean dark = false;
    private Color resetColorLight;
    private Color resetColorDark;

    public PColorData() {
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

    public void setColor(Color newColor) {
        if (dark) {
            super.setColorDark(newColor);
        } else {
            super.setColorLight(newColor);
        }
        changeMyColor();
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

//    public void setColorFromHex(String hex) {
//        if (hex.contains(SEPARATOR)) {
//            String[] arr = hex.split(SEPARATOR);
//            super.setColorLight(Color.web(arr[0]));
//            super.setColorDark(Color.web(arr[1]));
//            if (arr.length > 2) {
//                setUse(Boolean.parseBoolean(arr[2]));
//            }
//
//        } else {
//            setColor(Color.web(hex));
//        }
//        changeMyColor();
//    }

    //==========================================================
    // sind die CSS Farben
    //==========================================================
    public String getCssBackground() {
        return cssBackground;
    }

    public String getCssFont() {
        return cssFont;
    }

    public String getCssFontBold() {
        return cssFontBold;
    }

    public String getCssBackgroundAndSel() {
        return cssBackgroundAndSel;
    }

    //==========================================================
    //CSS erstellen
    //==========================================================
//    private String getColorLightToHex() {
//        return PColorFactory.getColorToHex(getColorLight());
//    }
//
//    private String getColorDarkToHex() {
//        return PColorFactory.getColorToHex(getColorDark());
//    }

    public String getColorSelectedToWeb() {
        return "#" + PColorFactory.getColorToHex(dark ? getColorDark() : getColorLight());
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
        cssFontBold = ("-fx-font-weight: bold; -fx-text-fill: " + getColorSelectedToWeb() + ";").intern();
        cssFont = ("-fx-text-fill: " + getColorSelectedToWeb() + ";").intern();
        cssBackground = ("-fx-control-inner-background: " + getColorSelectedToWeb() + ";").intern();
        cssBackgroundAndSel = ("-fx-control-inner-background: " + getColorSelectedToWeb() + ";" +
                "-fx-selection-bar: " + getColorDarkerToWeb() + ";" +
                " -fx-selection-bar-non-focused: " + getColorDarkerToWeb() + ";").intern();
    }
}
