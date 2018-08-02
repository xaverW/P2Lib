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


package de.p2tools.p2Lib.guiTools.pTimePeriod;

import javafx.beans.property.IntegerProperty;
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
    public static final int IME_MAX_SEC = 24 * 60 * 60 - 1;
    private final String pattern = "HH:mm";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    private final int MIN_DIST = 15; // min einstellbarer Wert [min] und Tick im Slider

    private final int MIN_VALUE = 0;
    private final int MAX_VALUE = 24 * 60 / MIN_DIST; // Max MIN_DIST
    private final int MIN_DIST_SEC = 15 * 60; // min einstellbarer Wert [s]

    private final IntegerProperty minProperty;
    private final IntegerProperty maxProperty;

    private final SplitMenuButton menuButton = new SplitMenuButton();
    private final Slider sliderMin = new Slider();
    private final Slider sliderMax = new Slider();
    private final Label lblMin = new Label("Von:");
    private final Label lblMax = new Label("Bis:");
    private final Label lblValueMin = new Label();
    private final Label lblValueMax = new Label();
    private final Label lblTime = new Label();


    public PTimePeriodBox(IntegerProperty minProperty, IntegerProperty maxProperty) {
        this.minProperty = minProperty;
        this.maxProperty = maxProperty;

        init();
        initSlider();
    }

    public final void setTooltip(Tooltip value) {
        menuButton.setTooltip(value);
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(2));

        HBox.setHgrow(menuButton, Priority.ALWAYS);
        menuButton.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().addAll(lblTime, menuButton);

        menuButton.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
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

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(lblMin, 0, 0);
        gridPane.add(sliderMin, 1, 0);
        gridPane.add(lblValueMin, 2, 0);
        gridPane.add(lblMax, 0, 1);
        gridPane.add(sliderMax, 1, 1);
        gridPane.add(lblValueMax, 2, 1);

        CustomMenuItem cmi = new CustomMenuItem(gridPane);
        cmi.setHideOnClick(false);
        menuButton.getItems().add(cmi);

        getStyleClass().add("pTimePeriodBox");
        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pTimePeriodBox/pTimePeriodBox.css";
        getStylesheets().add(CSS_FILE);
    }

    private void initSlider() {
        sliderMin.setMin(MIN_VALUE);
        sliderMin.setMax(MAX_VALUE - 1);

        initSlider(sliderMin, minProperty);
        sliderMin.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                minProperty.setValue((int) sliderMin.getValue() * MIN_DIST_SEC);
                repairMaxValue();
                System.out.println("minProperty: " + minProperty.getValue() / 60);
            }
        });

        sliderMax.setMin(MIN_VALUE + 1);
        sliderMax.setMax(MAX_VALUE);

        initSlider(sliderMax, maxProperty);
        sliderMax.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                maxProperty.setValue((int) sliderMax.getValue() * MIN_DIST_SEC);
                repairMinValue();
                System.out.println("maxProperty: " + maxProperty.getValue() / 60);
            }
        });
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

        // kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        slider.setValue(ip.getValue() / MIN_DIST_SEC);

        ip.addListener(l -> {
            slider.setValue(ip.getValue() / MIN_DIST_SEC);
        });
        slider.valueProperty().addListener(l -> setLabel());
    }

    private void repairMinValue() {
        int low = (int) sliderMin.getValue();
        int height = (int) sliderMax.getValue();
        if (low >= height) {
            low = height - 1;
            low = low < 0 ? 0 : low;
            sliderMin.setValue(low);
        }
    }

    private void repairMaxValue() {
        int low = (int) sliderMin.getValue();
        int height = (int) sliderMax.getValue();
        if (height <= low) {
            height = low + 1;
            height = height > MAX_VALUE ? MAX_VALUE : height;
            sliderMax.setValue(height);
        }
    }

    private void setLabel() {
        final String STR_ALLES = "alles";
        int iLowSecond = (int) sliderMin.getValue() * MIN_DIST_SEC;
        LocalTime lt = LocalTime.ofSecondOfDay(iLowSecond);
        String timeL = lt.format(formatter);

        int iHiSecond = (int) sliderMax.getValue() * MIN_DIST_SEC;
        iHiSecond = iHiSecond > IME_MAX_SEC ? IME_MAX_SEC : iHiSecond;
        lt = LocalTime.ofSecondOfDay(iHiSecond);
        String timeH = iHiSecond == IME_MAX_SEC ? "24:00" : lt.format(formatter);

        if (iLowSecond == 0 && iHiSecond == IME_MAX_SEC) {
            lblTime.setText("Sendezeit: " + STR_ALLES);
        } else {
            lblTime.setText("Sendezeit: von " + timeL + " bis " + timeH);
        }

    }

}
