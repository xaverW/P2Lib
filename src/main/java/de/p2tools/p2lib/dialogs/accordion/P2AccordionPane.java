/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.dialogs.accordion;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;

public class P2AccordionPane extends AnchorPane {

    private final Accordion accordion = new Accordion();
    private final HBox hBox = new HBox(0);
    private final CheckBox cbxAccordion = new CheckBox("");
    public Collection<TitledPane> titledPanes;
    private ScrollPane scrollPane = new ScrollPane();
    private VBox noaccordion = new VBox();
    private BooleanProperty accordionProp;
    private IntegerProperty selectedPane;

    public P2AccordionPane(BooleanProperty accordionProp, IntegerProperty selectedPane) {
        this.accordionProp = accordionProp;
        this.selectedPane = selectedPane;

        cbxAccordion.selectedProperty().bindBidirectional(accordionProp);
        cbxAccordion.selectedProperty().addListener((observable, oldValue, newValue) ->
                P2Accordion.setAccordion(cbxAccordion.isSelected(), accordion, noaccordion, scrollPane, titledPanes, selectedPane));

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        hBox.getChildren().addAll(cbxAccordion, scrollPane);

        getChildren().addAll(hBox);
        AnchorPane.setLeftAnchor(hBox, 10.0);
        AnchorPane.setBottomAnchor(hBox, 10.0);
        AnchorPane.setRightAnchor(hBox, 10.0);
        AnchorPane.setTopAnchor(hBox, 10.0);
    }

    public void init() {
        titledPanes = createPanes();
        P2Accordion.initAccordionPane(accordion, selectedPane);
        P2Accordion.setAccordion(cbxAccordion.isSelected(), accordion, noaccordion, scrollPane, titledPanes, selectedPane);
    }

    public void close() {
        cbxAccordion.selectedProperty().unbindBidirectional(accordionProp);
    }


    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();

        return result;
    }

    public HBox getHBox() {
        return hBox;
    }
}
