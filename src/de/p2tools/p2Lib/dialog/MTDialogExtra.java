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

package de.p2tools.p2Lib.dialog;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MTDialogExtra extends MTDialog {

    private VBox vBoxDialog = new VBox();
    private VBox vboxCont = new VBox();
    private HBox hBoxOk = new HBox(10);


    public MTDialogExtra(String fxml, StringProperty conf, String title, boolean modal) {
        super(fxml, conf, title, modal);
        initDialog();
    }

    private void initDialog() {
        vBoxDialog.setSpacing(10);
        vBoxDialog.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        vboxCont.setPadding(new Insets(5));
        VBox.setVgrow(vboxCont, Priority.ALWAYS);
        scrollPane.setContent(vboxCont);

        hBoxOk.setAlignment(Pos.CENTER_RIGHT);

        VBox vBox = new VBox();
        vBox.getStyleClass().add("dialog-border");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        vBox.getChildren().add(scrollPane);

        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBoxDialog.getChildren().addAll(vBox, hBoxOk);
    }

    public VBox getvBoxDialog() {
        return vBoxDialog;
    }

    public void setvBoxDialog(VBox vBoxDialog) {
        this.vBoxDialog = vBoxDialog;
    }

    public VBox getVboxCont() {
        return vboxCont;
    }

    public void setVboxCont(VBox vboxCont) {
        this.vboxCont = vboxCont;
    }

    public HBox getHboxOk() {
        return hBoxOk;
    }

    public void sethBoxOk(HBox hBoxOk) {
        this.hBoxOk = hBoxOk;
    }
}
