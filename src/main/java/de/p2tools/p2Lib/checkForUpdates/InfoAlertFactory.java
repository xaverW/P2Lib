/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.mtDownload.DownloadFactory;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InfoAlertFactory {
    private static final int VERSION_PADDING_T = 5;
    private static final int VERSION_PADDING_B = 10;

    private InfoAlertFactory() {
    }

    public static void makeTab(Stage stage, ProgUpdateData progUpdateData, Tab tabVersion,
                               BooleanProperty showUpdateAgain, BooleanProperty searchForUpdate,
                               CheckBox chkSearchUpdate, CheckBox chkShowUpdateAgain, boolean newVers) {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label txtVersion = new Label(progUpdateData.getVersionText());
        Hyperlink hyperlinkUrl = new PHyperlink(progUpdateData.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progUpdateData.getProgDownloadUrl());

        TextArea textArea = new TextArea();
        textArea.setText(progUpdateData.getShowText());
        if (newVers) {
            textArea.setPrefRowCount(2);
        }

        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-Website:");
        final Label lblRel = new Label(newVers ? "Änderungen:" : "");

        int row = 0;
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(stage, progUpdateData, gridPane, row);

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

    private static int getButton(Stage stage, ProgUpdateData progUpdateData, GridPane gridPane, int row) {
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
}
