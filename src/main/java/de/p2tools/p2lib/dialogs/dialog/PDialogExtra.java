/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.dialogs.dialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.pmask.PMaskerPane;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PDialogExtra extends PDialog {

    private final VBox vBoxCompleteDialog = new VBox(10); // ist der gesamte Dialog
    private final HBox hBoxOverAll = new HBox(10); // ist der Bereich über dem Inhalt, Titel und dem Scrollpanel
    private final HBox hBoxTitle = new HBox(10); // ist der Bereich über dem Inhalt mit dem Titel

    private final VBox vBoxCont = new VBox(10); // ist der Inhalt des Dialogs und in der Scrollpane

    private final HBox hBoxOverButtons = new HBox(10); // ist der Bereich über den Buttons aber außerhalb des Rahmens
    private final HBox hBoxLeft = new HBox(10); // ist vor der ButtonBar
    private final HBox hBoxRight = new HBox(10); // ist nach der ButtonBar
    private final ButtonBar buttonBar = new ButtonBar();

    private static ArrayList<PDialog> dialogList = new ArrayList<>();
    private final ScrollPane scrollPane = new ScrollPane();
    private PMaskerPane maskerPane = null;
    private boolean masker = false;
    private DECO deco = DECO.BORDER;

    public static enum STATE {
        STATE_OK,
        STATE_CANCEL,
        STATE_1,
        STATE_2,
        STATE_3,
        STATE_4,
        STATE_5
    }

    public PDialogExtra() {
        super(P2LibConst.primaryStage, null, "", true, true);
        initDialog();
    }

    public PDialogExtra(StringProperty conf, String title) {
        // ist nur ein einfacher Dialog, zentral über dem Hauptfenster
        super(P2LibConst.primaryStage, conf, title, true, true);
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

    public PDialogExtra(Stage ownerForCenteringDialog, StringProperty conf,
                        String title, boolean modal, boolean setOnlySize, DECO deco, boolean masker) {
        super(ownerForCenteringDialog, conf, title, modal, setOnlySize);
        this.deco = deco;
        this.masker = masker;
        maskerPane = new PMaskerPane();
        initDialog();
    }

    private static synchronized void addDialog(PDialog pDialog) {
        boolean found = false;
        for (PDialog dialog : dialogList) {
            if (dialog.equals(pDialog)) {
                found = true;
            }
        }
        if (!found) {
            dialogList.add(pDialog);
        }
    }

    private static synchronized void removeDialog(PDialog pDialog) {
        dialogList.remove(pDialog);
    }

    public static void closeAllDialog() {
        dialogList.stream().forEach(pDialog -> {
            Platform.runLater(() -> {
                pDialog.hide();
            });
        });
    }

    public static void showAllDialog() {
        dialogList.stream().forEach(pDialog -> {
            Platform.runLater(() -> {
                pDialog.showDialog();
            });
        });
    }

    @Override
    public void hide() {
        // close/hide are the same
        super.close();
    }

    @Override
    public void close() {
        //bei wiederkehrenden Dialogen: die pos/size merken
        removeDialog(this);
        super.close();
    }

    @Override
    public void init(boolean show) {
        if (show) {
            addDialog(this);
        }
        super.init(show);
    }

    @Override
    public void showDialog() {
        addDialog(this);
        super.showDialog();
    }

    //== getBoxes ==
    public VBox getVBoxCompleteDialog() {
        return vBoxCompleteDialog;
    }

    public HBox getHBoxOverAll() {
        hBoxOverAll.setVisible(true);
        hBoxOverAll.setManaged(true);
        return hBoxOverAll;
    }

    public HBox getHBoxTitle() {
        hBoxTitle.setVisible(true);
        hBoxTitle.setManaged(true);
        return hBoxTitle;
    }

    public VBox getVBoxCont() {
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

    //== add Buttons ==
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

    public void addOkCancelApplyButtons(Button btnOk, Button btnCancel, Button btnApply) {
        if (btnOk != null) {
            ButtonBar.setButtonData(btnOk, ButtonBar.ButtonData.OK_DONE);
            buttonBar.getButtons().addAll(btnOk);
        }
        if (btnCancel != null) {
            ButtonBar.setButtonData(btnCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(btnCancel);
        }
        if (btnApply != null) {
            ButtonBar.setButtonData(btnApply, ButtonBar.ButtonData.APPLY);
            buttonBar.getButtons().addAll(btnApply);
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

    //== maskerPane ==
    public void setMaskerVisible(boolean visible) {
        if (masker) {
            //ist nur dann, enthalten
            maskerPane.setMaskerVisible(visible, false, false);
        }
    }

    public void setMaskerVisible(boolean visible, boolean button) {
        if (masker) {
            //ist nur dann, enthalten
            maskerPane.setMaskerVisible(visible, button, button);
        }
    }

    public PMaskerPane getMaskerPane() {
        return maskerPane;
    }

    //==============================
    //und da wird dann gemacht
    private void initDialog() {
        initBefore();

        switch (deco) {
            case BORDER_BIG:
                initBorderBig();
                break;
            case BORDER:
                initBorder();
                break;
            case BORDER_SMALL:
                initBorderSmall();
                break;
            case NO_BORDER:
            default:
                initNoBorder();
                break;
        }

        addBottom();
    }

    private void initBefore() {
        if (masker) {
            StackPane.setAlignment(maskerPane, Pos.CENTER);
            maskerPane.setPadding(new Insets(4, 1, 1, 1));

            final StackPane stackPane = new StackPane(); // ist der gesamte Dialog
            super.setPane(stackPane);
            stackPane.getChildren().addAll(vBoxCompleteDialog, maskerPane);

        } else {
            super.setPane(vBoxCompleteDialog);
        }

        vBoxCompleteDialog.setPadding(new Insets(P2LibConst.DIST_EDGE));//ist der Bereich außerhalb des Rahmens

        hBoxOverAll.setVisible(false);//wird nur eingeschaltet, wenn abgerufen
        hBoxOverAll.setManaged(false);

        hBoxTitle.setVisible(false);//wird auch nur eingeschaltet, wenn abgerufen
        hBoxTitle.setManaged(false);
        hBoxTitle.getStyleClass().add("dialog-title");

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        vBoxCont.setPadding(new Insets(P2LibConst.DIST_EDGE));
        scrollPane.setContent(vBoxCont);
    }

    private void initBorderBig() {
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border-big");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        vBoxStyledBorder.getChildren().addAll(scrollPane);
        vBoxCompleteDialog.getChildren().addAll(hBoxOverAll, hBoxTitle, vBoxStyledBorder);
    }

    private void initBorder() {
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        vBoxStyledBorder.getChildren().addAll(scrollPane);
        vBoxCompleteDialog.getChildren().addAll(hBoxOverAll, hBoxTitle, vBoxStyledBorder);
    }

    private void initBorderSmall() {
        VBox vBoxStyledBorder = new VBox();
        vBoxStyledBorder.getStyleClass().add("dialog-border-small");
        vBoxStyledBorder.setSpacing(10);
        VBox.setVgrow(vBoxStyledBorder, Priority.ALWAYS);

        vBoxStyledBorder.getChildren().addAll(scrollPane);
        vBoxCompleteDialog.getChildren().addAll(hBoxOverAll, hBoxTitle, vBoxStyledBorder);
    }

    private void initNoBorder() {
        vBoxCompleteDialog.getChildren().addAll(hBoxOverAll, hBoxTitle, scrollPane);
    }

    private void addBottom() {
        HBox hButton = new HBox();
        hButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxLeft.setAlignment(Pos.CENTER_RIGHT);
        hBoxRight.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBar, Priority.ALWAYS);
        hButton.getChildren().addAll(hBoxLeft, buttonBar, hBoxRight);
        vBoxCompleteDialog.getChildren().addAll(hBoxOverButtons, hButton);
    }

    public enum DECO {
        BORDER_BIG, BORDER, BORDER_SMALL, NO_BORDER
    }
}
