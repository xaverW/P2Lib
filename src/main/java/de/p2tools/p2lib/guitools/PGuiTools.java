/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.guitools;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.*;

public class PGuiTools {

    public static Region getHBoxGrower() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public static Region getVBoxGrower() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    public static HBox getHDistance(int size) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(size, 0, 0, 0));
        return hBox;
    }

    public static VBox getVDistance(int size) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, size, 0, 0));
        return vBox;
    }

    /**
     * Center a component (e.g. Dialog) on screen
     *
     * @param component The reference component
     * @param absolute  if true, use absolute position, otherwise relative
     */
    public static void centerOnScreen(final Component component, final boolean absolute) {
        final int width = component.getWidth();
        final int height = component.getHeight();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (height / 2);
        if (!absolute) {
            x /= 2;
            y /= 2;
        }
        component.setLocation(x, y);
    }

}
