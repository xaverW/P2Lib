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

import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.mtdownload.DownloadFactory;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.net.PUrlTools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class InfoAlertsTabFactory {
    private static final int VERSION_PADDING_T = 5;
    private static final int VERSION_PADDING_B = 10;

    public static Tab addTabInfo(final FoundSearchDataDTO foundSearchDataDTO) {
        if (!foundSearchDataDTO.isFoundNewInfo() || foundSearchDataDTO.getFoundFileListInfo().isEmpty()) {
            return null;
        }

        return makeTabInfos(foundSearchDataDTO.getFoundFileListInfo());
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

    public static Tab addTabVersion(final FoundSearchDataDTO foundSearchDataDTO) {
        //der wird immer angezeigt
        return makeTabVersion(foundSearchDataDTO.getStage(), foundSearchDataDTO);
    }

    private static Tab makeTabVersion(final Stage stage, final FoundSearchDataDTO foundSearchDataDTO) {
        final Tab tabVersion = new Tab("neue Version");
        if (!foundSearchDataDTO.isFoundNewVersion()) {
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

        if (foundSearchDataDTO.isFoundNewVersion()) {
            //gibt eine aktuellere Version
            textArea.setText(foundSearchDataDTO.getNewVersionText());
        } else {
            //keine neue Version
            textArea.setText("Sie benutzen die aktuelle Version.");
        }

        final Label lblActVersion = new Label("Aktuelle Version:");
        final Label lblVersion = new Label("Neue Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download:");

        final Label lblRel = new Label(foundSearchDataDTO.isFoundNewVersion() ? "Änderungen:" : "");
        final Label txtActVersion = new Label(foundSearchDataDTO.getProgVersion() +
                "  vom: " + P2LDateFactory.getDate_yMd(foundSearchDataDTO.getProgBuildDate()));

        final Label txtVersion = new Label(foundSearchDataDTO.getNewVersionNo() +
                "  vom: " + P2LDateFactory.getDate_yMd(foundSearchDataDTO.getNewVersionDate()));

        final Hyperlink hyperlinkUrl = new P2Hyperlink(foundSearchDataDTO.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new P2Hyperlink(foundSearchDataDTO.getUrlDownload());

        int row = 0;
        lblActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtActVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        lblVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));
        txtVersion.setPadding(new Insets(VERSION_PADDING_T, 0, VERSION_PADDING_B, 0));

        final HBox pane1 = new HBox();
//        pane1.getStyleClass().add("update-grid-cell");
        pane1.getChildren().add(lblActVersion);
        pane1.setAlignment(Pos.CENTER_LEFT);


        final HBox pane2 = new HBox();
//        pane2.getStyleClass().add("update-grid-cell");
        pane2.getChildren().add(txtActVersion);
        pane2.setAlignment(Pos.CENTER_LEFT);

        final HBox pane3 = new HBox();
//        pane3.getStyleClass().add("update-grid-cell");
        pane3.getChildren().add(lblVersion);
        pane3.setAlignment(Pos.CENTER_LEFT);

        final HBox pane4 = new HBox();
//        pane4.getStyleClass().add("update-grid-cell");
        pane4.getChildren().add(txtVersion);
        pane4.setAlignment(Pos.CENTER_LEFT);


        gridPane.add(pane1, 0, row);
        gridPane.add(pane2, 1, row);

        if (foundSearchDataDTO.isFoundNewVersion()) {
            gridPane.add(pane3, 0, ++row);
            gridPane.add(pane4, 1, row);
        }

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        row = getButton(foundSearchDataDTO, foundSearchDataDTO.getFoundFileListAct(), gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        final VBox vBox = new VBox(10);
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        final HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        if (foundSearchDataDTO.isFoundNewVersion()) {
            final CheckBox chkShowUpdateAgain = new CheckBox("Dieses Update nochmal anzeigen");
            chkShowUpdateAgain.selectedProperty().bindBidirectional(foundSearchDataDTO.searchActAgainProperty());
            final HBox hB = new HBox();
            HBox.setHgrow(hB, Priority.ALWAYS);
            hBox.getChildren().addAll(chkShowUpdateAgain, hB);
        }
        final CheckBox chkSearchUpdate = new CheckBox("beim Programmstart nach Updates suchen");
        chkSearchUpdate.selectedProperty().bindBidirectional(foundSearchDataDTO.searchUpdateProperty());
        hBox.getChildren().add(chkSearchUpdate);

        vBox.getChildren().addAll(hBox);
        scrollPane.setContent(vBox);
        tabVersion.setContent(scrollPane);
        return tabVersion;
    }

    public static Tab addTabBeta(final FoundSearchDataDTO foundSearchDataDTO, final boolean isBetaTab) {
        if (isBetaTab && foundSearchDataDTO.getFoundFileListBeta().isEmpty()) {
            // dann gibts eh nix
            return null;
        }
        if (!isBetaTab && foundSearchDataDTO.getFoundFileListDaily().isEmpty()) {
            // dann gibts eh nix
            return null;
        }

        if (isBetaTab) {
            if (!foundSearchDataDTO.isShowAllDownloads() &&
                    !foundSearchDataDTO.isSearchBeta()) {
                // dann kein beta anzeigen
                return null;
            }

        } else {
            if (!foundSearchDataDTO.isShowAllDownloads() &&
                    !foundSearchDataDTO.isSearchBeta() &&
                    !foundSearchDataDTO.isSearchDaily()) {
                // dann kein daily anzeigen
                return null;
            }
        }

        return makeTabBeta(foundSearchDataDTO, isBetaTab);
    }

    private static Tab makeTabBeta(final FoundSearchDataDTO foundSearchDataDTO, final boolean beta) {
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

        textArea.setText(beta ? foundSearchDataDTO.getNewBetaText() : foundSearchDataDTO.getNewDailyText());


        final Label txtActVersion = new Label(foundSearchDataDTO.getProgVersion() +
                "   [Build: " + foundSearchDataDTO.getProgBuildNo() +
                "  vom: " + P2LDateFactory.getDate_yMd(foundSearchDataDTO.getProgBuildDate()) + "]");

        final Label txtVersion = new Label();
        if (beta) {
            txtVersion.setText(foundSearchDataDTO.getNewBetaVersion() +
                    "   [Build: " + foundSearchDataDTO.getNewBetaBuildNo()
                    + "  vom: " + P2LDateFactory.getDate_yMd(foundSearchDataDTO.getNewBetaDate()) + "]");
        } else {
            txtVersion.setText(foundSearchDataDTO.getNewDailyVersion() +
                    "   [Build: " + foundSearchDataDTO.getNewDailyBuild()
                    + "  vom: " + P2LDateFactory.getDate_yMd(foundSearchDataDTO.getNewDailyDate()) + "]");
        }

        final Hyperlink hyperlinkUrl = new P2Hyperlink(foundSearchDataDTO.getUrlWebsite());
        final Hyperlink hyperlinkDownUrl = new P2Hyperlink(foundSearchDataDTO.getUrlDownload());
        final Label lblActVersion = new Label("Aktuelle Version:");
        final Label lblVersion = new Label("Neue Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download:");
        final Label lblRel = new Label("Änderungen:");

        final HBox pane1 = new HBox();
//        pane1.getStyleClass().add("update-grid-cell");
        pane1.getChildren().add(lblActVersion);
        pane1.setAlignment(Pos.CENTER_LEFT);

        final HBox pane2 = new HBox();
//        pane2.getStyleClass().add("update-grid-cell");
        pane2.getChildren().add(txtActVersion);
        pane2.setAlignment(Pos.CENTER_LEFT);

        final HBox pane3 = new HBox();
//        pane3.getStyleClass().add("update-grid-cell");
        pane3.getChildren().add(lblVersion);
        pane3.setAlignment(Pos.CENTER_LEFT);

        final HBox pane4 = new HBox();
//        pane4.getStyleClass().add("update-grid-cell");
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

        row = getButton(foundSearchDataDTO,
                beta ? foundSearchDataDTO.getFoundFileListBeta() : foundSearchDataDTO.getFoundFileListDaily(),
                gridPane, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

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

    private static int getButton(final FoundSearchDataDTO foundSearchDataDTO, final FoundFileList foundFileList, final GridPane gridPane, int row) {
        boolean done = false;
        for (final FoundFile foundFile : foundFileList) {

            final Button button = new Button();
            button.setMaxWidth(Double.MAX_VALUE);
            final String text = PUrlTools.getFileName(foundFile.getFileUrl());
            button.setText(text);
            button.setTooltip(new Tooltip(foundFile.getFileUrl()));
            button.setOnAction(a -> {
                DownloadFactory.downloadFile(foundSearchDataDTO.getStage(), foundFile.getFileUrl(),
                        foundSearchDataDTO.downloadDirProperty(), "");
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
