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

package de.p2tools.p2lib.dialogs.dialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.guitools.P2WindowIcon;
import de.p2tools.p2lib.tools.IoReadWriteStyle;
import de.p2tools.p2lib.tools.PException;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;


public class P2Dialog {
    private static String iconPath = "";
    private StringProperty sizeConfiguration;
    private final boolean modal;
    private final boolean setOnlySize; // dann wird nur die Größe nicht aber die Position gesetzt
    private final String title;
    private final Stage ownerForCenteringDialog;
    private Scene scene = null;
    private Stage stage = null;
    private ObjectProperty<Stage> stageProp = new SimpleObjectProperty<>(null);
    private Pane pane;

    P2Dialog(Stage ownerForCenteringDialog, StringProperty sizeConfiguration,
             String title, boolean modal, boolean setOnlySize) {

        this.ownerForCenteringDialog = ownerForCenteringDialog;
        this.sizeConfiguration = sizeConfiguration;
        this.modal = modal;
        this.title = title;
        this.setOnlySize = setOnlySize;
    }

    P2Dialog(Stage ownerForCenteringDialog, StringProperty sizeConfiguration,
             String title, boolean modal, boolean setOnlySize, String iconPath) {

        this.ownerForCenteringDialog = ownerForCenteringDialog;
        this.sizeConfiguration = sizeConfiguration;
        this.modal = modal;
        this.title = title;
        this.setOnlySize = setOnlySize;
        P2Dialog.iconPath = iconPath;
    }

    public static String getIconPath() {
        return iconPath;
    }

    public static void setIconPath(String iconPath) {
        P2Dialog.iconPath = iconPath;
    }

    void setPane(Pane pane) {
        this.pane = pane;
    }

    void init(boolean show) {
        try {
            scene = new Scene(pane);
            if (scene == null) {
                PException.throwPException(912012458, "no scene");
            }
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    close();
                }
            });

            stage = new Stage();
            stageProp.setValue(stage);
            stage.setScene(scene);
            stage.setTitle(title);
            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.setOnCloseRequest(e -> {
                e.consume();
                close();
            });
            //brauchts zwar nicht 2x, der Dialog "springt" dann aber weniger
            stage.setOnShowing(e -> {
                if (setOnlySize) {
                    P2GuiSize.setOnlySize(sizeConfiguration, stage, ownerForCenteringDialog);
                } else {
                    P2GuiSize.setSizePos(sizeConfiguration, stage, ownerForCenteringDialog);
                }
            });
            stage.setOnShown(e -> {
                if (setOnlySize) {
                    P2GuiSize.setOnlySize(sizeConfiguration, stage, ownerForCenteringDialog);
                } else {
                    P2GuiSize.setSizePos(sizeConfiguration, stage, ownerForCenteringDialog);
                }
            });

            updateCss();
            setIcon();
            make();

            P2DialogFactory.addSizeListener(stage, sizeConfiguration);

            if (show) {
                showDialog();
            }

        } catch (final Exception exc) {
            P2Log.errorLog(152030145, exc);
        }
    }

    public void setIcon() {
        if (iconPath.isEmpty() || !new File(iconPath).exists()) {
            P2WindowIcon.addWindowP2Icon(stage);
            return;
        }

        try {
            Image icon = new Image(new File(iconPath).toURI().toString(),
                    P2LibConst.WINDOW_ICON_WIDTH, P2LibConst.WINDOW_ICON_HEIGHT, true, true);
            stage.getIcons().add(0, icon);
        } catch (Exception ex) {
            P2Log.errorLog(204503978, ex);
            P2WindowIcon.addWindowP2Icon(stage);
        }
    }

    public void updateCss() {
        P2LibInit.addP2CssToScene(scene);
        if (P2LibConst.styleFile != null && !P2LibConst.styleFile.isEmpty() && scene != null) {
            final Path path = Path.of(P2LibConst.styleFile);
            IoReadWriteStyle.readStyle(path, scene);
        }
    }

    public void hide() {
        // close/hide are the same
        close();
    }

    public void close() {
        stage.close();
    }

    public void showDialog() {
        if (!stage.isShowing()) {
            if (modal) {
                stage.showAndWait();
            } else {
                stage.show();
            }
        }
        stage.requestFocus();
        stage.toFront();
    }

    public StringProperty getSizeConfiguration() {
        return sizeConfiguration;
    }

    public void setSizeConfiguration(StringProperty sizeConfiguration) {
        this.sizeConfiguration = sizeConfiguration;
    }

    public Stage getStage() {
        return stage;
    }

    public ObjectProperty<Stage> getStageProp() {
        return stageProp;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    protected void make() {
    }
}
