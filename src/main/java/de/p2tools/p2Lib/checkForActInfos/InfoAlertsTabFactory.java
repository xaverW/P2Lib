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

import de.p2tools.p2Lib.mtDownload.DownloadFactory;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class InfoAlertsTabFactory {
    private static final int VERSION_PADDING_T = 5;
    private static final int VERSION_PADDING_B = 10;

    public static Tab addTabInfo(final FoundSearchData foundSearchData) {
        if (!foundSearchData.isFoundNewInfo() || foundSearchData.getFoundFileListInfo().isEmpty()) {
            return null;
        }

        return makeTabInfos(foundSearchData.getFoundFileListInfo());
    }

    private static Tab makeTabInfos(final FoundFileList foundFileList) {
        final Tab tabInfos = new Tab("Programminfos");
        tabInfos.setClosable(false);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        final VBox vBox = new VBox(10);
        for (int i = foundFileList.size() - 1; i >= 0; --i) {
            final FoundFile foundFile = foundFileList.get(i);
            final TextArea textArea = new TextArea(foundFile.getFileText());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            vBox.getChildren().add(textArea);
            VBox.setVgrow(textArea, Priority.ALWAYS);
        }

        scrollPane.setContent(vBox);
        tabInfos.setContent(scrollPane);
        return tabInfos;
    }

    public static Tab addTabVersion(final FoundSearchData foundSearchData) {
        //der wird immer angezeigt
        return makeTabVersion(foundSearchData.getStage(), foundSearchData);
    }

    private static Tab makeTabVersion(final Stage stage, final FoundSearchData foundSearchData) {
        final Tab tabVersion = new Tab("neue Version");
        if (!foundSearchData.isFoundNewVersion()) {
            tabVersion.setText("aktuelle Version");
        }
        tabVersion.setClosable(false);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final TextArea textArea = new TextArea();
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

        final Label lblActVersion = new Label("Aktuelle Version:");
        final Label lblVersion = new Label("Neueste Version:");
        final Label lblWeb = new Label("MTInfo Webseite:");
        final Label lblDown = new Label("Download-Website:");

        final Label lblRel = new Label(foundSearchData.isFoundNewVersion() ? "Änderungen:" : "");
        final Label txtActVersion = new Label(foundSearchData.getProgVersion() +
                "  vom: " + PDateFactory.getDate_yMd(foundSearchData.getProgBuildDate()));

        final Label txtVersion = new Label(foundSearchData.getNewVersionNo() +
                "  vom: " + PDateFactory.getDate_yMd(foundSearchData.getNewVersionDate()));

        final Hyperlink hyperlinkUrl = new PHyperlink(foundSearchData.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new PHyperlink(foundSearchData.getUrlDownload());

        int row = 0;
        lblActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));

        final HBox pane1 = new HBox();
        pane1.getStyleClass().add("update-grid-cell");
        pane1.getChildren().add(lblActVersion);
        pane1.setAlignment(Pos.CENTER_LEFT);


        final HBox pane2 = new HBox();
        pane2.getStyleClass().add("update-grid-cell");
        pane2.getChildren().add(txtActVersion);
        pane2.setAlignment(Pos.CENTER_LEFT);

        final HBox pane3 = new HBox();
        pane3.getStyleClass().add("update-grid-cell");
        pane3.getChildren().add(lblVersion);
        pane3.setAlignment(Pos.CENTER_LEFT);

        final HBox pane4 = new HBox();
        pane4.getStyleClass().add("update-grid-cell");
        pane4.getChildren().add(txtVersion);
        pane4.setAlignment(Pos.CENTER_LEFT);


        gridPane.add(pane1, 0, row);
        gridPane.add(pane2, 1, row);

        if (foundSearchData.isFoundNewVersion()) {
            gridPane.add(pane3, 0, ++row);
            gridPane.add(pane4, 1, row);
        }

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(foundSearchData, foundSearchData.getFoundFileListAct(), gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        final VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        final HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        if (foundSearchData.isFoundNewVersion()) {
            final CheckBox chkShowUpdateAgain = new CheckBox("dieses Update nochmal anzeigen");
            chkShowUpdateAgain.selectedProperty().bindBidirectional(foundSearchData.searchActAgainProperty());
            final HBox hB = new HBox();
            HBox.setHgrow(hB, Priority.ALWAYS);
            hBox.getChildren().addAll(chkShowUpdateAgain, hB);
        }
        final CheckBox chkSearchUpdate = new CheckBox("beim Programmstart nach Updates suchen");
        chkSearchUpdate.selectedProperty().bindBidirectional(foundSearchData.searchActProperty());
        hBox.getChildren().add(chkSearchUpdate);

        vBox.getChildren().addAll(hBox);
        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
        return tabVersion;
    }

    public static Tab addTabBeta(final FoundSearchData foundSearchData, final boolean beta) {
        if (!foundSearchData.searchBetaProperty().getValue()) {
            //Beta oder Daily: danach soll nicht gesucht werden
            return null;
        }

        if (!beta && !foundSearchData.searchDailyProperty().getValue()) {
            //Daily: danach soll nicht gesucht werden
            return null;
        }

        if (beta) {
            //beta
            if (!foundSearchData.isFoundNewBeta() || foundSearchData.getFoundFileListBeta().isEmpty()) {
                //beta: nichts gefunden oder Liste leer
                return null;
            }
        } else {
            //daily
            if (!foundSearchData.isFoundNewDaily() || foundSearchData.getFoundFileListDaily().isEmpty()) {
                //daily: nichts gefunden oder Liste leer
                return null;
            }
        }

        return makeTabBeta(foundSearchData, beta);
    }

    private static Tab makeTabBeta(final FoundSearchData foundSearchData, final boolean beta) {
        final Tab tabVersion = new Tab(beta ? "neue Beta" : "neues Daily");
        tabVersion.setClosable(false);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final TextArea textArea = new TextArea();
        textArea.setPrefRowCount(6);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        textArea.setText(beta ? foundSearchData.getNewBetaText() : foundSearchData.getNewDailyText());


        final Label txtActVersion = new Label(foundSearchData.getProgVersion() +
                "   [Build: " + foundSearchData.getProgBuildNo() +
                "  vom: " + PDateFactory.getDate_yMd(foundSearchData.getProgBuildDate()) + "]");

        final Label txtVersion = new Label();
        if (beta) {
            txtVersion.setText(foundSearchData.getNewBetaVersion() +
                    "   [Build: " + foundSearchData.getNewBetaBuildNo()
                    + "  vom: " + PDateFactory.getDate_yMd(foundSearchData.getNewBetaDate()) + "]");
        } else {
            txtVersion.setText(foundSearchData.getNewDailyVersion() +
                    "   [Build: " + foundSearchData.getNewDailyBuild()
                    + "  vom: " + PDateFactory.getDate_yMd(foundSearchData.getNewDailyDate()) + "]");
        }

        final Hyperlink hyperlinkUrl = new PHyperlink(foundSearchData.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new PHyperlink(foundSearchData.getUrlDownload());
        final Label lblActVersion = new Label("Aktuelle Version:");
        final Label lblVersion = new Label("Neueste Version:");
        final Label lblWeb = new Label("MTInfo Webseite:");
        final Label lblDown = new Label("Download-Website:");
        final Label lblRel = new Label("Änderungen:");

        final HBox pane1 = new HBox();
        pane1.getStyleClass().add("update-grid-cell");
        pane1.getChildren().add(lblActVersion);
        pane1.setAlignment(Pos.CENTER_LEFT);

        final HBox pane2 = new HBox();
        pane2.getStyleClass().add("update-grid-cell");
        pane2.getChildren().add(txtActVersion);
        pane2.setAlignment(Pos.CENTER_LEFT);

        final HBox pane3 = new HBox();
        pane3.getStyleClass().add("update-grid-cell");
        pane3.getChildren().add(lblVersion);
        pane3.setAlignment(Pos.CENTER_LEFT);

        final HBox pane4 = new HBox();
        pane4.getStyleClass().add("update-grid-cell");
        pane4.getChildren().add(txtVersion);
        pane4.setAlignment(Pos.CENTER_LEFT);

        int row = 0;
        lblActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));

        gridPane.add(pane1, 0, row);
        gridPane.add(pane2, 1, row);

        gridPane.add(pane3, 0, ++row);
        gridPane.add(pane4, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(foundSearchData,
                beta ? foundSearchData.getFoundFileListBeta() : foundSearchData.getFoundFileListDaily(),
                gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        final RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        gridPane.getRowConstraints().add(rowConstraints);


        final VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
        return tabVersion;
    }

    private static int getButton(final FoundSearchData foundSearchData, final FoundFileList foundFileList, final GridPane gridPane, int row) {
        boolean done = false;
        for (final FoundFile foundFile : foundFileList) {

            final Button button = new Button();
            button.setMaxWidth(Double.MAX_VALUE);
            final String text = PUrlTools.getFileName(foundFile.getFileUrl());
            button.setText(text);
            button.setTooltip(new Tooltip(foundFile.getFileUrl()));
            button.setOnAction(a -> {
                DownloadFactory.downloadFile(foundSearchData.getStage(), foundFile.getFileUrl(),
                        foundSearchData.downloadDirProperty(), "");
            });
            gridPane.add(button, 1, ++row);
            if (!done) {
                done = true;
                gridPane.add(new Label("Update laden:"), 0, row);
            }
        }
        return row;
    }
}
