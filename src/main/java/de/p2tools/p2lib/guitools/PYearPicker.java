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


package de.p2tools.p2lib.guitools;

import de.p2tools.p2lib.tools.date.PLDateFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Region;

public class PYearPicker extends Spinner<Integer> {

    private ObjectProperty<Integer> objInt = null;

    public PYearPicker() {
        init(PLDateFactory.getActYearInt());
    }

    public PYearPicker(int year, IntegerProperty integerProperty) {
        this.objInt = integerProperty.asObject();
        init(year);
    }

    public void setYearPicker(int year) {
        this.getValueFactory().setValue(year);
    }

    public void bindBidirectional(IntegerProperty integerProperty) {
        unbind();
        this.objInt = integerProperty.asObject();
        bindBidirectional();
    }

    private void bindBidirectional() {
        if (objInt != null) {
            this.getValueFactory().valueProperty().bindBidirectional(objInt);
        }
    }

    public void unbind() {
        if (objInt != null) {
            this.getValueFactory().valueProperty().unbindBidirectional(objInt);
        }
    }

    public String getYear() {
        return this.valueFactoryProperty().getValue() + "";
    }

    private void init(int year) {
        getStyleClass().add("PYearPicker");
//        final String CSS_FILE = "de/p2tools/p2lib/p2Css.css";
//        getStylesheets().add(CSS_FILE);
        this.setMinWidth(Region.USE_PREF_SIZE);

        this.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1950, 2050, year);
        this.setValueFactory(valueFactory);
        this.getValueFactory().setValue(year);
        bindBidirectional();
    }
}
