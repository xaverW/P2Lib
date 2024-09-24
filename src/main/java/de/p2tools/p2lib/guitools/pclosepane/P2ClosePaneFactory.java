/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.guitools.pclosepane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class P2ClosePaneFactory {
    private P2ClosePaneFactory() {
    }

    public static Tab makeTabH(Pane pane, String title, BooleanProperty tabProp, BooleanProperty ripProp) {
        P2ClosePaneH closePaneH = new P2ClosePaneH();
        closePaneH.getButtonClose().setOnAction(a -> tabProp.set(false));
        closePaneH.getButtonRip().setOnAction(a -> ripProp.set(!ripProp.get()));
        closePaneH.addPane(pane);
        Tab tab = new Tab(title);
        tab.setClosable(false);
        tab.setContent(closePaneH);
        return tab;
    }

    public static Tab makeTabV(Pane pane, String title, BooleanProperty tabProp, BooleanProperty ripProp) {
        P2ClosePaneV closePaneH = new P2ClosePaneV();
        closePaneH.getButtonClose().setOnAction(a -> tabProp.set(false));
        closePaneH.getButtonRip().setOnAction(a -> ripProp.set(!ripProp.get()));
        closePaneH.addPane(pane);
        Tab tab = new Tab(title);
        tab.setClosable(false);
        tab.setContent(closePaneH);
        return tab;
    }

    public static void setSplit(BooleanProperty bound, SplitPane splitPane,
                                P2InfoController p2InfoController, boolean infoFirst,
                                Region pane, DoubleProperty divider, BooleanProperty isShowing) {
        // hier wird der Filter ein- ausgeblendet
        if (bound.get() && splitPane.getItems().size() > 1) {
            bound.set(false);
            splitPane.getDividers().get(0).positionProperty().unbindBidirectional(divider);
        }

        splitPane.getItems().clear();
        if (!p2InfoController.arePanesShowing()) {
            // dann wird nix angezeigt
            splitPane.getItems().add(pane);
            isShowing.set(false);
            return;
        }

        if (isShowing.getValue()) {
            bound.set(true);
            if (infoFirst) {
                splitPane.getItems().addAll(p2InfoController, pane);
            } else {
                splitPane.getItems().addAll(pane, p2InfoController);
            }
            SplitPane.setResizableWithParent(p2InfoController, false);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(divider);

        } else {
            splitPane.getItems().add(pane);
        }
    }
}
