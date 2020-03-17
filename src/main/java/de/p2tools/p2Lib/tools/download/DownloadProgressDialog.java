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


package de.p2tools.p2Lib.tools.download;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PProgressBar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloadProgressDialog extends PDialogExtra {

    private VBox vBoxCont;
    private Button btnCancel = new Button("");
    private GridPane gridPane = new GridPane();
    private PProgressBar progressBar = new PProgressBar();
    private Label lblName = new Label();
    private boolean isCanceled = false;
    private String startText = "";

    DownloadProgressDialog(Stage stage, String fileName, String text) {
        super(stage, null, "Download", false, false, DECO.NONE);

        this.lblName.setText(fileName);
        this.startText = text;

        vBoxCont = getvBoxCont();
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

//        HBox hBox = new HBox(10);
//        hBox.setAlignment(Pos.CENTER);
//        hBox.getChildren().addAll(lblName, progressBar, btnCancel);
//        vBoxCont.getChildren().addAll(hBox);

        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());

        gridPane.add(lblName, 0, 0);
        gridPane.add(progressBar, 1, 0);
        gridPane.add(btnCancel, 2, 0);

        btnCancel.setGraphic(new ImageView(P2LibConst.IMAGE_STOP));
        btnCancel.setTooltip(new Tooltip("Den Download abbrechen."));
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

    public void close() {
        Platform.runLater(() -> super.close());
    }
}
