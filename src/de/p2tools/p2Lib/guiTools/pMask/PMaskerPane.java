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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PMaskerPane extends BorderPane {
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final ProgressBar progressBar = new ProgressBar();

    private final VBox vBox = new VBox(20);
    private final Label lblText = new Label("");

    public PMaskerPane() {

        // todo erst mal ein Anfang :)

        getStyleClass().add("pMaskerPane");
        lblText.getStyleClass().add("textLabel");
        progressBar.getStyleClass().add("progressBar");

        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pMask/pMaskerPane.css";
        getStylesheets().add(CSS_FILE);


        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(progressIndicator);

        progressIndicator.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
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

    public void setProgress(double progress, String text) {
        progressBar.setProgress(progress);
        lblText.setText(text);

        setSize();
        vBox.getChildren().clear();
        if (text.isEmpty()) {
            vBox.getChildren().addAll(progressBar);
        } else {
            vBox.getChildren().addAll(progressBar, lblText);
        }

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
            progressIndicator.setMaxSize(100, 100);
            return;
        }

        vBox.setPrefSize(w, h);
        vBox.setMinSize(w, h);
        vBox.setMaxSize(w, h);
    }
}
