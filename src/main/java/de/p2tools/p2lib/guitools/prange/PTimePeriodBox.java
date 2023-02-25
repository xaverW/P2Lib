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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PTimePeriodBox extends VBox {
    public static final int TIME_MAX_SEC = 24 * 60 * 60 - 1;
    private final String pattern = "HH:mm";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    private final int MIN_DIST = 15; // min einstellbarer Wert [min] und Tick im Slider

    private final int MIN_VALUE = 0;
    private final int MAX_VALUE = 24 * 60 / MIN_DIST; // Max MIN_DIST
    private final int MIN_DIST_SEC = 15 * 60; // min einstellbarer Wert [s]

    private final IntegerProperty minValue = new SimpleIntegerProperty();
    private final IntegerProperty maxValue = new SimpleIntegerProperty();

    private final SplitMenuButton menuButton = new SplitMenuButton();
    private final Slider sliderMin = new Slider();
    private final Slider sliderMax = new Slider();
    private final Label lblMin = new Label("Von:");
    private final Label lblMax = new Label("Bis:");
    private final Label lblValueMin = new Label();
    private final Label lblValueMax = new Label();
    private String vluePrefix = "Dauer: ";


    public PTimePeriodBox() {
        setPropValue(minValue, MIN_VALUE);
        setPropValue(maxValue, MAX_VALUE);

        this.minValue.setValue(MIN_VALUE * MIN_DIST);
        this.maxValue.setValue(MAX_VALUE * MIN_DIST);

        init();
        initSlider();
    }

    public PTimePeriodBox(IntegerProperty minValue, IntegerProperty maxValue) {
        this.minValue.bindBidirectional(minValue);
        this.maxValue.bindBidirectional(maxValue);

        init();
        initSlider();
    }

    public final void setTooltip(Tooltip value) {
        menuButton.setTooltip(value);
    }

    public int getMinValue() {
        return minValue.get();
    }

    public IntegerProperty minValueProperty() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue.set(minValue);
    }

    public int getMaxValue() {
        return maxValue.get();
    }

    public IntegerProperty maxValueProperty() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue.set(maxValue);
    }

    public String getVluePrefix() {
        return vluePrefix;
    }

    public void setVluePrefix(String vluePrefix) {
        this.vluePrefix = vluePrefix;
        setTimeTxt();
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(0));

        HBox.setHgrow(menuButton, Priority.ALWAYS);
        menuButton.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().addAll(menuButton);

        menuButton.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                resetSlider();
            }
        });

        menuButton.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                if (menuButton.isShowing()) {
                    menuButton.hide();
                } else {
                    menuButton.show();
                }
            }
        });

        sliderMin.setMinWidth(300);
        sliderMax.setMinWidth(300);

        GridPane gridPane1 = new GridPane();
        gridPane1.setHgap(10);
        gridPane1.setVgap(10);
        gridPane1.add(lblMin, 0, 0);
        gridPane1.add(sliderMin, 1, 0);
        gridPane1.add(lblValueMin, 2, 0);

        GridPane gridPane2 = new GridPane();
        gridPane2.setHgap(10);
        gridPane2.setVgap(10);
        gridPane2.add(lblMax, 0, 0);
        gridPane2.add(sliderMax, 1, 0);
        gridPane2.add(lblValueMax, 2, 0);

        CustomMenuItem cmi1 = new CustomMenuItem(gridPane1);
        CustomMenuItem cmi2 = new CustomMenuItem(gridPane2);
        cmi1.setHideOnClick(false);
        cmi2.setHideOnClick(false);
        menuButton.getItems().addAll(cmi1, cmi2);

//        menuButton.getStyleClass().add("pRangeButton");
//        getStyleClass().add("pTimePeriodBox");
    }

    private void initSlider() {
        // slider MIN
        sliderMin.setMin(MIN_VALUE);
        sliderMin.setMax(MAX_VALUE - 1);
        initSlider(sliderMin, minValue);
        // kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        sliderMin.setValue(minValue.getValue() / MIN_DIST_SEC);

        minValue.addListener(l -> {
            sliderMin.setValue(minValue.getValue() / MIN_DIST_SEC);
        });
        sliderMin.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                setPropValue(minValue, (int) sliderMin.getValue());
                repairMaxValue();
            }
        });

        // slider MAX
        sliderMax.setMin(MIN_VALUE + 1);
        sliderMax.setMax(MAX_VALUE);

        initSlider(sliderMax, maxValue);
        // kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        sliderMax.setValue(maxValue.getValue() / MIN_DIST_SEC);

        maxValue.addListener(l -> {
            sliderMax.setValue(maxValue.getValue() / MIN_DIST_SEC);
        });
        sliderMax.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                setPropValue(maxValue, (int) sliderMax.getValue());
                repairMinValue();
            }
        });
        setTimeTxt();
    }

    private void resetSlider() {
        setPropValue(minValue, MIN_VALUE);
        setPropValue(maxValue, MAX_VALUE);
        setTimeTxt();
    }

    private void initSlider(Slider slider, IntegerProperty ip) {
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(1); // TickUnit / TickCount+1
        slider.setMajorTickUnit(4 * 60 / MIN_DIST); // 4h
        slider.setBlockIncrement(1); // MIN_DIST
        slider.setSnapToTicks(false);
        slider.setShowTickLabels(true);

        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double x) {
                int i = x.intValue();
                i = i * MIN_DIST / 60; //hh:mm
                return i + "";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });

        slider.valueProperty().addListener(l -> setTimeTxt());
    }

    private void repairMinValue() {
        int low = (int) sliderMin.getValue();
        int height = (int) sliderMax.getValue();
        if (low >= height) {
            low = height - 1;
            low = low < 0 ? 0 : low;
            sliderMin.setValue(low);
            setPropValue(minValue, low);
        }
    }

    private void repairMaxValue() {
        int low = (int) sliderMin.getValue();
        int height = (int) sliderMax.getValue();
        if (height <= low) {
            height = low + 1;
            height = height > MAX_VALUE ? MAX_VALUE : height;
            sliderMax.setValue(height);
            setPropValue(maxValue, height);
        }
    }

    private void setPropValue(IntegerProperty ip, int value) {
        ip.setValue(value * MIN_DIST_SEC);
    }

    private void setTimeTxt() {
        final String STR_ALLES = "alles";
        int iLowSecond = (int) sliderMin.getValue() * MIN_DIST_SEC;
        LocalTime lt = LocalTime.ofSecondOfDay(iLowSecond);
        String timeL = lt.format(formatter);

        int iHiSecond = (int) sliderMax.getValue() * MIN_DIST_SEC;
        iHiSecond = iHiSecond > TIME_MAX_SEC ? TIME_MAX_SEC : iHiSecond;
        lt = LocalTime.ofSecondOfDay(iHiSecond);
        String timeH = iHiSecond == TIME_MAX_SEC ? "24:00" : lt.format(formatter);

        if (iLowSecond == 0 && iHiSecond == TIME_MAX_SEC) {
            final String text = vluePrefix + STR_ALLES;
            menuButton.setText(text);
        } else {
            final String text = vluePrefix + "von " + timeL + " bis " + timeH + " Uhr";
            menuButton.setText(text);
        }
    }

}
