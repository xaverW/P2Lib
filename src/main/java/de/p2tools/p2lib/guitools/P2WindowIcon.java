/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.P2ProgIcons;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class P2WindowIcon {
    private P2WindowIcon() {
    }

    public static void setStageIcon(String imagePath) {
        // /tmp/path/icon.png
        if (imagePath.isEmpty()) {
            P2LibConst.STAGE_ICON = P2LibConst.STAGE_ICON_ORG;

        } else {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    P2LibConst.STAGE_ICON = new Image(file.toURI().toString(),
                            P2LibConst.WINDOW_ICON_WIDTH, P2LibConst.WINDOW_ICON_HEIGHT, true, true);

                } else {
                    P2Log.sysLog("Das vorgegebene Programm-Icon gibt es nicht");
                    P2LibConst.STAGE_ICON = P2LibConst.STAGE_ICON_ORG;
                }
            } catch (Exception ex) {
                P2Log.errorLog(987545412, ex, "Kann das vorgegebene Programm-Icon nicht setzen");
                P2LibConst.STAGE_ICON = P2LibConst.STAGE_ICON_ORG;
            }
        }

        P2WindowIcon.addWindowIcon();
    }

    public static void setOrgIcon(String imagePath) {
        // de/p2tools/p2lib/icons/icon.png
        // Ist das Icon wenn der User kein eigenes vorgibt
        if (imagePath.isEmpty()) {
            P2LibConst.STAGE_ICON_ORG = P2ProgIcons.P2_STAGE_ICON.getImage();
            return;
        }

        try {
            Image image = new Image(ClassLoader.getSystemResource(imagePath).toURI().toString(),
                    P2LibConst.WINDOW_ICON_WIDTH, P2LibConst.WINDOW_ICON_HEIGHT, true, true);
            P2LibConst.STAGE_ICON_ORG = image;
        } catch (Exception ex) {
            P2Log.errorLog(987545412, ex, "Kann das vorgegebene Programm-Icon nicht setzen");
            P2LibConst.STAGE_ICON_ORG = P2ProgIcons.P2_STAGE_ICON.getImage();
        }
    }

    public static void addWindowIcon() {
        addWindowIcon(P2LibConst.actStage);
        P2DialogExtra.setIconForAllDialog();
    }

    public static void addWindowIcon(Stage stage) {
        try {
            stage.getIcons().clear();
            stage.getIcons().add(0, P2LibConst.STAGE_ICON);
        } catch (Exception ex) {
            // passiert beim StartDialog
            P2Log.errorLog(945720146, "Kann Window-Icon nicht setzen");
            stage.getIcons().add(0, P2ProgIcons.P2_STAGE_ICON.getImage());
        }
    }
}
