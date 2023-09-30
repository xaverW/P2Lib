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

import de.p2tools.p2lib.ProgIconsP2Lib;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
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

public class DownloadProgressDialog extends PDialogExtra {

    private VBox vBoxCont;
    private Button btnCancel = new Button("");
    private GridPane gridPane = new GridPane();
    private P2ProgressBar progressBar = new P2ProgressBar();
    private Label lblName = new Label();
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
        getStage().setMinWidth(500);
        vBoxCont.setPadding(new Insets(5));
        vBoxCont.setSpacing(10);
        vBoxCont.getChildren().addAll(gridPane);

        progressBar.setMinWidth(100);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setProgress(0, startText);

        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());

        gridPane.add(lblName, 0, 0);
        gridPane.add(progressBar, 1, 0);
        gridPane.add(btnCancel, 2, 0);

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
