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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class InfoBetaUpdateAlert {

    private final ProgInfo progInfo;

    private final VBox vBox = new VBox(10);
    private final VBox vBoxInfo = new VBox(10);
    private final CheckBox chkSearchUpdateInfo = new CheckBox("Nach beta-Updates suchen.");
    private final boolean newVersion;
    private final Stage stage;
    BooleanProperty searchForUpdate = null;


    public InfoBetaUpdateAlert(ProgInfo progInfo, boolean newVersion, BooleanProperty searchForUpdate) {
        this.stage = P2LibConst.primaryStage;
        this.progInfo = progInfo;
        this.newVersion = newVersion;
        this.searchForUpdate = searchForUpdate;
    }

    public InfoBetaUpdateAlert(Stage stage, ProgInfo progInfo, boolean newVersion, BooleanProperty searchForUpdate) {
        this.stage = stage;
        this.progInfo = progInfo;
        this.newVersion = newVersion;
        this.searchForUpdate = searchForUpdate;
    }

    public boolean showInfoAlert(String title, String header) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setResizable(true);

        if (searchForUpdate != null) {
            chkSearchUpdateInfo.selectedProperty().bindBidirectional(searchForUpdate);
            chkSearchUpdateInfo.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER_RIGHT);
            VBox.setVgrow(vBoxInfo, Priority.ALWAYS);
            vBox.getChildren().addAll(vBoxInfo, chkSearchUpdateInfo);
            alert.getDialogPane().setContent(vBox);
        } else {
            alert.getDialogPane().setContent(vBoxInfo);
        }

        makeTabVersion();

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private void makeTabVersion() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Label lblVersion = new Label(progInfo.getProgVersion() + "");
        Label lblBuild = new Label(progInfo.getProgBuildNo() + "  vom  " + progInfo.getProgBuildDate());
        Hyperlink hyperlinkUrl = new PHyperlink(progInfo.getProgUrl());
        hyperlinkUrl.setPadding(new Insets(0));
        Hyperlink hyperlinkDownUrl = new PHyperlink(progInfo.getProgDownloadUrl());
        hyperlinkDownUrl.setPadding(new Insets(0));

        TextArea textArea = new TextArea();
        if (newVersion) {
            textArea.setText(progInfo.getProgReleaseNotes());
        } else {
            textArea.setText(P2LibConst.LINE_SEPARATOR + "Sie benutzen die aktuellste Version von " + progInfo.getProgName() + ".");
        }

        textArea.setMinHeight(150);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        int row = 0;
        gridPane.add(new Label("Version:"), 0, row);
        gridPane.add(lblVersion, 1, row);
        gridPane.add(new Label("Build:"), 0, ++row);
        gridPane.add(lblBuild, 1, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label("Webseite:"), 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);
        gridPane.add(new Label("Download-URL:"), 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(newVersion ? "Änderungen:" : ""), 0, ++row);
        if (newVersion) {
            gridPane.add(textArea, 1, row);
        } else {
            gridPane.add(textArea, 0, row, 2, 1);
        }

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        scrollPane.setContent(gridPane);
        vBoxInfo.getChildren().add(scrollPane);
    }

}
