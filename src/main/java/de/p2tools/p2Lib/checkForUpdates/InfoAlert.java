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


package de.p2tools.p2Lib.checkForUpdates;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.download.DownloadFactory;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

    private final CheckBox chkShowUpdateAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkShowUpdateBetaAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkSearchUpdate = new CheckBox("nach Programmupdates suchen");
    private final CheckBox chkSearchUpdateBeta = new CheckBox("nach Programmupdates suchen");
    private final boolean newVersion, newVersionBeta;
    private final Stage stage;
    private boolean newInfos = false;
    BooleanProperty searchForUpdate = null;
    BooleanProperty searchForUpdateBeta = null;
    BooleanProperty showUpdateAgain = null;
    BooleanProperty showUpdateBetaAgain = null;


    public InfoAlert(Stage stage,
                     ProgUpdateData progUpdateData, ProgUpdateData progUpdateDataBeta, ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
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

    public InfoAlert(Stage stage, ProgUpdateData progUpdateData, ProgUpdateData progUpdateDataBeta, ArrayList<ProgUpdateInfoData> newProgUpdateInfoDataList,
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
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private Tab addTabVersion() {
        if (progUpdateData == null) {
            return null;
        }

        final Tab tabVersion = new Tab("neue Version");
        if (!newVersion) {
            tabVersion.setText("aktuelle Version");
        }
        makeTabVersion(tabVersion);
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
        makeTabBeta(tabBeta);
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

    private void makeTabVersion(Tab tabVersion) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label txtVersion = new Label(progUpdateData.getProgVersion() + "");
        Hyperlink hyperlinkUrl = new PHyperlink(progUpdateData.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progUpdateData.getProgDownloadUrl());

        TextArea textArea = new TextArea();
        if (newVersion) {
            textArea.setText(progUpdateData.getProgReleaseNotes());
        } else {
            textArea.setText(P2LibConst.LINE_SEPARATOR + "Sie benutzen die aktuellste Version von MTPlayer.");
            textArea.setPrefRowCount(2);
        }

        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-Website:");
        final Label lblRel = new Label(newVersion ? "Änderungen:" : "");

        int row = 0;
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(progUpdateData, gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        if (showUpdateAgain != null || searchForUpdate != null) {
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            if (showUpdateAgain != null) {
                HBox hB = new HBox();
                HBox.setHgrow(hB, Priority.ALWAYS);
                chkShowUpdateAgain.selectedProperty().bindBidirectional(showUpdateAgain);
                hBox.getChildren().addAll(chkShowUpdateAgain, hB);
            }
            if (searchForUpdate != null) {
                chkSearchUpdate.selectedProperty().bindBidirectional(searchForUpdate);
                hBox.getChildren().add(chkSearchUpdate);
            }
            vBox.getChildren().addAll(hBox);
        }

        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
    }

    private void makeTabBeta(Tab tabVersion) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label txtVersion = new Label(progUpdateDataBeta.getProgVersion() + "");
        Label txtBuild = new Label(progUpdateDataBeta.getProgBuildNo() + " vom " + progUpdateDataBeta.getProgBuildDate());
        Hyperlink hyperlinkUrl = new PHyperlink(progUpdateDataBeta.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progUpdateDataBeta.getProgDownloadUrl());

        final Label lblVersion = new Label("Version:");
        final Label lblBuild = new Label("Build:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-Website:");
        final Label lblRel = new Label(newVersionBeta ? "Änderungen:" : "");

        TextArea textAreaBeta = new TextArea();
        textAreaBeta.setWrapText(true);
        textAreaBeta.setEditable(false);
        GridPane.setVgrow(textAreaBeta, Priority.ALWAYS);

        if (newVersionBeta) {
            textAreaBeta.setText(progUpdateDataBeta.getProgReleaseNotes());
        } else {
            textAreaBeta.setText(P2LibConst.LINE_SEPARATOR + "Es gibt keine aktuellere Beta-Version von MTPlayer.");
            textAreaBeta.setPrefRowCount(2);
        }

        int row = 0;
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblBuild, 0, ++row);
        gridPane.add(txtBuild, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(progUpdateDataBeta, gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textAreaBeta, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        if (showUpdateBetaAgain != null || searchForUpdateBeta != null) {
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            if (showUpdateBetaAgain != null) {
                HBox hB = new HBox();
                HBox.setHgrow(hB, Priority.ALWAYS);
                chkShowUpdateBetaAgain.selectedProperty().bindBidirectional(showUpdateBetaAgain);
                hBox.getChildren().addAll(chkShowUpdateBetaAgain, hB);
            }
            if (searchForUpdateBeta != null) {
                chkSearchUpdateBeta.selectedProperty().bindBidirectional(searchForUpdateBeta);
                hBox.getChildren().add(chkSearchUpdateBeta);
            }
            vBox.getChildren().addAll(hBox);
        }

        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
    }

    private int getButton(ProgUpdateData progUpdateData, GridPane gridPane, int row) {
        boolean done = false;
        for (String url : progUpdateData.getDownloads()) {
            Button button = new Button();
            button.setMaxWidth(Double.MAX_VALUE);
            String text = PUrlTools.getFileName(url);
            button.setText(text);
            button.setTooltip(new Tooltip(url));
            button.setOnAction(a -> {
                DownloadFactory.downloadFile(stage, url);
            });
            gridPane.add(button, 1, ++row);
            if (!done) {
                done = true;
                gridPane.add(new Label("Download:"), 0, row);
            }
        }
        return row;
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
