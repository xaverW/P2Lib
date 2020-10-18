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


package de.p2tools.p2Lib.configFile.pConfData;

import de.p2tools.p2Lib.tools.PColorFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class PColorData {

    private String cssFontBold = "";
    private String cssFont = "";
    private String cssBackground = "";
    private String cssBackgroundSel = "";

    private String key;
    private String text = "";
    private Color colorReset = Color.WHITE;

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.WHITE);

    public PColorData(String key, Color color, String text) {
        setKey(key);
        setColor(color);
        setText(text);
        setColorReset(color);
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color newColor) {
        color.set(newColor);
        cssFontBold = "-fx-font-weight: bold; -fx-text-fill: " + getColorToWeb() + ";".intern();
        cssFont = "-fx-text-fill: " + getColorToWeb() + ";".intern();
        cssBackground = "-fx-control-inner-background: " + getColorToWeb() + ";"
                + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);".intern();
        cssBackgroundSel = ("-fx-control-inner-background: " + getColorToWeb() + ";" +
                "-fx-selection-bar: " + getColorDarkToWeb() + ";" +
                " -fx-selection-bar-non-focused: " + getColorDarkToWeb() + ";").intern();
    }


    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Color getColorReset() {
        return colorReset;
    }

    public void setColorReset(Color colorReset) {
        this.colorReset = colorReset;
    }

    public void resetColor() {
        setColor(getColorReset());
    }

    public void setColorFromHex(String hex) {
        setColor(Color.web(hex));
    }

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

    public String getColorToWeb() {
        return "#" + PColorFactory.getColorToHex(color.getValue());
    }

    public String getColorDarkToWeb() {
        return "#" + PColorFactory.getColorToHex(getDarkerColor(color.getValue()));
    }

    final double DIV = 0.3;

    private Color getDarkerColor(Color color) {
        Color c;
        double dist = color.getRed() < color.getGreen() ? color.getRed() : color.getGreen();
        dist = dist < color.getBlue() ? dist : color.getBlue();
        dist = 0.99 * dist;

        dist = dist < DIV ? dist : DIV;
        double red;
        double green;
        double blue;

        if (dist > 0.1) {
            red = color.getRed() - dist;
            green = color.getGreen() - dist;
            blue = color.getBlue() - dist;

        } else {
            // da ändert sich dann auch der Farbton
            red = color.getRed() > DIV ? (color.getRed() - DIV) : color.getRed();
            green = color.getGreen() > DIV ? (color.getGreen() - DIV) : color.getGreen();
            blue = color.getBlue() > DIV ? (color.getBlue() - DIV) : color.getBlue();
        }
        c = new Color(red, green, blue, color.getOpacity());
        return c;
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }
}
