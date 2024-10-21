/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class P2ClosePaneController extends VBox {

    private final TabPane tabPane = new TabPane();
    private final BooleanProperty INFO_IS_SHOWING;
    private final List<P2ClosePaneDto> infoDTOList;

    public P2ClosePaneController(List<P2ClosePaneDto> infoDTOList, BooleanProperty INFO_IS_SHOWING) {
        this.INFO_IS_SHOWING = INFO_IS_SHOWING;
        this.infoDTOList = infoDTOList;
        initInfoPane();
    }

    public boolean arePanesShowing() {
        // dann wird wenigsten eins angezeigt
        for (P2ClosePaneDto infoDTO : infoDTOList) {
            if (!infoDTO.PANE_INFO_IS_RIP.get()) {
                return true;
            }
        }
        return false;
    }

    private void initInfoPane() {
        for (P2ClosePaneDto infoDTO : infoDTOList) {
            if (infoDTO.PANE_INFO_IS_RIP.get()) {
                dialogInfo(infoDTO);
            }
            infoDTO.PANE_INFO_IS_RIP.addListener((u, o, n) -> {
                if (n) {
                    dialogInfo(infoDTO);
                } else {
                    INFO_IS_SHOWING.set(true);
                }
                setTabs();
            });
        }

        setTabs();
    }

    private void dialogInfo(P2ClosePaneDto infoDTO) {
        new P2ClosePaneDialog(infoDTO.infoPane, infoDTO.textDialog,
                infoDTO.DIALOG_INFO_SIZE,
                infoDTO.PANE_INFO_IS_RIP,
                infoDTO.tabOn,
                infoDTO.maskerVis);
    }

    private void setTabs() {
        tabPane.getTabs().clear();


        for (P2ClosePaneDto infoDTO : infoDTOList) {
            if (!infoDTO.PANE_INFO_IS_RIP.get()) {
                tabPane.getTabs().add(infoDTO.vertical ?
                        P2ClosePaneFactory.makeTabV(infoDTO.infoPane, infoDTO.textInfo,
                                INFO_IS_SHOWING, infoDTO.PANE_INFO_IS_RIP) :
                        P2ClosePaneFactory.makeTabH(infoDTO.infoPane, infoDTO.textInfo,
                                INFO_IS_SHOWING, infoDTO.PANE_INFO_IS_RIP));
            }
        }

        if (tabPane.getTabs().isEmpty()) {
            // keine Tabs
            INFO_IS_SHOWING.set(false);

        } else if (tabPane.getTabs().size() == 1) {
            // dann gibts einen Tab
            final Node node = tabPane.getTabs().get(0).getContent();
            tabPane.getTabs().remove(0);
            getChildren().setAll(node);
            VBox.setVgrow(node, Priority.ALWAYS);

        } else {
            // dann gibts mehre Tabs
            getChildren().setAll(tabPane);
            VBox.setVgrow(tabPane, Priority.ALWAYS);
        }
    }
}
