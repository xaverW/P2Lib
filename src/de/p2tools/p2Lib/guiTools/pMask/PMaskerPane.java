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

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PMaskerPane extends BorderPane {
    //    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final ProgressIndicator progressBar = new ProgressIndicator();
//    private final ProgressBar progressBar = new ProgressBar();

    private final VBox vBoxCont = new VBox(20);
    private final Label lblText = new Label("");
    private final Button btnStop = new Button("Stop");

    public PMaskerPane() {

        getStyleClass().add("pMaskerPane");
        lblText.getStyleClass().add("textLabel");
        progressBar.getStyleClass().add("progressBar");
        btnStop.getStyleClass().add("buttonStop");
        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pMask/pMaskerPane.css";
        getStylesheets().add(CSS_FILE);

        vBoxCont.setPadding(new Insets(20));
        vBoxCont.setAlignment(Pos.CENTER);

        this.heightProperty().addListener((observable, oldValue, newValue) -> setSize());
        this.widthProperty().addListener((observable, oldValue, newValue) -> setSize());

//        progressIndicator.setMaxSize(200, 200);
//        progressIndicator.setMinSize(200, 200);
//        VBox.setVgrow(progressIndicator, Priority.ALWAYS);

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMinHeight(30);

        lblText.setMaxWidth(Double.MAX_VALUE);
        lblText.setAlignment(Pos.CENTER);
        lblText.setPadding(new Insets(3, 10, 3, 10));

        setBtnVisible(false);
        setSize();
        this.setCenter(vBoxCont);
        this.setVisible(false);
        setPBar();
    }

    public Button getButton() {
        return btnStop;
    }

    public void setButtonText(String text) {
        Platform.runLater(() -> {
            btnStop.setText(text);
        });
    }


    public void setButtonVisible(boolean buttonVisible) {
        Platform.runLater(() -> {
            setBtnVisible(buttonVisible);
        });
    }

    public void setMaskerVisible(boolean maskerVisible) {
        Platform.runLater(() -> {
            setMaskerVisible(maskerVisible, false);
        });
    }

    public void setMaskerVisible(boolean maskerVisible, boolean buttonVisible) {
        Platform.runLater(() -> {
            setBtnVisible(buttonVisible);
            setVisible(maskerVisible);
        });
    }

    public void setMaskerProgress(double progress, String text) {
        Platform.runLater(() -> {
            setProgress(progress, text);
        });
    }

    public void setMaskerText(String text) {
        Platform.runLater(() -> {
            lblText.setText(text);
        });
    }

    public void setMaskerProgressIndeterminate() {
        Platform.runLater(() -> {
            setProgress(-1, "");
        });
    }

    private void setBtnVisible(boolean visible) {
        btnStop.setVisible(visible);
        btnStop.setManaged(visible);
    }

    private void setPBar() {
        vBoxCont.getChildren().clear();

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(lblText, Priority.ALWAYS);
        hBox.getChildren().addAll(lblText, btnStop);

        vBoxCont.getChildren().addAll(progressBar, hBox);
    }

    private void setProgress(double progress, String text) {
        progressBar.setProgress(progress);
        lblText.setText(text);
    }

    private void setSize() {
        double w = this.getWidth(), h = this.getHeight();
        w = w / 3;
        h = h / 3;

        if (w == 0 || h == 0) {
            return;
        }

        vBoxCont.setMinWidth(w);
        vBoxCont.setMaxWidth(w);
        vBoxCont.setAlignment(Pos.CENTER);
    }

}
