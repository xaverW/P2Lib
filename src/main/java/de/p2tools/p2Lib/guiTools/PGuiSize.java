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

    public static int getPosX(StringProperty property) {
        int posX = 0;
        final String[] arr = property.getValue().split(":");

        try {
            if (arr.length == 4) {
                posX = Integer.parseInt(arr[2]);
            }
        } catch (final Exception ex) {
            posX = 0;
        }

        return posX;
    }

    public static int getPosY(StringProperty property) {
        int posY = 0;
        final String[] arr = property.getValue().split(":");

        try {
            if (arr.length == 4) {
                posY = Integer.parseInt(arr[3]);
            }
        } catch (final Exception ex) {
            posY = 0;
        }

        return posY;
    }

    public static void setSizePos(StringProperty sizeConfiguration, Stage newStage, Stage ownerForCenteringDialog) {
        setSizePos(sizeConfiguration, newStage, ownerForCenteringDialog, true, true);
    }

    public static void setOnlySize(StringProperty sizeConfiguration, Stage stage) {
        //nur die Größe setzen
        setSizePos(sizeConfiguration, stage, null, false, true);
    }

    public static void setOnlyPos(StringProperty sizeConfiguration, Stage newStage) {
        //nur die Größe setzen
        setSizePos(sizeConfiguration, newStage, null, true, false);
    }

    public static void setOnlyPos(StringProperty sizeConfiguration, Stage newStage, Stage ownerForCenteringDialog) {
        //nur die Größe setzen
        setSizePos(sizeConfiguration, newStage, ownerForCenteringDialog, true, false);
    }

    private static boolean setSizePos(StringProperty sizeConfiguration,
                                      Stage newStage, Stage ownerForCenteringDialog,
                                      boolean setPos,
                                      boolean setSize) {

        int h = 0, w = 0, posX = 0, posY = 0;
        boolean size = false, pos = false;

        String[] arr = {""};
        if (sizeConfiguration != null && !sizeConfiguration.getValueSafe().isEmpty()) {
            arr = sizeConfiguration.getValue().split(":");
        }

        if (arr.length >= 2) {
            //dann gibts zumindest die Größe
            size = true;
            w = PGuiSize.getWidth(sizeConfiguration);
            h = PGuiSize.getHeight(sizeConfiguration);
        }
        if (arr.length == 4) {
            //und auch die Pos
            pos = true;
            posX = PGuiSize.getPosX(sizeConfiguration);
            posY = PGuiSize.getPosY(sizeConfiguration);
        }

        //Größe setzen
        if (setSize && size) {
            newStage.setWidth(w);
            newStage.setHeight(h);
        } else {
            //dann einpassen
            newStage.sizeToScene();
        }

        //Pos setzen
        if (setPos && pos) {
            newStage.setX(posX);
            newStage.setY(posY);
        } else {
            setInFrontOfPrimaryStage(newStage, ownerForCenteringDialog);
        }

        if (!setPos && setSize && size) {
            return true;
        }
        if (!setSize && setPos && pos) {
            return true;
        } else if (setSize && setPos && size && pos) {
            return true;
        } else {
            return false;
        }
    }

    private static void setInFrontOfPrimaryStage(Stage newStage, Stage ownerForCenteringDialog) {
        // vor Primärfenster des Programms zentrieren
        if (ownerForCenteringDialog == null) {
            newStage.centerOnScreen();
            return;
        }
        setStage(newStage, ownerForCenteringDialog);
    }

    private static void setStage(Stage newStage, Stage owner) {
        double nX = newStage.getWidth();
        double ny = newStage.getHeight();
        double x, y;
        if (nX >= 0 || ny >= 0) {
            x = owner.getX() + owner.getWidth() / 2 - newStage.getWidth() / 2;
            y = owner.getY() + owner.getHeight() / 2 - newStage.getHeight() / 2;
        } else {
            x = owner.getX() + owner.getWidth() / 2;
            y = owner.getY() + owner.getHeight() / 2;
        }
        newStage.setX(x);
        newStage.setY(y);
    }
}
