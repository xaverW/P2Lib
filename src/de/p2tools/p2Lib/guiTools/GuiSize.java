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
import javafx.stage.Stage;

public class GuiSize {

    public static void getSizeScene(StringProperty nr, Stage stage) {
        if (stage != null && stage.getScene() != null && nr != null) {
            nr.set((int) stage.getScene().getWidth() + ":"
                    + (int) stage.getScene().getHeight()
                    + ':'
                    + (int) stage.getX()
                    + ':'
                    + (int) stage.getY());
        }
    }

    public static int getWidth(StringProperty nr) {
        int width = 0;
        final String[] arr = nr.getValue().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                width = Integer.parseInt(arr[0]);
            }
        } catch (final Exception ex) {
            width = 0;
        }

        return width;
    }

    public static int getHeight(StringProperty nr) {
        int height = 0;
        final String[] arr = nr.getValue().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                height = Integer.parseInt(arr[1]);
            }
        } catch (final Exception ex) {
            height = 0;
        }

        return height;
    }

    public static void setPos(StringProperty nr, Stage stage) {
        int posX, posY;
        posX = 0;
        posY = 0;
        final String[] arr = nr.getValue().split(":");
        try {
            if (arr.length == 4) {
                posX = Integer.parseInt(arr[2]);
                posY = Integer.parseInt(arr[3]);
            }
        } catch (final Exception ex) {
            posX = 0;
            posY = 0;
        }
        if (posX > 0 && posY > 0) {
            stage.setX(posX);
            stage.setY(posY);
        }
    }

}
