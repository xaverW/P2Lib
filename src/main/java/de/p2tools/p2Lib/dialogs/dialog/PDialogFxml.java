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
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;


public class PDialogFxml {
    private Scene scene = null;
    private Stage stage = null;

    private final StringProperty conf;
    private final String fxml;
    private final boolean modal;
    private final String title;
    private final Stage primaryStage;

    private double stageWidth = 0;
    private double stageHeight = 0;

    public PDialogFxml(Stage primaryStage, String fxml, StringProperty conf, String title, boolean modal) {
        this.primaryStage = primaryStage;
        this.fxml = fxml;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(String fxml, StringProperty conf, String title, boolean modal) {
        this.primaryStage = P2LibConst.primaryStage;
        this.fxml = fxml;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(Stage primaryStage, StringProperty conf, String title, boolean modal) {
        this.primaryStage = primaryStage;
        this.fxml = null;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(StringProperty conf, String title, boolean modal) {
        this.primaryStage = P2LibConst.primaryStage;
        this.fxml = null;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(Stage primaryStage, String fxml, String title, boolean modal) {
        this.primaryStage = primaryStage;
        this.fxml = fxml;
        this.conf = null;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(String fxml, String title, boolean modal) {
        this.primaryStage = P2LibConst.primaryStage;
        this.fxml = fxml;
        this.conf = null;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(Stage primaryStage, String title, boolean modal) {
        this.primaryStage = primaryStage;
        this.fxml = null;
        this.conf = null;
        this.modal = modal;
        this.title = title;
    }

    public PDialogFxml(String title, boolean modal) {
        this.primaryStage = P2LibConst.primaryStage;
        this.fxml = null;
        this.conf = null;
        this.modal = modal;
        this.title = title;
    }

    public void init() {
        // die Dialoge werden beim Programmstart angelegt
        Platform.runLater(() -> {
            initDialog();
        });
    }

    public void init(Pane pane) {
        // die Dialoge werden beim Programmstart angelegt
        Platform.runLater(() -> {
            init(pane, false);
        });
    }

    public void init(boolean show) {
        initDialog();
        if (show) {
            showDialog();
        }
    }

    public void init(Pane pane, boolean show) {
        setSize(pane);
        initDialog();
        if (show) {
            showDialog();
        }
    }

    private void initDialog() {
        try {
            if (scene == null) {
                final URL fxmlUrl = getClass().getResource(fxml);
                final FXMLLoader fXMLLoader = new FXMLLoader(fxmlUrl);
                fXMLLoader.setController(this);
                final Parent root = fXMLLoader.load();

                setSize(root);
            }

            P2LibInit.addP2LibCssToScene(scene);
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
                    if (escEvent(keyEvent)) {
                        close();
                    }
                }
            });

            make();

            if (conf == null) {
                scene.getWindow().sizeToScene();
            }

        } catch (final Exception exc) {
            PLog.errorLog(984120202, exc);
        }
    }

    private void setSize(Parent parent) {
        if (conf == null) {
            this.scene = new Scene(parent);
        } else {
            int w = PGuiSize.getWidth(conf);
            int h = PGuiSize.getHeight(conf);
            if (w > 0 && h > 0) {
                this.scene = new Scene(parent, PGuiSize.getWidth(conf), PGuiSize.getHeight(conf));
            } else {
                this.scene = new Scene(parent);
            }
        }
    }

    public boolean escEvent(KeyEvent keyEvent) {
        return true;
    }

    public void hide() {
        // close/hide are the same
        close();
    }

    public void close() {
        //bei wiederkehrenden Dialogen: die pos/size merken
        stageWidth = stage.getWidth();
        stageHeight = stage.getHeight();

        if (conf != null) {
            PGuiSize.getSizeScene(conf, stage);
        }
        stage.close();
    }

    public void showDialog() {
        if (stageHeight > 0 && stageWidth > 0) {
            //bei wiederkehrenden Dialogen die pos/size setzen
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);
        }

        if (conf == null || !PGuiSize.setPos(conf, stage)) {
//            setPrimaryStage();
            stage.centerOnScreen();
        }

        if (modal) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

//    private void setPrimaryStage() {
//        // vor Primär zentrieren
//        if (primaryStage != null) {
//            ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
//                double stageWidth = newValue.doubleValue();
//                stage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - stageWidth / 2);
//            };
//            ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
//                double stageHeight = newValue.doubleValue();
//                stage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - stageHeight / 2);
//            };
//
//            stage.widthProperty().addListener(widthListener);
//            stage.heightProperty().addListener(heightListener);
//
//            stage.setOnShown(e -> {
//                stage.widthProperty().removeListener(widthListener);
//                stage.heightProperty().removeListener(heightListener);
//            });
//        }
//    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public void make() {
    }

}
