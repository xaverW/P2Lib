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


package de.p2tools.p2lib.checkforupdates;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoAlert {

    private final ProgUpdateData progUpdateData;
    private final ProgUpdateData progUpdateDataBeta;
    private final ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList;
    private final int VERSION_PADDING_T = 5;
    private final int VERSION_PADDING_B = 10;

    private final CheckBox chkShowUpdateAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkShowUpdateBetaAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkSearchUpdate = new CheckBox("nach Programmupdates suchen");
    private final CheckBox chkSearchUpdateBeta = new CheckBox("nach Programmupdates suchen");
    private final boolean newVersion, newVersionBeta;
    private final Stage stage;
    BooleanProperty searchForUpdate = null;
    BooleanProperty searchForUpdateBeta = null;
    BooleanProperty showUpdateAgain = null;
    BooleanProperty showUpdateBetaAgain = null;
    private boolean newInfos = false;


    public InfoAlert(Stage stage,
                     ProgUpdateData progUpdateData, ProgUpdateData progUpdateDataBeta,
                     ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
                     boolean newVersion, boolean newVersionBeta,
                     BooleanProperty searchForUpdate, BooleanProperty searchForUpdateBeta,
                     BooleanProperty showUpdateAgain, BooleanProperty showUpdateBetaAgain) {

        this.stage = stage;
        this.progUpdateData = progUpdateData;
        this.progUpdateDataBeta = progUpdateDataBeta;
        this.newProgUpdateInfoDataList = newProgUpdateInfoDataList;
        this.newVersion = newVersion;
        this.newVersionBeta = newVersionBeta;
        this.searchForUpdate = searchForUpdate;
        this.searchForUpdateBeta = searchForUpdateBeta;
        this.showUpdateAgain = showUpdateAgain;
        this.showUpdateBetaAgain = showUpdateBetaAgain;
    }

    public InfoAlert(Stage stage,
                     ProgUpdateData progUpdateData, ProgUpdateData progUpdateDataBeta,
                     ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
                     boolean newVersion, boolean newVersionBeta) {

        this.stage = stage;
        this.progUpdateData = progUpdateData;
        this.progUpdateDataBeta = progUpdateDataBeta;
        this.newProgUpdateInfoDataList = newProgUpdateInfoDataList;
        this.newVersion = newVersion;
        this.newVersionBeta = newVersionBeta;
    }

    public boolean showInfoAlert(String header) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle("Updates");
        alert.setHeaderText(header);
        alert.setResizable(true);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER_RIGHT);
        alert.getDialogPane().setContent(vBox);

        Tab tabVersion = addTabVersion();
        Tab tabBeta = addTabBeta();
        Tab tabInfos = addTabInfo();

        List<Tab> tabList = new ArrayList<>();
        if (tabVersion != null) {
            tabList.add(tabVersion);
        }
        if (tabBeta != null) {
            tabList.add(tabBeta);
        }
        if (tabInfos != null) {
            tabList.add(tabInfos);
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
            if (newInfos && tabInfos != null) {
                tabPane.getSelectionModel().select(tabInfos);
            }
            vBox.getChildren().add(tabPane);
            VBox.setVgrow(tabPane, Priority.ALWAYS);
        }

        final Optional<ButtonType> bt = alert.showAndWait();
        return bt.isPresent() && bt.get() == ButtonType.OK;
    }

    private Tab addTabVersion() {
        if (progUpdateData == null) {
            return null;
        }

        final Tab tabVersion = new Tab("neue Version");
        if (!newVersion) {
            tabVersion.setText("aktuelle Version");
        }

        progUpdateData.setVersionText(progUpdateData.getProgVersion() + "");
        if (newVersion) {
            progUpdateData.setShowText(progUpdateData.getProgReleaseNotes());
        } else {
            progUpdateData.setShowText(P2LibConst.LINE_SEPARATOR + "Sie benutzen die aktuelle Version.");
        }

        InfoAlertFactory.makeTab(stage, progUpdateData, tabVersion, showUpdateAgain, searchForUpdate,
                chkSearchUpdate, chkShowUpdateAgain, newVersion);
        tabVersion.setClosable(false);
        return tabVersion;
    }

    private Tab addTabBeta() {
        if (progUpdateDataBeta == null) {
            return null;
        }

        final Tab tabBeta = new Tab("neue BETA");
        if (!newVersionBeta) {
            tabBeta.setText("aktuelle BETA");
        }
        progUpdateDataBeta.setVersionText(progUpdateDataBeta.getProgVersion() +
                "   (Build " + progUpdateDataBeta.getProgBuildNo() + " vom " + progUpdateDataBeta.getProgBuildDate() + ")");
        if (newVersionBeta) {
            progUpdateDataBeta.setShowText(progUpdateDataBeta.getProgReleaseNotes());
        } else {
            progUpdateDataBeta.setShowText(P2LibConst.LINE_SEPARATOR + "Es gibt keine aktuellere Beta-Version.");
        }

        InfoAlertFactory.makeTab(stage, progUpdateDataBeta, tabBeta, showUpdateBetaAgain, searchForUpdateBeta,
                chkSearchUpdateBeta, chkShowUpdateBetaAgain, newVersionBeta);
        tabBeta.setClosable(false);
        return tabBeta;
    }

    private Tab addTabInfo() {
        if (progUpdateData == null || progUpdateData.getInfos().isEmpty()) {
            return null;
        }

        final Tab tabInfos = new Tab("Programminfos");
        makeTabInfos(tabInfos);
        tabInfos.setClosable(false);
        return tabInfos;
    }


    private void makeTabInfos(Tab tabInfos) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        for (int i = progUpdateData.getInfos().size() - 1; i >= 0; --i) {
            ProgUpdateInfoData progUpdateInfoData = progUpdateData.getInfos().get(i);
            TextArea textArea = new TextArea(progUpdateInfoData.getInfo());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            gridPane.add(textArea, 0, row++);

            if (newProgUpdateInfoDataList.contains(progUpdateInfoData)) {
                // gibt dann neue Infos
                newInfos = true;
            } else {
                // dann ist eine alte Info: Grau
                textArea.setStyle("-fx-text-fill: gray;");
            }
        }

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());
        scrollPane.setContent(gridPane);
        tabInfos.setContent(scrollPane);
    }
}
