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

    private final ProgInfo progInfo;
    private final ProgInfo progInfoBeta;
    private final ArrayList<Infos> newInfosList;

    private final CheckBox chkShowUpdateAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkShowUpdateBetaAgain = new CheckBox("dieses Update nochmals melden");
    private final CheckBox chkSearchUpdate = new CheckBox("nach Programmupdates suchen");
    private final CheckBox chkSearchUpdateBeta = new CheckBox("nach Programmupdates suchen");
    private final boolean newVersion;
    private final Stage stage;
    private boolean newInfos = false;
    BooleanProperty searchForUpdate = null;
    BooleanProperty searchForUpdateBeta = null;
    BooleanProperty showUpdateAgain = null;
    BooleanProperty showUpdateBetaAgain = null;


    public InfoAlert(Stage stage,
                     ProgInfo progInfo, ProgInfo progInfoBeta, ArrayList<Infos> newInfosList,
                     boolean newVersion, BooleanProperty searchForUpdate, BooleanProperty searchForUpdateBeta,
                     BooleanProperty showUpdateAgain, BooleanProperty showUpdateBetaAgain) {
        this.stage = stage;
        this.progInfo = progInfo;
        this.progInfoBeta = progInfoBeta;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
        this.searchForUpdate = searchForUpdate;
        this.searchForUpdateBeta = searchForUpdateBeta;
        this.showUpdateAgain = showUpdateAgain;
        this.showUpdateBetaAgain = showUpdateBetaAgain;
    }

    public InfoAlert(Stage stage, ProgInfo progInfo, ProgInfo progInfoBeta, ArrayList<Infos> newInfosList,
                     boolean newVersion) {
        this.stage = stage;
        this.progInfo = progInfo;
        this.progInfoBeta = progInfoBeta;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
    }

    public boolean showInfoAlert(String header) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle("Programminfos");
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
        if (progInfo == null) {
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
        if (progInfoBeta == null) {
            return null;
        }

        final Tab tabBeta = new Tab("neue BETA");
        makeTabBeta(tabBeta);
        tabBeta.setClosable(false);
        return tabBeta;
    }

    private Tab addTabInfo() {
        if (progInfo == null || progInfo.getInfos().isEmpty()) {
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

        Label txtVersion = new Label(progInfo.getProgVersion() + "");
        Hyperlink hyperlinkUrl = new PHyperlink(progInfo.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progInfo.getProgDownloadUrl());

        TextArea textArea = new TextArea();
        if (newVersion) {
            textArea.setText(progInfo.getProgReleaseNotes());
        } else {
            textArea.setText(P2LibConst.LINE_SEPARATOR + "Sie benutzen die aktuellste Version von " + progInfo.getProgName() + ".");
            textArea.setPrefRowCount(2);
        }

//        textArea.setMinHeight(150);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-URL:");
        final Label lblRel = new Label(newVersion ? "Änderungen:" : "");

        int row = 0;
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

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

        Label txtVersion = new Label(progInfoBeta.getProgVersion() + "");
        Label txtBuild = new Label(progInfoBeta.getProgBuildNo() + " vom " + progInfoBeta.getProgBuildDate());
        Hyperlink hyperlinkUrl = new PHyperlink(progInfoBeta.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progInfoBeta.getProgDownloadUrl());

        final Label lblVersion = new Label("Version:");
        final Label lblBuild = new Label("Build:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-URL:");
        final Label lblRel = new Label(newVersion ? "Änderungen:" : "");

        TextArea textAreaBeta = new TextArea();
        textAreaBeta.setWrapText(true);
        textAreaBeta.setEditable(false);
        GridPane.setVgrow(textAreaBeta, Priority.ALWAYS);

        String text = progInfoBeta.getProgReleaseNotes();
        textAreaBeta.setText(text);

        int row = 0;
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblBuild, 0, ++row);
        gridPane.add(txtBuild, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

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

    private void makeTabInfos(Tab tabInfos) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        for (int i = progInfo.getInfos().size() - 1; i >= 0; --i) {
            Infos infos = progInfo.getInfos().get(i);
            TextArea textArea = new TextArea(infos.getInfo());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            gridPane.add(textArea, 0, row++);

            if (newInfosList.contains(infos)) {
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
