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


package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.tools.PDateUtils;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class PYearPicker extends Spinner<Integer> {

    private IntegerProperty integerProperty = null;
    public final int PYEAR_PICKER_EMPTY = 0;


    public PYearPicker() {
        init(PDateUtils.getAktYearInt());
    }

    public PYearPicker(int year, IntegerProperty integerProperty) {
        this.integerProperty = integerProperty;
        init(year);
    }

    public void setYearPicker(int year) {
        this.getValueFactory().setValue(year);
    }

    public void setProperty(IntegerProperty integerProperty) {
        if (this.integerProperty != null) {
            this.integerProperty.unbind();
        }
        this.integerProperty = integerProperty;
        this.integerProperty.bind(this.valueProperty());
    }

    public void clearYearPicker() {
        this.getValueFactory().setValue(PYEAR_PICKER_EMPTY);
    }

    public String getYearPicker() {
        return this.valueFactoryProperty().getValue() + "";
    }

    private void init(int year) {
        this.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1950, 2050, year);
        this.setValueFactory(valueFactory);
        this.getValueFactory().setValue(year);
        if (this.integerProperty != null) {
            this.integerProperty.bind(this.valueProperty());
        }
    }
}
