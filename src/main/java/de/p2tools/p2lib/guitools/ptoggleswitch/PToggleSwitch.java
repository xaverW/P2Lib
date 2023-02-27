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


package de.p2tools.p2lib.guitools.ptoggleswitch;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class PToggleSwitch extends HBox {

    private final CheckBox checkBox = new CheckBox();
    private final Label lblLeft = new Label();
    private Label lblRight = null;
    private boolean tglInFront = false;
    private boolean hGrow = true;
    private String strOn = "", strOff = "", strIndeterminate = "";
    private Label lblOn, lblOff, lblIndeterminate;
    private boolean bold = false;

    public PToggleSwitch() {
        super();
        init();
    }

    public PToggleSwitch(String text) {
        super();
        lblLeft.setText(text);
        init();
    }

    public PToggleSwitch(String text, boolean bold) {
        super();
        lblLeft.setText(text);
        this.bold = bold;
        init();
    }

    public PToggleSwitch(String text, boolean tglInFront, boolean hGrow) {
        super();
        lblLeft.setText(text);
        this.tglInFront = tglInFront;
        this.hGrow = hGrow;
        init();
    }

    public void setBold() {
        this.bold = true;
    }

    public void setLabelLeft(String lblOn, String lblOff, String lblIndeterminate) {
        //da wird der extern zugeführte Label gesetzt
        this.strOn = lblOn;
        this.strOff = lblOff;
        this.strIndeterminate = lblIndeterminate;

        indeterminateProperty().addListener((observable, oldValue, newValue) -> setLblLeft());
        selectedProperty().addListener((observable, oldValue, newValue) -> setLblLeft());
        setLblLeft();
    }

    private void setLblLeft() {
        if (this.isSelected()) {
            lblLeft.setText(strOn);
        } else if (this.isIndeterminate()) {
            lblLeft.setText(strIndeterminate);
        } else {
            lblLeft.setText(strOff);
        }
    }

    public void setLabelRight(Label lblOn, Label lblOff, Label lblIndeterminate) {
        //da wird der extern zugeführte Label gesetzt
        this.lblOn = lblOn;
        this.lblOff = lblOff;
        this.lblIndeterminate = lblIndeterminate;
        indeterminateProperty().addListener((observable, oldValue, newValue) -> setRight());
        selectedProperty().addListener((observable, oldValue, newValue) -> setRight());
        setRight();
    }

    public void setLabelRight(Label lblRight, String strOn, String strOff, String strIndeterminate) {
        //da wird der extern zugeführte Label gesetzt
        this.lblRight = lblRight;
        this.strOn = strOn;
        this.strOff = strOff;
        this.strIndeterminate = strIndeterminate;
        indeterminateProperty().addListener((observable, oldValue, newValue) -> setRight());
        selectedProperty().addListener((observable, oldValue, newValue) -> setRight());
        setRight();
    }

    private void setRight() {
        if (lblOn != null && lblOff != null && lblIndeterminate != null) {
            lblIndeterminate.setVisible(isIndeterminate());
            lblOn.setVisible(!isIndeterminate() && isSelected());
            lblOff.setVisible(!isIndeterminate() && !isSelected());
        }

        if (lblRight != null) {
            if (isIndeterminate()) {
                lblRight.setText(strIndeterminate);
            } else if (isSelected()) {
                lblRight.setText(strOn);
            } else {
                lblRight.setText(strOff);
            }
        }
    }

    public final BooleanProperty selectedProperty() {
        return checkBox.selectedProperty();
    }

    public final BooleanProperty checkBoxDisableProperty() {
        return checkBox.disableProperty();
    }

    public void setCheckBoxDisable(boolean value) {
        checkBox.setDisable(value);
    }

    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        return checkBox.isSelected();
    }

    public final BooleanProperty indeterminateProperty() {
        return checkBox.indeterminateProperty();
    }


    public final void setIndeterminate(boolean value) {
        checkBox.setIndeterminate(value);
    }

    public final boolean isIndeterminate() {
        return checkBox.isIndeterminate();
    }

    public final void setAllowIndeterminate(boolean value) {
        checkBox.setAllowIndeterminate(value);
    }

    public final void setText(String value) {
        lblLeft.setText(value);
    }

    public final String getText() {
        return lblLeft.getText();
    }

    public final void setTooltip(Tooltip value) {
        checkBox.setTooltip(value);
    }

    public final void setHGrow(boolean hGrow) {
        this.hGrow = hGrow;
        setCheckHgrow();
    }

    private void init() {
        this.setAlignment(Pos.CENTER_LEFT);

        this.setSpacing(15);
        //wenn mal gebraucht
//        if (!lblLeft.getText().isEmpty()) {
//            this.setSpacing(15);
//        }
//        lblLeft.textProperty().addListener((v, o, n) -> {
//            if (lblLeft.getText().isEmpty()) {
//                this.setSpacing(0);
//            } else {
//                this.setSpacing(15);
//
//            }
//        });
//        lblLeft.disableProperty().bindBidirectional(indeterminateProperty()); // ist vielleicht klarer??

        if (tglInFront) {
            lblLeft.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().addAll(checkBox, lblLeft);
        } else {
            lblLeft.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().addAll(lblLeft, checkBox);
        }
        setCheckHgrow();
        getStyleClass().add("pToggleSwitch");
        if (bold) {
            getStyleClass().add("pToggleSwitchBold");
        }
    }

    private void setCheckHgrow() {
        if (hGrow) {
            lblLeft.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lblLeft, Priority.ALWAYS);
        } else {
            HBox.setHgrow(lblLeft, Priority.NEVER);
            lblLeft.setMaxWidth(Region.USE_PREF_SIZE);
        }
    }
}