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

import de.p2tools.p2Lib.guiTools.PHyperlink;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.Optional;

public class InfoAlert {

    private final ProgInfo progInfo;
    private final ArrayList<Infos> newInfosList;

    private final TabPane tabPane = new TabPane();
    private final Tab tabVersion = new Tab("Neue Version");
    private final Tab tabInfos = new Tab("Programminfos");
    private final boolean newVersion;

    public InfoAlert(ProgInfo progInfo, ArrayList<Infos> newInfosList, boolean newVersion) {
        this.progInfo = progInfo;
        this.newInfosList = newInfosList;
        this.newVersion = newVersion;
    }

    public boolean showInfoAlert(String title, String header) {

        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.getDialogPane().setContent(tabPane);
        alert.setResizable(true);

        tabVersion.setClosable(false);
        tabInfos.setClosable(false);

        makeTabVersion();
        tabPane.getTabs().add(tabVersion);

        if (!progInfo.getInfos().isEmpty()) {
            makeTabInfos();
            tabPane.getTabs().add(tabInfos);
        }

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private void makeTabVersion() {
        if (!newVersion) {
            TextArea textArea = new TextArea("\nSie benutzen die neueste Version");
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setPadding(new Insets(10));
            tabVersion.setContent(textArea);
            return;
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label txtVersion = new Label(progInfo.getProgVersion() + "");
        GridPane.setHgrow(txtVersion, Priority.ALWAYS);

        Hyperlink hyperlinkUrl = new PHyperlink(progInfo.getProgUrl());
        Hyperlink hyperlinkDownUrl = new PHyperlink(progInfo.getProgDownloadUrl());

        TextArea textArea = new TextArea(progInfo.getProgReleaseNotes());
        textArea.setWrapText(true);
        textArea.setEditable(false);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setVgrow(textArea, Priority.ALWAYS);

        final Label lblVersion = new Label("Version:");
        final Label lblWeb = new Label("Webseite:");
        final Label lblDown = new Label("Download-URL:");
        final Label lblRel = new Label("Änderungen:");

        int row = 0;
        gridPane.add(lblVersion, 0, row);
        gridPane.add(txtVersion, 1, row);

        gridPane.add(lblWeb, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDown, 0, ++row);
        gridPane.add(hyperlinkDownUrl, 1, row);

        gridPane.add(lblRel, 0, ++row);
        gridPane.add(textArea, 1, row);

        ColumnConstraints c0 = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(c0);
        c0.setMinWidth(GridPane.USE_PREF_SIZE);

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
            textArea.setWrapText(true);
            textArea.setMinHeight(150);
            textArea.setMaxHeight(150);
            textArea.setEditable(false);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            if (!newInfosList.contains(infos)) {
                textArea.setStyle("-fx-text-fill: gray;");
            }

            gridPane.add(new Label(infos.getInfoNr() + ""), 0, row);
            gridPane.add(textArea, 1, row++);
        }

        ColumnConstraints c0 = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(c0);
        c0.setMinWidth(GridPane.USE_PREF_SIZE);

        scrollPane.setContent(gridPane);
        tabInfos.setContent(scrollPane);
    }
}
