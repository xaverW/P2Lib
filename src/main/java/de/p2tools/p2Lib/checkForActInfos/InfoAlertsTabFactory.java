/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.download.DownloadFactory;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InfoAlertsTabFactory {
    private static final int VERSION_PADDING_T = 5;
    private static final int VERSION_PADDING_B = 10;

    public static Tab addTabInfo(FoundSearchData foundSearchData) {
        if (!foundSearchData.isFoundNewInfo() || foundSearchData.getFoundFileListInfo().isEmpty()) {
            return null;
        }

        return makeTabInfos(foundSearchData.getFoundFileListInfo());
    }

    private static Tab makeTabInfos(FoundFileList foundFileList) {
        final Tab tabInfos = new Tab("Programminfos");
        tabInfos.setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        VBox vBox = new VBox(10);
        for (int i = foundFileList.size() - 1; i >= 0; --i) {
            FoundFile foundFile = foundFileList.get(i);
            TextArea textArea = new TextArea(foundFile.getFileText());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            vBox.getChildren().add(textArea);
            VBox.setVgrow(textArea, Priority.ALWAYS);
        }

        scrollPane.setContent(vBox);
        tabInfos.setContent(scrollPane);
        return tabInfos;
    }

    public static Tab addTabVersion(FoundSearchData foundSearchData) {
        //der wird immer angezeigt
        return makeTabVersion(foundSearchData.getStage(), foundSearchData);
    }

    private static Tab makeTabVersion(Stage stage, FoundSearchData foundSearchData) {
        final Tab tabVersion = new Tab("neue Version");
        if (!foundSearchData.isFoundNewVersion()) {
            tabVersion.setText("aktuelle Version");
        }
        tabVersion.setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(6);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        if (foundSearchData.isFoundNewVersion()) {
            //gibt eine aktuellere Version
            textArea.setText(foundSearchData.getNewVersionText());
        } else {
            //keine neue Version
            textArea.setText("Sie benutzen die aktuellste Version.");
        }

        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-Website:");

        final Label lblRel = new Label(foundSearchData.isFoundNewVersion() ? "Änderungen:" : "");
        final Label txtVersion = new Label(foundSearchData.isFoundNewVersion() ?
                foundSearchData.getNewVersionNo() : foundSearchData.getProgVersion());

        final Hyperlink hyperlinkUrl = new PHyperlink(foundSearchData.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new PHyperlink(foundSearchData.getUrlDownload());

        int row = 0;
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(stage, foundSearchData.getFoundFileListAct(), gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        if (foundSearchData.isFoundNewVersion()) {
            CheckBox chkShowUpdateAgain = new CheckBox("dieses Update nochmal anzeigen");
            chkShowUpdateAgain.selectedProperty().bindBidirectional(foundSearchData.searchActAgainProperty());
            HBox hB = new HBox();
            HBox.setHgrow(hB, Priority.ALWAYS);
            hBox.getChildren().addAll(chkShowUpdateAgain, hB);
        }
        CheckBox chkSearchUpdate = new CheckBox("beim Programmstart nach Updates suchen");
        chkSearchUpdate.selectedProperty().bindBidirectional(foundSearchData.searchActProperty());
        hBox.getChildren().add(chkSearchUpdate);

        vBox.getChildren().addAll(hBox);
        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
        return tabVersion;
    }

    public static Tab addTabBeta(FoundSearchData foundSearchData, boolean beta) {
        if (!foundSearchData.searchBetaProperty().getValue()) {
            //Beta oder Daily: danach soll nicht gesucht werden
            return null;
        }

        if (!beta && !foundSearchData.searchDailyProperty().getValue()) {
            //Daily: danach soll nicht gesucht werden
            return null;
        }

        if (beta) {
            if (!foundSearchData.isFoundNewBeta() || foundSearchData.getFoundFileListBeta().isEmpty()) {
                //Beta: nichts gefunden oder Liste leer
                return null;
            }
        } else {
            if (!foundSearchData.isFoundNewDaily() || foundSearchData.getFoundFileListDaily().isEmpty()) {
                //Daily: nichts gefunden oder Liste leer
                return null;
            }
        }

        return makeTabBeta(foundSearchData.getStage(), foundSearchData, beta);
    }

    private static Tab makeTabBeta(Stage stage, FoundSearchData foundSearchData, boolean beta) {
        final Tab tabVersion = new Tab(beta ? "neue Beta" : "neues Daily");
        tabVersion.setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(6);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        textArea.setText(beta ? foundSearchData.getNewBetaText() : foundSearchData.getNewDailyText());

        final Label txtVersion = new Label(beta ? foundSearchData.getNewBetaDate() : foundSearchData.getNewDailyDate());
        final Hyperlink hyperlinkUrl = new PHyperlink(foundSearchData.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new PHyperlink(foundSearchData.getUrlDownload());
        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-Website:");
        final Label lblRel = new Label("Änderungen:");

        int row = 0;
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(stage,
                beta ? foundSearchData.getFoundFileListBeta() : foundSearchData.getFoundFileListDaily(),
                gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
        return tabVersion;
    }

    private static int getButton(Stage stage, FoundFileList foundFileList, GridPane gridPane, int row) {
        boolean done = false;
        for (FoundFile foundFile : foundFileList) {

            Button button = new Button();
            button.setMaxWidth(Double.MAX_VALUE);
            String text = PUrlTools.getFileName(foundFile.getFileUrl());
            button.setText(text);
            button.setTooltip(new Tooltip(foundFile.getFileUrl()));
            button.setOnAction(a -> {
                DownloadFactory.downloadFile(stage, foundFile.getFileUrl());
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
