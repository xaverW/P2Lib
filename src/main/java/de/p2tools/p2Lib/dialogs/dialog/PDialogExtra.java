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

import de.p2tools.p2Lib.P2LibConst;
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

    private final VBox vBoxCompleteDialog = new VBox(); // ist der gesamte Dialog
    private final HBox hBoxTitle = new HBox(10); // ist der Bereich über dem Inhalt mit dem Titel
    private final HBox hBoxOverAll = new HBox(10); // ist der Bereich über dem Inhalt und dem Scrollpanel
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox(10); // ist der Inhalt des Dialogs und in der Scrollpane
    private final HBox hBoxOverButtons = new HBox(10); // ist der Bereich über den Buttons aber außerhalb des Rahmens
    private final HBox hBoxLeft = new HBox(10); // ist vor der ButtonBar
    private final HBox hBoxRight = new HBox(10); // ist nach der ButtonBar
    private final ButtonBar buttonBar = new ButtonBar();
    private DECO deco = DECO.BORDER;

    public enum DECO {
        NONE, BORDER, SMALL
    }

    public PDialogExtra(StringProperty conf, String title) {
        // ist nur ein einfacher Dialog, zentral über dem Hauptfenster
        super(P2LibConst.primaryStage, conf, title, true, true);
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

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setOnlySize) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        initDialog();
    }

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setOnlySize, DECO deco) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        this.deco = deco;
        initDialog();
    }

    public void init(boolean show) {
        super.init(show);
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

    public HBox getHboxLeft() {
        return hBoxLeft;
    }

    public HBox getHboxRight() {
        return hBoxRight;
    }

    public ButtonBar getButtonBar() {
        return buttonBar;
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

    public void addOkCancelButtons(Button btnOk, Button btnCancel) {
        if (btnCancel != null) {
            ButtonBar.setButtonData(btnCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(btnCancel);
        }
        if (btnOk != null) {
            ButtonBar.setButtonData(btnOk, ButtonBar.ButtonData.OK_DONE);
            buttonBar.getButtons().addAll(btnOk);
        }
    }

    public void addOkCancelApplyButtons(Button btnOk, Button btnCancel, Button btnApplay) {
        if (btnOk != null) {
            ButtonBar.setButtonData(btnOk, ButtonBar.ButtonData.OK_DONE);
            buttonBar.getButtons().addAll(btnOk);
        }
        if (btnCancel != null) {
            ButtonBar.setButtonData(btnCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(btnCancel);
        }
        if (btnApplay != null) {
            ButtonBar.setButtonData(btnApplay, ButtonBar.ButtonData.APPLY);
            buttonBar.getButtons().addAll(btnApplay);
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

    private void initDialog() {
        initBefore();

        switch (deco) {
            case BORDER:
                initBorder();
                break;
            case SMALL:
                initBorderSmall();
                break;
            case NONE:
            default:
                initNone();
        }
        addButtonBox(vBoxCompleteDialog);
    }

    private void initBefore() {
        super.setPane(vBoxCompleteDialog);
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
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().add(hBoxOverAll);

        vBoxStyledBorder.getChildren().addAll(scrollPane);
        vBoxCompleteDialog.getChildren().addAll(hBoxTitle, vBoxStyledBorder);
    }

    private void initBorderSmall() {
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border-small");
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
}
