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

package de.p2tools.p2Lib.icons;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class GetIcon {

    public static String P2_ICON_PATH = "/de/p2tools/p2Lib/icons/";

    public static void addWindowP2Icon(Stage stage) {
        final String P2_ICON_32 = "P2_32.png";

        System.out.println("--");

        final int ICON_WIDTH = 58;
        final int ICON_HEIGHT = 58;
        Image icon = GetIcon.getImage(P2_ICON_32, P2_ICON_PATH, ICON_WIDTH, ICON_HEIGHT);
        stage.getIcons().add(0, icon);
    }

    public static void addWindowP2Icon(Stage stage, String path) {
        final int ICON_WIDTH = 58;
        final int ICON_HEIGHT = 58;

        if (path.isEmpty() || !new File(path).exists()) {
            addWindowP2Icon(stage);
            return;
        }

        try {
            Image icon = new Image(new File(path).toURI().toString(), ICON_WIDTH, ICON_HEIGHT, true, true);
            stage.getIcons().add(0, icon);
        } catch (Exception ex) {
            PLog.errorLog(204503978, ex);
            addWindowP2Icon(stage);
        }
    }

    public static ImageView getImageView(String strIcon) {
        return new ImageView(getImage(strIcon, P2_ICON_PATH, 0, 0));
    }

    public static ImageView getImageView(String strIcon, int w, int h) {
        return new ImageView(getImage(strIcon, P2_ICON_PATH, w, h));
    }

    public static ImageView getImageView(String strIcon, String path, int w, int h) {
        return new ImageView(getImage(strIcon, path, w, h));
    }

    public static Image getImage(String strIcon) {
        return getImage(strIcon, P2_ICON_PATH, 0, 0);
    }

    public static Image getImage(String strIcon, int w, int h) {
        return getImage(strIcon, P2_ICON_PATH, w, h);
    }

    public static Image getImage(String strIcon, String path) {
        return getImage(strIcon, path, 0, 0);
    }

    public static Image getImage(String strIcon, String path, int w, int h) {
        return getStdImage(strIcon, path, w, h);
    }

    private static Image getStdImage(String strIcon, String path, int w, int h) {
        return new Image(P2LibConst.class.getResource(path + strIcon).toExternalForm(),
                w, h, false, true);
    }

//    private static Image getStdImage(String strIcon, String path, int w, int h) {
//        return new Image(P2LibConst.class.getResourceAsStream(path + strIcon),
//                w, h, false, true);
//    }
}
