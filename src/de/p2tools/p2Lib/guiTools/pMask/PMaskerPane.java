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


package de.p2tools.p2Lib.guiTools.pMask;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PMaskerPane extends BorderPane {
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final ProgressBar progressBar = new ProgressBar();

    private final VBox vBox = new VBox(20);
    private final Label lblText = new Label("");
    private final Button btnStop = new Button("Stop");
    private boolean useButton = false;

    public PMaskerPane() {

        // todo erst mal ein Anfang :)

        getStyleClass().add("pMaskerPane");
        lblText.getStyleClass().add("textLabel");
        progressBar.getStyleClass().add("progressBar");
        btnStop.getStyleClass().add("buttonStop");

        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pMask/pMaskerPane.css";
        getStylesheets().add(CSS_FILE);


        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(progressIndicator);

        progressIndicator.setMaxSize(100, 100);

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMinHeight(30);

        lblText.setMaxWidth(Double.MAX_VALUE);
        lblText.setAlignment(Pos.CENTER);
        lblText.setPadding(new Insets(3, 10, 3, 10));

        VBox.setVgrow(progressIndicator, Priority.ALWAYS);
        VBox.setVgrow(progressBar, Priority.ALWAYS);
        VBox.setVgrow(lblText, Priority.ALWAYS);

        setSize();
        this.setCenter(vBox);
    }

    public Button initButton(String text) {
        useButton = true;
        btnStop.setText(text);
        return btnStop;
    }

    public void setProgress(double progress, String text) {
        progressBar.setProgress(progress);
        lblText.setText(text);

        vBox.getChildren().clear();
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        if (text.isEmpty() && useButton) {
            HBox hBox = new HBox(10);
            HBox.setHgrow(progressBar, Priority.ALWAYS);
            hBox.getChildren().addAll(progressBar, btnStop);
            vBox.getChildren().addAll(hBox);

        } else if (text.isEmpty() && !useButton) {
            vBox.getChildren().addAll(progressBar);

        } else if (!text.isEmpty() && useButton) {
            HBox hBox = new HBox(10);
            HBox.setHgrow(progressBar, Priority.ALWAYS);
            hBox.getChildren().addAll(progressBar, btnStop);
            vBox.getChildren().addAll(hBox, lblText);

        } else if (!text.isEmpty() && !useButton) {
            vBox.getChildren().addAll(progressBar, lblText);
        }

        setSize();
    }


    public void resetProgress() {
        vBox.getChildren().clear();
        vBox.getChildren().add(progressIndicator);
    }

    private void setSize() {
        double w = this.getWidth(), h = this.getHeight();
        w = w / 3;
        h = h / 3;

        if (w == 0 || h == 0) {
            return;
        }

        vBox.setMinWidth(w);
        vBox.setMaxWidth(w);
        vBox.setAlignment(Pos.CENTER);
    }
}
