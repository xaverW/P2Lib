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

import de.p2tools.p2Lib.tools.PException;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class PAccordion {

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
        if (selectedPane.get() < 0) {
            return; // beim Start
        }

        if (selectedPane.get() >= accordion.getPanes().size()) {
            PException.throwPException(974125035, "setAccordionPane");
        }

        final int sel = selectedPane.get();
        accordion.setExpandedPane(accordion.getPanes().get(sel));
    }


}
