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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PDialogExtra extends PDialog {

    private VBox vBoxCompleteDialog = new VBox(); // ist der gesamte Dialog
    private HBox hBoxTitle = new HBox(10); // ist den Bereich über dem Inhalt mit dem Titel
    private VBox vboxCont = new VBox(); // ist der Inhalt des Dialogs
    private HBox hBoxOk = new HBox(10); // ist die Zeile mit den Button

    public PDialogExtra(StringProperty conf, String title, boolean modal) {
        super(conf, title, modal);
        initDialog();
    }

    public PDialogExtra(StringProperty conf, String title, boolean modal, boolean setOnlySize) {
        super(conf, title, modal, setOnlySize);
        initDialog();
    }

    public PDialogExtra(Stage ownerForCenteringDialog, String title) {
        super(ownerForCenteringDialog, null, title, true, true);
        initDialog();
    }

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf, String title) {
        super(ownerForCenteringDialog, conf, title, true, true);
        initDialog();
    }

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf, String title, boolean modal, boolean setOnlySize) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        initDialog();
    }

    public void init(boolean show) {
        super.init(vBoxCompleteDialog, show);
    }

    public VBox getVBoxCompleteDialog() {
        return vBoxCompleteDialog;
    }

    public HBox getHBoxTitle() {
        hBoxTitle.setVisible(true);
        hBoxTitle.setManaged(true);
        return hBoxTitle;
    }

    public VBox getVboxCont() {
        return vboxCont;
    }

    public HBox getHboxOk() {
        return hBoxOk;
    }

    private void initDialog() {
        vBoxCompleteDialog.setSpacing(10);
        vBoxCompleteDialog.setPadding(new Insets(10));

        hBoxTitle.getStyleClass().add("dialog-title");
        hBoxTitle.setVisible(false);
        hBoxTitle.setManaged(false);

        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        vboxCont.setPadding(new Insets(5));
        VBox.setVgrow(vboxCont, Priority.ALWAYS);

        hBoxOk.setAlignment(Pos.CENTER_RIGHT);

        scrollPane.setContent(vboxCont);
        vBoxStyledBorder.getChildren().addAll(hBoxTitle, scrollPane);
        vBoxCompleteDialog.getChildren().addAll(vBoxStyledBorder, hBoxOk);
        super.setPane(vBoxCompleteDialog);
    }

    public void addHlpOkButtons(Button btnHelp, Button... btnList) {
        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        getHboxOk().getChildren().addAll(btnHelp, hBox);

        for (Button b : btnList) {
            b.setMaxWidth(Double.MAX_VALUE);
        }
        TilePane tilePane = new TilePane(10, 10);
        tilePane.setAlignment(Pos.CENTER_RIGHT);
        tilePane.getChildren().addAll(btnList);
        getHboxOk().getChildren().add(tilePane);
    }

    public void addOkButtons(Button... btnList) {
        for (Button b : btnList) {
            b.setMaxWidth(Double.MAX_VALUE);
        }
        TilePane tilePane = new TilePane(10, 10);
        tilePane.setAlignment(Pos.CENTER_RIGHT);
        tilePane.getChildren().addAll(btnList);
        vBoxCompleteDialog.getChildren().remove(hBoxOk);
        vBoxCompleteDialog.getChildren().add(tilePane);
    }

}
