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

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class PGuiSize {

    public static void getSizeScene(StringProperty property, Stage stage) {
        if (stage != null && stage.getScene() != null && property != null) {
            System.out.println(stage.getX() + " - " + stage.getY());
            property.set((int) stage.getScene().getWidth() + ":"
                    + (int) stage.getScene().getHeight()
                    + ':'
                    + (int) stage.getX()
                    + ':'
                    + (int) stage.getY());
        }
    }

    public static void getSizeWindow(StringProperty property, Stage stage) {
        if (stage != null &&
                stage.getScene() != null && stage.getScene().getWindow() != null &&
                property != null) {

            property.set((int) stage.getScene().getWindow().getWidth() + ":"
                    + (int) stage.getScene().getWindow().getHeight()
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

        if (posX < 0 || posY < 0) {
            // dann wäre es außerhalb des Desktops
            PLog.sysLog("setPos - x<0||y<0 - x/y: " + posX + " / " + posY);
            stage.centerOnScreen();
        } else {
            stage.setX(posX);
            stage.setY(posY);
        }
        return true;
    }

    public static void showSave(Stage stage) {
        System.out.println("\nshowSave(Stage stage)");
        Platform.runLater(() -> {
            if (!stage.isShowing()) {
                System.out.println("   show");
                stage.show();
            }

            final double posX, posY;
            posX = stage.getX();
            posY = stage.getY();
            if (posX < 0 || posY < 0) {
                // dann wäre es außerhalb des Desktops
                System.out.println("   showSave (x/y): " + posX + " - " + posY);
                stage.centerOnScreen();
            }

            stage.toFront();
        });
    }
}
