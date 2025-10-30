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


package de.p2tools.p2lib.checkforactinfos;

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class InfoAlert extends P2DialogExtra {
    FoundSearchDataDTO foundSearchDataDTO;

    public InfoAlert(FoundSearchDataDTO foundSearchDataDTO) {
        super(foundSearchDataDTO.getStage(), null,
                "Updates", true, true, true, DECO.BORDER_SMALL);
        this.foundSearchDataDTO = foundSearchDataDTO;
        buildDialog();
        init(true);
    }

    private void buildDialog() {
        VBox vBox = getVBoxCont();

        Tab tabInfos = InfoAlertsTabFactory.addTabInfo(foundSearchDataDTO);
        Tab tabVersion = InfoAlertsTabFactory.addTabVersion(foundSearchDataDTO);
        Tab tabBeta = InfoAlertsTabFactory.addTabBeta(foundSearchDataDTO, true);
        Tab tabDaily = InfoAlertsTabFactory.addTabBeta(foundSearchDataDTO, false);

        List<Tab> tabList = new ArrayList<>();
        if (tabInfos != null) {
            tabList.add(tabInfos);
        }
        tabList.add(tabVersion);
        if (tabBeta != null) {
            tabList.add(tabBeta);
        }
        if (tabDaily != null) {
            tabList.add(tabDaily);
        }

        if (tabList.size() == 1) {
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

        Button btnOk = new Button("OK");
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());
    }
}
