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

import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.icon.GetIcon;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class PDialog {
    private Scene scene = null;
    private Stage stage = null;
    private Pane pane;

    private final StringProperty sizeConfiguration;
    private final boolean modal;
    private final boolean setOnlySize;
    private final String title;
    private final Stage ownerForCenteringDialog;

    private double stageWidth = 0;
    private double stageHeight = 0;

    PDialog(Stage ownerForCenteringDialog, StringProperty sizeConfiguration,
            String title, boolean modal, boolean setOnlySize) {

        this.ownerForCenteringDialog = ownerForCenteringDialog;
        this.sizeConfiguration = sizeConfiguration;
        this.modal = modal;
        this.title = title;
        this.setOnlySize = setOnlySize;
    }

    void setPane(Pane pane) {
        this.pane = pane;
    }

    void init(boolean show) {
        try {
            createNewScene(pane);
            if (scene == null) {
                PException.throwPException(912012458, "no scene");
            }

            updateCss();
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);

            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setOnCloseRequest(e -> {
                e.consume();
                close();
            });
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    close();
                }
            });

            GetIcon.addWindowP2Icon(stage);
            make();

            if (setOnlySize || sizeConfiguration == null) {
                scene.getWindow().sizeToScene();
            }

            if (show) {
                showDialog();
            }

        } catch (final Exception exc) {
            PLog.errorLog(152030145, exc);
        }
    }

    public void updateCss() {
        P2LibInit.addP2LibCssToScene(scene);
    }

    private void createNewScene(Pane pane) {
        if (sizeConfiguration == null) {
            this.scene = new Scene(pane);
        } else {
            int w = PGuiSize.getWidth(sizeConfiguration);
            int h = PGuiSize.getHeight(sizeConfiguration);
            if (w > 0 && h > 0) {
                this.scene = new Scene(pane, PGuiSize.getWidth(sizeConfiguration), PGuiSize.getHeight(sizeConfiguration));
            } else {
                this.scene = new Scene(pane);
            }
        }
    }

    public void hide() {
        // close/hide are the same
        close();
    }

    public void close() {
        //bei wiederkehrenden Dialogen: die pos/size merken
        stageWidth = stage.getWidth();
        stageHeight = stage.getHeight();

        if (sizeConfiguration != null) {
            PGuiSize.getSizeScene(sizeConfiguration, stage);
        }
        stage.close();
    }

    public void showDialog() {
        if (stageHeight > 0 && stageWidth > 0) {
            //bei wiederkehrenden Dialogen die pos/size setzen
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);
        }

        if (setOnlySize || sizeConfiguration == null || !PGuiSize.setPos(sizeConfiguration, stage)) {
            if (ownerForCenteringDialog == null) {
                PDialogFactory.setInCenterOfScreen(stage);
            } else {
                PDialogFactory.setInFrontOfPrimaryStage(ownerForCenteringDialog, stage);
            }
        }

        if (modal) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    protected void make() {
    }

}
