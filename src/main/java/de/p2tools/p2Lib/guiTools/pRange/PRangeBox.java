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


package de.p2tools.p2Lib.guiTools.pRange;

import de.p2tools.p2Lib.tools.PException;
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

public class PRangeBox extends VBox {

    private final int MIN_DIST = 5; //min einstellbarer Wert und Tick im Slider

    private final int MIN_VALUE;
    private final int MAX_VALUE; //Max MIN_DIST

    private final IntegerProperty minValue = new SimpleIntegerProperty();
    private final IntegerProperty maxValue = new SimpleIntegerProperty();

    private final SplitMenuButton menuButton = new SplitMenuButton();
    private final Slider sliderMin = new Slider();
    private final Slider sliderMax = new Slider();
    private final Label lblMin = new Label("Von:");
    private final Label lblMax = new Label("Bis:");
    private final Label lblValueMin = new Label();
    private final Label lblValueMax = new Label();
    private String valuePrefix = "Bereich: ";
    private String unitSuffix = " Minuten";


    public PRangeBox(int min, int max) {
        checkValues(min, max);

        MIN_VALUE = min / MIN_DIST;
        MAX_VALUE = max / MIN_DIST;

        minValue.setValue(min);
        maxValue.setValue(max);

        init();
        initSlider();
    }

    public PRangeBox(int min, int max, IntegerProperty minValue, IntegerProperty maxValue) {
        checkValues(min, max);

        MIN_VALUE = min / MIN_DIST;
        MAX_VALUE = max / MIN_DIST;

        minValue.bindBidirectional(minValue);
        maxValue.bindBidirectional(maxValue);

        init();
        initSlider();
    }

    private void checkValues(int min, int max) {
        if (max <= min) {
            PException.throwPException(951207534, "PRangeBox: max <= min");
        }
        if (min % MIN_DIST > 0) {
            PException.throwPException(978451236, "PRangeBox: min % MIN_DIST > 0");
        }
        if (max % MIN_DIST > 0) {
            PException.throwPException(987451036, "PRangeBox: max % MIN_DIST > 0");
        }
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

    public String getValuePrefix() {
        return valuePrefix;
    }

    public void setValuePrefix(String valuePrefix) {
        this.valuePrefix = valuePrefix;
        setRangeTxt();
    }

    public void setUnitSuffix(String unitSuffix) {
        this.unitSuffix = unitSuffix;
        setRangeTxt();
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
    }

    private void initSlider() {
        //slider MIN
        sliderMin.setMin(MIN_VALUE);
        sliderMin.setMax(MAX_VALUE - 1);
        initSlider(sliderMin, minValue);
        //kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        sliderMin.setValue(minValue.getValue() / MIN_DIST);

        minValue.addListener(l -> {
            sliderMin.setValue(minValue.getValue() / MIN_DIST);
        });

        sliderMin.valueProperty().addListener((o, oldV, newV) -> {
            if (!sliderMin.isValueChanging()) {
                setPropValue(minValue, (int) sliderMin.getValue());
                repairMaxValue();
            }
        });
        sliderMin.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                setPropValue(minValue, (int) sliderMin.getValue());
                repairMaxValue();
            }
        });


        //todo gibt Probleme wenn nur geklickt wird
//        sliderMin.valueProperty().addListener((observable, oldValue, newValue) -> {
//            setPropValue(minValue, (int) sliderMin.getValue());
//            repairMaxValue();
//        });


        //slider MAX
        sliderMax.setMin(MIN_VALUE + 1);
        sliderMax.setMax(MAX_VALUE);

        initSlider(sliderMax, maxValue);
        //kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        sliderMax.setValue(maxValue.getValue() / MIN_DIST);

        maxValue.addListener(l -> {
            sliderMax.setValue(maxValue.getValue() / MIN_DIST);
        });
        sliderMax.valueProperty().addListener((o, oldV, newV) -> {
            if (!sliderMax.isValueChanging()) {
                setPropValue(maxValue, (int) sliderMax.getValue());
                repairMinValue();
            }
        });
        sliderMax.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                setPropValue(maxValue, (int) sliderMax.getValue());
                repairMinValue();
            }
        });
        setRangeTxt();
    }

    private void resetSlider() {
        setPropValue(minValue, MIN_VALUE);
        setPropValue(maxValue, MAX_VALUE);
        setRangeTxt();
    }

    private void initSlider(Slider slider, IntegerProperty ip) {
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(1); //TickUnit / TickCount+1
        slider.setMajorTickUnit(50 / MIN_DIST); //4h
        slider.setBlockIncrement(1); //MIN_DIST
        slider.setSnapToTicks(false);
        slider.setShowTickLabels(true);

        slider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double x) {
                if (x == MIN_VALUE) return "alles";
                if (x == MAX_VALUE) return "alles";

                int i = x.intValue();
                i = i * MIN_DIST;
                return i + "";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });

        slider.valueProperty().addListener(l -> setRangeTxt());
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
        ip.setValue(value * MIN_DIST);
    }

    private void setRangeTxt() {
        final String STR_ALLES = "alles";

        int minIntSlider = (int) sliderMin.getValue();
        int maxIntSlider = (int) sliderMax.getValue();
        int minInt = (int) sliderMin.getValue() * MIN_DIST;
        int maxInt = (int) sliderMax.getValue() * MIN_DIST;

        final String text;
        if (minIntSlider == MIN_VALUE && maxIntSlider == MAX_VALUE) {
            text = valuePrefix + STR_ALLES;
        } else if (minIntSlider == MIN_VALUE) {
            text = valuePrefix + "max. " + maxInt + unitSuffix;
        } else if (maxIntSlider == MAX_VALUE) {
            text = valuePrefix + "min. " + minInt + unitSuffix;
        } else {
            text = valuePrefix + "von " + minInt + " bis " + maxInt + unitSuffix;
        }

        menuButton.setText(text);
    }

}
