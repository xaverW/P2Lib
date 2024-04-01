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


package de.p2tools.p2lib.mtdownload;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.ProgIconsP2Lib;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2ProgressBar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloadProgressDialog extends P2DialogExtra {

    private final VBox vBoxCont;
    private final Button btnCancel = new Button("");
    private final GridPane gridPane = new GridPane();
    private final P2ProgressBar progressBar = new P2ProgressBar();
    private final Label lblName = new Label();
    private boolean isCanceled = false;
    private String startText = "";

    DownloadProgressDialog(Stage stage, String fileName, String text) {
        super(stage, null, "Download", false, false, DECO.NO_BORDER);

        this.lblName.setText(fileName);
        this.startText = text;

        vBoxCont = getVBoxCont();
        init(true);
    }

    @Override
    public void make() {
        vBoxCont.setPadding(new Insets(5));
        vBoxCont.setSpacing(P2LibConst.PADDING_VBOX);
        vBoxCont.getChildren().addAll(gridPane);

        progressBar.setMinWidth(100);
        progressBar.setProgress(0, startText);

        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);

        gridPane.add(lblName, 0, 0, 2, 1);
        gridPane.add(progressBar, 0, 1);
        gridPane.add(btnCancel, 1, 1);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize());

        btnCancel.setGraphic(ProgIconsP2Lib.IMAGE_STOP.getImageView());
        btnCancel.setTooltip(new Tooltip("Den Download abbrechen"));
        btnCancel.setOnAction(event -> {
            isCanceled = true;
            quit();
        });
    }

    public void setProgress(double progress, String text) {
        Platform.runLater(() -> progressBar.setProgress(progress, text));
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    private void quit() {
        close();
    }

    @Override
    public void close() {
        Platform.runLater(() -> super.close());
    }
}
