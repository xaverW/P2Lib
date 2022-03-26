/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.guiTools.PGuiSize;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PDialogFactory {
    private PDialogFactory() {
    }

    public static void showDialog(Stage stage, StringProperty sizeConfiguration) {
        showDialog(stage, sizeConfiguration, 0, 0,
                null, false);
    }

    public static void showDialog(Stage stage, StringProperty sizeConfiguration, double stageHeight, double stageWidth,
                                  Stage ownerForCenteringDialog, boolean modal) {

        //Größe setzen
        if (stageHeight > 0 && stageWidth > 0) {
            //die gemerkte Größe wieder setzen
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);
        } else if (sizeConfiguration != null && !sizeConfiguration.getValueSafe().isEmpty()) {
            //gespeicherte Größe setzen
            int w = PGuiSize.getWidth(sizeConfiguration);
            int h = PGuiSize.getHeight(sizeConfiguration);
            if (w > 0 && h > 0) {
                stage.setWidth(w);
                stage.setHeight(h);
            }
        }

        //Pos setzen
        if (sizeConfiguration == null || !PGuiSize.setPos(sizeConfiguration, stage)) {
            if (ownerForCenteringDialog == null) {
                stage.centerOnScreen();
            } else {
                setInFrontOfPrimaryStage(ownerForCenteringDialog, stage);
            }
        }

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

    public static void setSizePos(Stage stage, StringProperty sizeConfiguration, double stageHeight, double stageWidth,
                                  Stage ownerForCenteringDialog) {
        //Größe setzen
        if (stageHeight > 0 && stageWidth > 0) {
            //die gemerkte Größe wieder setzen
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);
        } else if (sizeConfiguration != null && !sizeConfiguration.getValueSafe().isEmpty()) {
            //gespeicherte Größe setzen
            int w = PGuiSize.getWidth(sizeConfiguration);
            int h = PGuiSize.getHeight(sizeConfiguration);
            if (w > 0 && h > 0) {
                stage.setWidth(w);
                stage.setHeight(h);
            }
        }

        //Pos setzen
        if (sizeConfiguration == null || !PGuiSize.setPos(sizeConfiguration, stage)) {
            if (ownerForCenteringDialog == null) {
                stage.centerOnScreen();
            } else {
                setInFrontOfPrimaryStage(ownerForCenteringDialog, stage);
            }
        }
    }

    public static void setInCenterOfScreen(Stage stage) {
        // im Monitor zentrieren
        //todo: testen stage.centerOnScreen();
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

    public static void setInFrontOfPrimaryStage(Stage ownerForCenteringDialog, Stage stage) {
        // vor Primärfenster des Programms zentrieren
        if (ownerForCenteringDialog == null) {
            return;
        }

        ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
            setStage(stage, ownerForCenteringDialog);
        };
        ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
            setStage(stage, ownerForCenteringDialog);
        };

        stage.widthProperty().addListener(widthListener);
        stage.heightProperty().addListener(heightListener);

        stage.setOnShown(e -> {
            stage.widthProperty().removeListener(widthListener);
            stage.heightProperty().removeListener(heightListener);
        });
        setStage(stage, ownerForCenteringDialog);
    }

    private static void setStage(Stage stage, Stage owner) {
        stage.setX(owner.getX() + owner.getWidth() / 2 - stage.getWidth() / 2);
        stage.setY(owner.getY() + owner.getHeight() / 2 - stage.getHeight() / 2);
    }
}
