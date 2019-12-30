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

package de.p2tools.p2Lib.dialogs.dialog;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PDialogExtra extends PDialog {

    private VBox vBoxCompleteDialog = new VBox(); // ist der gesamte Dialog
    private final ScrollPane scrollPane = new ScrollPane();
    private HBox hBoxOverAll = new HBox(10); // ist der Bereich über dem Inhalt und dem Scrollpanel
    private HBox hBoxTitle = new HBox(10); // ist der Bereich über dem Inhalt mit dem Titel
    private HBox hBoxOverButtons = new HBox(10); // ist der Bereich über den Buttons aber außerhalb des Rahmens
    private VBox vBoxCont = new VBox(10); // ist der Inhalt des Dialogs
    private HBox hBoxLeft = new HBox(10); // ist vor der ButtonBar
    private HBox hBoxRight = new HBox(10); // ist nach der ButtonBar
    private ButtonBar buttonBar = new ButtonBar();
    private DECO deco = DECO.BORDER;

    public static enum DECO {
        NONE, BORDER
    }

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

    public PDialogExtra(StringProperty conf, String title, boolean modal, boolean setOnlySize, DECO deco) {
        super(conf, title, modal, setOnlySize);
        this.deco = deco;
        initDialog();
    }

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf, String title, boolean modal, boolean setOnlySize,
                        DECO deco) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        this.deco = deco;
        initDialog();
    }

    public void init(boolean show) {
        super.init(vBoxCompleteDialog, show);
    }

    public VBox getVBoxCompleteDialog() {
        return vBoxCompleteDialog;
    }

    public HBox gethBoxOverAll() {
        hBoxOverAll.setVisible(true);
        hBoxOverAll.setManaged(true);
        return hBoxOverAll;
    }

    public HBox getHBoxTitle() {
        hBoxTitle.setVisible(true);
        hBoxTitle.setManaged(true);
        return hBoxTitle;
    }

    public VBox getvBoxCont() {
        return vBoxCont;
    }

    public HBox getHBoxOverButtons() {
        return hBoxOverButtons;
    }

    public HBox getHboxOk() {
        return hBoxLeft;
    }

    public HBox getHboxLeft() {
        return hBoxLeft;
    }

    public HBox getHboxRight() {
        return hBoxRight;
    }

    public ButtonBar getButtonBar() {
        return buttonBar;
    }

    private void initDialog() {
        initBefore();

        switch (deco) {
            case BORDER:
                initBorder();
                break;
            case NONE:
            default:
                initNone();
        }
        addButtonBox(vBoxCompleteDialog);
        super.setPane(vBoxCompleteDialog);
    }

    private void initBefore() {
        vBoxCompleteDialog.setSpacing(10);
        vBoxCompleteDialog.setPadding(new Insets(10));

        hBoxOverAll.setVisible(false);
        hBoxOverAll.setManaged(false);

        hBoxTitle.getStyleClass().add("dialog-title");
        hBoxTitle.setVisible(false);
        hBoxTitle.setManaged(false);

        VBox.setVgrow(vBoxCont, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setContent(vBoxCont);
    }

    private void initNone() {
//        getHBoxTitle().getChildren().add(new Label("Test"));
        vBoxCont.setPadding(new Insets(15));
        vBoxCompleteDialog.getChildren().addAll(hBoxOverAll, hBoxTitle, scrollPane);
    }

    private void initBorder() {
//        getHBoxTitle().getChildren().add(new Label("Test"));
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().add(hBoxOverAll);
        vBoxStyledBorder.getChildren().addAll(scrollPane);
        vBoxCompleteDialog.getChildren().addAll(hBoxTitle, vBoxStyledBorder);
    }

    private void addButtonBox(VBox vBox) {
        HBox hButton = new HBox();
        hButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxLeft.setAlignment(Pos.CENTER_RIGHT);
        hBoxRight.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBar, Priority.ALWAYS);
        hButton.getChildren().addAll(hBoxLeft, buttonBar, hBoxRight);
        vBox.getChildren().addAll(hBoxOverButtons, hButton);
    }

    public void addOkButton(Button btnOk) {
        if (btnOk != null) {
            ButtonBar.setButtonData(btnOk, ButtonBar.ButtonData.OK_DONE);
            buttonBar.getButtons().addAll(btnOk);
        }
    }

    public void addCancelButton(Button btnCancel) {
        if (btnCancel != null) {
            ButtonBar.setButtonData(btnCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(btnCancel);
        }
    }

    public void addButtons(Button btnOk, Button btnCancel) {
        if (btnOk != null) {
            ButtonBar.setButtonData(btnOk, ButtonBar.ButtonData.OK_DONE);
            buttonBar.getButtons().addAll(btnOk);
        }
        if (btnCancel != null) {
            ButtonBar.setButtonData(btnCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(btnCancel);
        }
    }

    public void addHlpButton(Node btn) {
        if (btn != null) {
            ButtonBar.setButtonData(btn, ButtonBar.ButtonData.HELP);
            buttonBar.getButtons().add(btn);
        }
    }

    public void addAnyButton(Node btn) {
        if (btn != null) {
            buttonBar.getButtons().addAll(btn);
        }
    }

}
