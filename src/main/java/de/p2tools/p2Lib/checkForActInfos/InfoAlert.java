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


package de.p2tools.p2Lib.checkForActInfos;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoAlert {
    FoundSearchData foundSearchData;

    public InfoAlert(FoundSearchData foundSearchData) {
        this.foundSearchData = foundSearchData;
    }

    public boolean showInfoAlert(String header) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (foundSearchData.getStage() != null) {
            alert.initOwner(foundSearchData.getStage());
        }
        alert.setTitle("Updates");
        alert.setHeaderText(header);
        alert.setResizable(true);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER_RIGHT);
        alert.getDialogPane().setContent(vBox);

        Tab tabInfos = InfoAlertsTabFactory.addTabInfo(foundSearchData);
        Tab tabVersion = InfoAlertsTabFactory.addTabVersion(foundSearchData);
        Tab tabBeta = InfoAlertsTabFactory.addTabBeta(foundSearchData, true);
        Tab tabDaily = InfoAlertsTabFactory.addTabBeta(foundSearchData, false);

        List<Tab> tabList = new ArrayList<>();
        if (tabInfos != null) {
            tabList.add(tabInfos);
        }
        if (tabVersion != null) {
            tabList.add(tabVersion);
        }
        if (tabBeta != null) {
            tabList.add(tabBeta);
        }
        if (tabDaily != null) {
            tabList.add(tabDaily);
        }

        if (tabList.isEmpty()) {
            return false;

        } else if (tabList.size() == 1) {
            Node cont = tabList.get(0).getContent();
            vBox.getChildren().add(cont);
            VBox.setVgrow(cont, Priority.ALWAYS);

        } else {
            final TabPane tabPane = new TabPane();
            tabPane.getTabs().addAll(tabList);
            if (tabInfos != null) {
                tabPane.getSelectionModel().select(tabInfos);
            }
            vBox.getChildren().add(tabPane);
            VBox.setVgrow(tabPane, Priority.ALWAYS);
        }

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
}
