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

import de.p2tools.p2lib.tools.P2Exception;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class P2Accordion {

    public static void initAccordionPane(Accordion accordion, IntegerProperty selectedPane) {

        accordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            TitledPane titledPane = accordion.getExpandedPane();

            ObservableList<TitledPane> accList = accordion.getPanes();
            for (int i = 0; i < accList.size(); ++i) {
                if (accList.get(i).equals(titledPane)) {
                    selectedPane.setValue(i);
                }
            }
        });

    }

    public static void setAccordionPane(Accordion accordion, IntegerProperty selectedPane) {
        if (selectedPane.get() < 0 && accordion.getPanes().size() == 1) {
            accordion.setExpandedPane(accordion.getPanes().get(0));
            return; // beim Programmstart, ist das übersichtlicher, nur wenn es einen gibt, dann anzuzeigen
        } else if (selectedPane.get() < 0) {
            return;
        }

        if (selectedPane.get() >= accordion.getPanes().size()) {
            P2Exception.throwPException(974125035, "setAccordionPane");
        }

        final int sel = selectedPane.get();
        accordion.setExpandedPane(accordion.getPanes().get(sel));
    }


    public static void setAccordion(boolean setAcc, Accordion accordion, VBox noAccordion, ScrollPane scrollPane,
                                    Collection<TitledPane> titledPanes, IntegerProperty selectedPane) {
        if (setAcc) {
            noAccordion.getChildren().clear();
            accordion.getPanes().addAll(titledPanes);
            scrollPane.setContent(accordion);
            setAccordionPane(accordion, selectedPane);

        } else {
            accordion.getPanes().clear();
            noAccordion.getChildren().addAll(titledPanes);
            titledPanes.stream().forEach(titledPane -> titledPane.setExpanded(true));
            scrollPane.setContent(noAccordion);
        }
    }
}
