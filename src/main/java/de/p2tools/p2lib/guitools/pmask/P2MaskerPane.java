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


package de.p2tools.p2lib.guitools.pmask;

import de.p2tools.p2lib.P2LibConst;
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

public class P2MaskerPane extends BorderPane {
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    private boolean txtBtnHorizontal = true;
    private final VBox vBoxCont = new VBox();
    private final Label lblText = new Label("");
    private final Button btnStop = new Button("Stop");

    public P2MaskerPane() {
        getStyleClass().add("p2MaskerPane");
        lblText.getStyleClass().add("p2MaskerTextLabel");
        btnStop.getStyleClass().add("p2MaskerButtonStop");
        progressIndicator.getStyleClass().add("p2MaskerProgressIndicator");

        setVBoxCont();
        this.setCenter(vBoxCont);
        this.setVisible(false);
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

    public void setTextVisible(boolean textVisible) {
        //dann wird nur der "Kreisel" angezeigt
        Platform.runLater(() -> {
            setLblVisible(textVisible);
        });
    }

    public void setMaskerText(String text) {
        Platform.runLater(() -> {
            lblText.setText(text);
        });
    }

    public void switchOffMasker() {
        setMaskerVisible(false, false, false);
    }

    public void setMaskerVisible() {
        Platform.runLater(() -> {
            setPaneVisible(true);
            setLblVisible(true);
            setBtnVisible(true);
        });
    }

    public void setMaskerVisible(boolean maskerVisible, boolean textVisible, boolean buttonVisible) {
        Platform.runLater(() -> {
            setPaneVisible(maskerVisible);
            setLblVisible(textVisible);
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

    public boolean isTxtBtnHorizontal() {
        return txtBtnHorizontal;
    }

    public void setTxtBtnHorizontal(boolean txtBtnHorizontal) {
        this.txtBtnHorizontal = txtBtnHorizontal;
        setVBoxCont();
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
        vBoxCont.getChildren().clear();
        vBoxCont.setMinWidth(500);
        vBoxCont.setMaxWidth(500);
        vBoxCont.setSpacing(20);
        vBoxCont.setPadding(new Insets(20));
        vBoxCont.setAlignment(Pos.CENTER);

        progressIndicator.setMaxSize(120, 120);
        progressIndicator.setMinSize(120, 120);

        lblText.setMaxWidth(Double.MAX_VALUE);
        lblText.setAlignment(Pos.CENTER);
        lblText.setPadding(new Insets(5, 10, 5, 10));
        lblText.setMinHeight(btnStop.getHeight());

        setBtnVisible(false);

        if (txtBtnHorizontal) {
            HBox hBox = new HBox(P2LibConst.PADDING_HBOX);
            hBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(lblText, Priority.ALWAYS);
            hBox.getChildren().addAll(lblText, btnStop);
            vBoxCont.getChildren().addAll(progressIndicator, hBox);

        } else {
            final VBox vBox = new VBox(P2LibConst.PADDING_VBOX);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(lblText, btnStop);
            vBoxCont.getChildren().addAll(progressIndicator, vBox);
        }
    }

    private void setProgress(double progress, String text) {
        progressIndicator.setProgress(progress);
        lblText.setText(text);
    }
}
