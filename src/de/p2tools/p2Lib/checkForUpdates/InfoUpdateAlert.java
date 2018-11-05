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

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class InfoUpdateAlert {

    private final ProgInfo progInfo;
    private final ArrayList<Infos> newInfosList;

    private final VBox vBox = new VBox(10);
    private final TabPane tabPane = new TabPane();
    private final Tab tabVersion = new Tab("Neue Version");
    private final Tab tabInfos = new Tab("Programminfos");
    private final CheckBox chkSearchUpdateInfo = new CheckBox("Nach Programmupdates suchen.");
    private final boolean newVersion;
    private final Stage stage;
    BooleanProperty bPropShowUpdateInfo = null;


    public InfoUpdateAlert(ProgInfo progInfo, ArrayList<Infos> newInfosList, boolean newVersion, BooleanProperty bPropShowUpdateInfo) {
        this.stage = PConst.primaryStage;
        this.progInfo = progInfo;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
        this.bPropShowUpdateInfo = bPropShowUpdateInfo;
    }

    public InfoUpdateAlert(Stage stage, ProgInfo progInfo, ArrayList<Infos> newInfosList, boolean newVersion, BooleanProperty bPropShowUpdateInfo) {
        this.stage = stage;
        this.progInfo = progInfo;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
        this.bPropShowUpdateInfo = bPropShowUpdateInfo;
    }

    public InfoUpdateAlert(Stage stage, ProgInfo progInfo, ArrayList<Infos> newInfosList, boolean newVersion) {
        this.stage = stage;
        this.progInfo = progInfo;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
    }

    public InfoUpdateAlert(ProgInfo progInfo, ArrayList<Infos> newInfosList, boolean newVersion) {
        this.stage = PConst.primaryStage;
        this.progInfo = progInfo;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
    }

    public boolean showInfoAlert(String title, String header) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setResizable(true);

        if (bPropShowUpdateInfo != null) {
            chkSearchUpdateInfo.selectedProperty().bindBidirectional(bPropShowUpdateInfo);
            chkSearchUpdateInfo.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER_RIGHT);
            VBox.setVgrow(tabPane, Priority.ALWAYS);
            vBox.getChildren().addAll(tabPane, chkSearchUpdateInfo);
            alert.getDialogPane().setContent(vBox);
        } else {
            alert.getDialogPane().setContent(tabPane);
        }

        addTabVersion();
        addTabInfo();

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private void addTabVersion() {
        if (!newVersion) {
            tabVersion.setText("aktuelle Version");
        }
        makeTabVersion();
        tabVersion.setClosable(false);
        tabPane.getTabs().add(tabVersion);
    }

    private void addTabInfo() {
        if (progInfo.getInfos().isEmpty()) {
            return;
        }

        makeTabInfos();
        tabInfos.setClosable(false);
        tabPane.getTabs().add(tabInfos);
        if (!newVersion) {
            tabPane.getSelectionModel().select(tabInfos);
        }
    }

    private void makeTabVersion() {
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
            textArea.setText(PConst.LINE_SEPARATOR + "Sie benutzen die neueste Version von " + progInfo.getProgName() + ".");
        }

        textArea.setMinHeight(150);
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

        gridPane.add(lblRel, 0, ++row);
        if (newVersion) {
            gridPane.add(textArea, 1, row);
        } else {
            gridPane.add(textArea, 0, row, 2, 1);
        }

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        scrollPane.setContent(gridPane);
        tabVersion.setContent(scrollPane);
    }

    private void makeTabInfos() {
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
            textArea.setMinHeight(150);
            textArea.setWrapText(true);
            textArea.setEditable(false);
            GridPane.setVgrow(textArea, Priority.ALWAYS);

            if (!newInfosList.contains(infos)) {
                if (infos.getInfoNr() == 1) {
                    // ist die Begrüßungsnachricht, macht nur beim ersten Start sinn
                    continue;
                }
                textArea.setStyle("-fx-text-fill: gray;");
            }

            gridPane.add(textArea, 0, row++);
        }

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());

        scrollPane.setContent(gridPane);
        tabInfos.setContent(scrollPane);
    }
}
