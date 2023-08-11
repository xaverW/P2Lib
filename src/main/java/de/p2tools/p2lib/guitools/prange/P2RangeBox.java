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


package de.p2tools.p2lib.guitools.prange;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.controlsfx.control.RangeSlider;

public class P2RangeBox extends VBox {

    private final String STR_ALLES = "Alles";
    private final int MIN_VALUE;
    private final int MAX_VALUE;
    private IntegerProperty minValueProp = new SimpleIntegerProperty();
    private IntegerProperty maxValueProp = new SimpleIntegerProperty();

    private String unitSuffix = " Minuten";
    private final RangeSlider slider;
    private final Label lblTime = new Label();
    private final String title;

    public P2RangeBox(String title, boolean titleTop, int min, int max) {
        this.title = title;
        this.MIN_VALUE = min;
        this.MAX_VALUE = max;
        minValueProp.setValue(min);
        maxValueProp.setValue(max);
        this.slider = new RangeSlider(minValueProp.getValue(), maxValueProp.getValue(),
                minValueProp.getValue(), maxValueProp.getValue());
        if (titleTop) {
            createSliderTop();
        } else {
            createSlider();
        }
    }

    public P2RangeBox(String title, boolean titleTop, int min, int max, IntegerProperty minValue, IntegerProperty maxValue) {
        this.title = title;
        this.MIN_VALUE = min;
        this.MAX_VALUE = max;
        minValueProp = minValue;
        maxValueProp = maxValue;
        this.slider = new RangeSlider(minValue.getValue(), maxValue.getValue(),
                minValue.getValue(), maxValue.getValue());
        if (titleTop) {
            createSliderTop();
        } else {
            createSlider();
        }
    }

    private void createSliderTop() {
        this.setSpacing(2);
        this.setMinHeight(Region.USE_PREF_SIZE);

        initSlider();
        setRangeTxt();

        HBox box = new HBox(P2LibConst.DIST_GRIDPANE_HGAP);
        box.setPadding(new Insets(0));
        box.getChildren().addAll(new Label(title), P2GuiTools.getHBoxGrower(), lblTime);
        this.getChildren().addAll(box, slider);
    }

    private void createSlider() {
        this.setSpacing(0);
        this.setMinHeight(Region.USE_PREF_SIZE);
        this.setPadding(new Insets(0));

        initSlider();
        slider.setPrefWidth(400);
        setRangeTxt();

        HBox hBox = new HBox(P2LibConst.DIST_GRIDPANE_HGAP);
        hBox.setPadding(new Insets(0));
        HBox.setHgrow(slider, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        if (title.isEmpty()) {
            hBox.getChildren().addAll(slider, P2GuiTools.getVDistance(20), getStack());
        } else {
            hBox.getChildren().addAll(new Label(title), P2GuiTools.getVDistance(10),
                    slider, P2GuiTools.getVDistance(20), getStack());
        }
        getChildren().add(hBox);
    }

    private StackPane getStack() {
        Label l = new Label("Von " + MAX_VALUE + " bis " + MAX_VALUE + unitSuffix); // ist der längste Label
        l.setVisible(false);
        StackPane stackPane = new StackPane(); // um die Breite konstant zu halten :)
        stackPane.getChildren().addAll(l, lblTime);
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        return stackPane;
    }

    private void initSlider() {
        slider.lowValueProperty().bindBidirectional(minValueProp);
        slider.highValueProperty().bindBidirectional(maxValueProp);
        slider.highValueProperty().addListener((u, o, n) -> setRangeTxt());
        slider.lowValueProperty().addListener((u, o, n) -> setRangeTxt());
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(10);
    }

    public int getActMinValue() {
        return minValueProp.get();
    }

    public IntegerProperty minValueProperty() {
        return minValueProp;
    }

    public int getActMaxValue() {
        return maxValueProp.get();
    }

    public IntegerProperty maxValueProperty() {
        return maxValueProp;
    }

    private void setRangeTxt() {
        int minIntSlider = (int) slider.getLowValue();
        int maxIntSlider = (int) slider.getHighValue();

        final String text;
        if (minIntSlider == MIN_VALUE && maxIntSlider == MAX_VALUE) {
            text = STR_ALLES;
        } else if (minIntSlider == MIN_VALUE) {
            text = "Max. " + maxIntSlider + unitSuffix;
        } else if (maxIntSlider == MAX_VALUE) {
            text = "Min. " + minIntSlider + unitSuffix;
        } else {
            text = "Von " + minIntSlider + " bis " + maxIntSlider + unitSuffix;
        }

        lblTime.setText(text);
    }
}
