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
import de.p2tools.p2lib.tools.P2ToolsFactory;
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
import java.util.ArrayList;


public class P2Dialog {
    static final ArrayList<P2Dialog> dialogList = new ArrayList<>();
    private static String iconPath = "";
    private StringProperty sizeConfiguration;
    private final boolean modal;
    private final boolean setOnlySize; // dann wird nur die Größe nicht aber die Position gesetzt
    private final String title;
    private final Stage ownerForCenteringDialog;
    private Scene scene = null;
    private Stage stage = null;
    private ObjectProperty<Stage> stageProp = new SimpleObjectProperty<>(null);
    private Pane pane = new Pane();

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

    public void init(boolean show) {
        try {
            addDialog(this);
            // geht beides
//            scene = new Scene(pane);
            if (sizeConfiguration == null) {
                scene = new Scene(pane);
            } else {
                scene = new Scene(pane,
                        P2GuiSize.getSceneSize(sizeConfiguration, true),
                        P2GuiSize.getSceneSize(sizeConfiguration, false));
            }

            stage = new Stage();
            stageProp.setValue(stage);
            stage.setScene(scene);
            // stage.sizeToScene(); // macht Probleme
            stage.setTitle(title);

            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    close();
                }
            });
            stage.setOnCloseRequest(e -> {
                e.consume();
                close();
            });

            if (P2ToolsFactory.getOs() == P2ToolsFactory.OperatingSystemType.LINUX) {
                // brauchts bei aktuellem GNOME
                P2GuiSize.setMinSize(sizeConfiguration, stage);
                if (!setOnlySize) {
                    P2GuiSize.setSizePos(sizeConfiguration, stage, ownerForCenteringDialog);
                }

                scene.widthProperty().addListener((u, o, n) -> {
                    stage.setMinHeight(0);
                    stage.setMinWidth(0);
                });
                scene.heightProperty().addListener((u, o, n) -> {
                    stage.setMinHeight(0);
                    stage.setMinWidth(0);
                });
                scene.setOnMouseEntered(mouseEvent -> {
                    stage.setMinHeight(0);
                    stage.setMinWidth(0);
                });
            }

            //braucht's zwar nicht 2x, der Dialog "springt" dann aber weniger
            stage.setOnShowing(e -> {
                if (setOnlySize) {
                    P2GuiSize.setSize(sizeConfiguration, stage);
                } else {
                    P2GuiSize.setSizePos(sizeConfiguration, stage, ownerForCenteringDialog);
                }
            });
            stage.setOnShown(e -> {
                if (setOnlySize) {
                    P2GuiSize.setSize(sizeConfiguration, stage);
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

    public void updateCss() {
        P2LibInit.addP2CssToScene(scene);
    }

    public void setIcon() {
        if (iconPath.isEmpty() || !new File(iconPath).exists()) {
            P2WindowIcon.addWindowIcon(stage);
            return;
        }

        try {
            Image icon = new Image(new File(iconPath).toURI().toString(),
                    P2LibConst.WINDOW_ICON_WIDTH, P2LibConst.WINDOW_ICON_HEIGHT, true, true);
            stage.getIcons().clear();
            stage.getIcons().add(0, icon);
        } catch (Exception ex) {
            P2Log.errorLog(204503978, ex);
            P2WindowIcon.addWindowIcon(stage);
        }
    }

    public void hide() {
        // wiederkehrender Dialog
        stage.hide();
    }

    public void close() {
        // endgültig schließen und Größe merken
        saveDialog();
        dialogList.remove(this);
        P2Log.debugLog("Anzahl Dialoge: " + dialogList.size());
        stage.close();
    }

    public void saveDialog() {
        P2GuiSize.getSize(sizeConfiguration, stage);
    }

    public void showDialog() {
        stage.requestFocus();
        stage.toFront();
        if (!stage.isShowing()) {
            if (modal) {
                stage.showAndWait();
            } else {
                stage.show();
            }
        }
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

    private static synchronized void addDialog(P2Dialog p2Dialog) {
        boolean found = false;
        for (P2Dialog dialog : dialogList) {
            if (dialog.equals(p2Dialog)) {
                found = true;
                break;
            }
        }
        if (!found) {
            dialogList.add(p2Dialog);
        }
        P2Log.debugLog("Anzahl Dialoge: " + dialogList.size());
    }
}
