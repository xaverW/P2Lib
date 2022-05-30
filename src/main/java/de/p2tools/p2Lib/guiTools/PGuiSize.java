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

package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.dialogs.dialog.PDialogFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PGuiSize {

    public static void getSizeStage(StringProperty property, Stage stage) {
        if (stage != null && property != null) {
            property.set((int) stage.getWidth() + ":"
                    + (int) stage.getHeight()
                    + ':'
                    + (int) stage.getX()
                    + ':'
                    + (int) stage.getY());
        }
    }

    public static void getSizeScene(StringProperty property, Stage stage, Scene scene) {
        if (scene != null && property != null) {
            property.set((int) scene.getWidth() + ":"
                    + (int) scene.getHeight()
                    + ':'
                    + (int) stage.getX()
                    + ':'
                    + (int) stage.getY());
        }
    }

    public static int getWidth(StringProperty property) {
        int width = 0;
        final String[] arr = property.getValue().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                width = Integer.parseInt(arr[0]);
            }
        } catch (final Exception ex) {
            width = 0;
        }

        return width;
    }

    public static int getHeight(StringProperty property) {
        int height = 0;
        final String[] arr = property.getValue().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                height = Integer.parseInt(arr[1]);
            }
        } catch (final Exception ex) {
            height = 0;
        }

        return height;
    }

    public static void setSizePos(Stage stage, StringProperty sizeConfiguration) {
        setSizePos(stage, sizeConfiguration, null, false);
    }

    public static void setSizePos(Stage stage, StringProperty sizeConfiguration,
                                  Stage ownerForCenteringDialog, boolean onlySize) {
        //Größe, Pos setzen
        if (sizeConfiguration != null && !sizeConfiguration.getValueSafe().isEmpty()) {
            //gespeicherte Größe setzen
            int w = PGuiSize.getWidth(sizeConfiguration);
            int h = PGuiSize.getHeight(sizeConfiguration);
            if (w > 0 && h > 0) {
                stage.setWidth(w);
                stage.setHeight(h);
            }
        } else {
            //dann einpassen
            stage.sizeToScene();
        }

        //Pos setzen
        if (onlySize || sizeConfiguration == null || !PGuiSize.setPos(sizeConfiguration, stage)) {
            if (ownerForCenteringDialog == null) {
                stage.centerOnScreen();
            } else {
                PDialogFactory.setInFrontOfPrimaryStage(ownerForCenteringDialog, stage);
            }
        }
    }

    public static boolean setPos(StringProperty property, Stage stage) {
        int posX, posY;
        posX = 0;
        posY = 0;
        final String[] arr = property.getValue().split(":");
        if (arr.length != 4) {
            // dann ists eh noch leer
            return false;
        }

        try {
            if (arr.length == 4) {
                posX = Integer.parseInt(arr[2]);
                posY = Integer.parseInt(arr[3]);
            }
        } catch (final Exception ex) {
            return false;
        }

        setSavePosition(posX, posY, stage);
        return true;
    }

    private static void setSavePosition(double posX, double posY, Stage stage) {
        if (posX < 0 || posY < 0) {
            // dann wäre es außerhalb des Desktops
            PLog.sysLog("setPosSave and center (x/y): " + posX + " / " + posY);
            stage.centerOnScreen();
        } else {
            stage.setX(posX);
            stage.setY(posY);
        }
    }
}
