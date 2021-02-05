/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class PColorData {

    private final double DIV = 0.4;
    private final double DIV_DARK = 0.6;
    public static final String SEPARATOR = "X";

    private String cssFontBold = "";
    private String cssFont = "";
    private String cssBackground = "";
    private String cssBackgroundSel = "";

    private boolean dark = false;

    private final String key;
    private final String text;
    private final Color resetColor;
    private final Color resetColorDark;
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.WHITE);
    private final ObjectProperty<Color> colorDark = new SimpleObjectProperty<>(this, "colorDark", Color.WHITE);

    public PColorData(String key, Color color, String text) {
        //todo
        this.key = key;
        this.resetColor = color;
        this.resetColorDark = colorDark.get();
        this.color.set(color);
        this.text = text;
        setColorTheme(dark);
    }

    public PColorData(String key, Color color, Color colorDark, String text) {
        //todo
        this.key = key;
        this.resetColor = color;
        this.resetColorDark = colorDark;
        this.color.set(color);
        this.colorDark.set(colorDark);
        this.text = text;
        setColorTheme(dark);
    }

    public void setColorTheme(boolean dark) {
        this.dark = dark;
        changeMyColor(dark ? colorDark.get() : color.get());
    }

    public Color getColor() {
        if (dark) {
            return colorDark.get();
        } else {
            return color.get();
        }
    }

    public Color getResetColorDark() {
        return colorDark.get();
    }

    public void setColor(Color newColor) {
        //sichern
        if (dark) {
            colorDark.set(newColor);
        } else {
            color.set(newColor);
        }
        //Farbe setzen
        changeMyColor(newColor);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public Color getResetColor() {
        return (dark ? resetColorDark : resetColor);
    }

    public void resetColor() {
        // set reset color
        setColor(dark ? resetColorDark : resetColor);
    }

    public void setColorFromHex(String hex) {
        if (hex.contains(SEPARATOR)) {
            String[] arr = hex.split(SEPARATOR);
            color.set(Color.web(arr[0]));
            colorDark.set(Color.web(arr[1]));
        } else {
            setColor(Color.web(hex));
        }
    }
    // ============================================
    // sind die CSS Farben
    // ============================================

    public String getCssBackground() {
        return cssBackground;
    }

    public String getCssBackgroundSel() {
        return cssBackgroundSel;
    }

    public String getCssFont() {
        return cssFont;
    }

    public String getCssFontBold() {
        return cssFontBold;
    }

    public String getDarkerColorToWeb() {
        return "#" + PColorFactory.getColorToHex(dark ? getBrighterColor(color.getValue()) : getDarkerColor(color.getValue()));
    }

    public String getActColorToWeb() {
        return "#" + PColorFactory.getColorToHex(dark ? colorDark.getValue() : color.getValue());
    }

    public String getColorToWeb() {
        return "#" + PColorFactory.getColorToHex(color.getValue());
    }

    public String getColorDarkToWeb() {
        return "#" + PColorFactory.getColorToHex(colorDark.getValue());
    }

    private String getColorToHex() {
        return PColorFactory.getColorToHex(color.getValue());
    }

    private void changeMyColor(Color newColor) {
        // build the css for the color
        cssFontBold = ("-fx-font-weight: bold; -fx-text-fill: " + getActColorToWeb() + ";").intern();
        cssFont = ("-fx-text-fill: " + getActColorToWeb() + ";").intern();
        cssBackground = ("-fx-control-inner-background: " + getActColorToWeb() + ";").intern();
        cssBackgroundSel = ("-fx-control-inner-background: " + getActColorToWeb() + ";" +
                "-fx-selection-bar: " + getDarkerColorToWeb() + ";" +
                " -fx-selection-bar-non-focused: " + getDarkerColorToWeb() + ";").intern();
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

}
