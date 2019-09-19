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
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    // private final ProgressBar progressBar = new ProgressBar();

    private final VBox vBoxCont = new VBox();
    private final HBox vBoxIndicator = new HBox();
    HBox hBoxText = new HBox(10);
    private final Label lblText = new Label("");
    private final Button btnStop = new Button("Stop");

    public PMaskerPane() {

        getStyleClass().add("pMaskerPane");
        lblText.getStyleClass().add("textLabel");
        progressIndicator.getStyleClass().add("progressIndicator");
        vBoxIndicator.getStyleClass().add("vBoxIndicator");
        btnStop.getStyleClass().add("buttonStop");
//        final String CSS_FILE = "de/p2tools/p2Lib/guiTools/pMask/pMaskerPane.css";
//        getStylesheets().add(CSS_FILE);

        this.heightProperty().addListener((observable, oldValue, newValue) -> setSize());
        this.widthProperty().addListener((observable, oldValue, newValue) -> setSize());

        setVBoxCont();
        setSize();
        this.setCenter(vBoxCont);
        this.setVisible(false);
    }

    public Button getButton() {
        return btnStop;
    }

    public void setButtonText(String text) {
        Platform.runLater(() -> {
            btnStop.setText(text);
            setSize();
        });
    }


    public void setButtonVisible(boolean buttonVisible) {
        Platform.runLater(() -> {
            setBtnVisible(buttonVisible);
        });
    }

    public void setTextVisible(boolean textVisible) {
        Platform.runLater(() -> {
            setLblVisible(textVisible);
        });
    }

    public void switchOffMasker() {
        setMaskerVisible(false);
    }

    public void setMaskerVisible(boolean maskerVisible) {
        setMaskerVisible(maskerVisible, true, false);
    }

    public void setMaskerVisible(boolean maskerVisible, boolean buttonVisible) {
        setMaskerVisible(maskerVisible, true, buttonVisible);
    }

    public void setMaskerVisible(boolean maskerVisible, boolean text, boolean buttonVisible) {
        Platform.runLater(() -> {
            setPaneVisible(maskerVisible);
            setLblVisible(text);
            setBtnVisible(buttonVisible);
        });
    }

    public void setMaskerProgress(double progress, String text) {
        Platform.runLater(() -> {
            setProgress(progress, text);
        });
    }

    public void setMaskerProgressIndeterminate() {
        Platform.runLater(() -> {
            setProgress(-1, "");
        });
    }

    public void setMaskerText(String text) {
        Platform.runLater(() -> {
            lblText.setText(text);
        });
    }

    private void setBtnVisible(boolean visible) {
        btnStop.setVisible(visible);
        btnStop.setManaged(visible);
    }

    private void setLblVisible(boolean visible) {
        lblText.setVisible(visible);
        lblText.setManaged(visible);
    }

    private void setPaneVisible(boolean visible) {
        this.setVisible(visible);
        progressIndicator.setVisible(visible);
    }

    private void setVBoxCont() {
        vBoxCont.setSpacing(20);
        vBoxCont.setPadding(new Insets(20));
        vBoxCont.setAlignment(Pos.CENTER);

        progressIndicator.setMaxSize(120, 120);
        progressIndicator.setMinSize(120, 120);

        lblText.setMaxWidth(Double.MAX_VALUE);
        lblText.setAlignment(Pos.CENTER);
        lblText.setPadding(new Insets(3, 10, 3, 10));

        btnStop.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lblText.setMinHeight(newValue.doubleValue());
            }
        });
        setBtnVisible(false);

        hBoxText.setAlignment(Pos.CENTER);
        HBox.setHgrow(lblText, Priority.ALWAYS);
        hBoxText.getChildren().addAll(lblText, btnStop);

        vBoxCont.getChildren().addAll(progressIndicator, hBoxText);
    }

    private void setProgress(double progress, String text) {
        progressIndicator.setProgress(progress);
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
