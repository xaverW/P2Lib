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

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class PDialog {
    private Scene scene = null;
    private Stage stage = null;

    private final StringProperty conf;
    private final boolean modal;
    private final String title;
    private final Stage owner;

    private double stageWidth = 0;
    private double stageHeight = 0;

    public PDialog(Stage owner, StringProperty conf, String title, boolean modal) {
        this.owner = owner;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialog(StringProperty conf, String title, boolean modal) {
        this.owner = PConst.primaryStage;
        this.conf = conf;
        this.modal = modal;
        this.title = title;
    }

    public PDialog(String title, boolean modal) {
        this.owner = PConst.primaryStage;
        this.conf = null;
        this.modal = modal;
        this.title = title;
    }

    public void init(Pane pane) {
        // die Dialoge werden beim Programmstart angelegt
        Platform.runLater(() -> init(pane, false));
    }

    public void init(Pane pane, boolean show) {
        try {
            setSize(pane);

            String css = this.getClass().getResource(PConst.cssFile).toExternalForm();
            scene.getStylesheets().add(css);

            if (scene == null) {
                PException.throwPException(912012458, "no scene");
            }

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

            make();

            if (conf == null) {
                scene.getWindow().sizeToScene();
            }

            if (show) {
                showDialog();
            }

        } catch (final Exception exc) {
            PLog.errorLog(152030145, exc);
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
            if (owner == null) {
                setInCenterOfScreen();
            } else {
                setInFrontOfPrimaryStage();
            }
        }

        if (modal) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    private void setInFrontOfPrimaryStage() {
        // vor Primär zentrieren
        if (owner != null) {
            ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
                double stageWidth = newValue.doubleValue();
                stage.setX(owner.getX() + owner.getWidth() / 2 - stageWidth / 2);
            };
            ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
                double stageHeight = newValue.doubleValue();
                stage.setY(owner.getY() + owner.getHeight() / 2 - stageHeight / 2);
            };

            stage.widthProperty().addListener(widthListener);
            stage.heightProperty().addListener(heightListener);

            stage.setOnShown(e -> {
                stage.widthProperty().removeListener(widthListener);
                stage.heightProperty().removeListener(heightListener);
            });
        }
    }

    private void setInCenterOfScreen() {
        // vor Primär zentrieren
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        if (screenBounds != null) {
            ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
                double stageWidth = newValue.doubleValue();
                stage.setX(screenBounds.getWidth() / 2 - stageWidth / 2);
            };
            ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
                double stageHeight = newValue.doubleValue();
                stage.setY(screenBounds.getHeight() / 2 - stageHeight / 2);
            };

            stage.widthProperty().addListener(widthListener);
            stage.heightProperty().addListener(heightListener);

            stage.setOnShown(e -> {
                stage.widthProperty().removeListener(widthListener);
                stage.heightProperty().removeListener(heightListener);
            });
        }
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public void make() {
    }

}