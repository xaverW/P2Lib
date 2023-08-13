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
 *
 * https://bitbucket.org/hansolo/enzo/overview
 * unter Apache License, Version 2.0, January 2004
 * Notification ist ein Tool von Gerrit Grunwald das ich mir für mein Projekte angepasst habe.
 */

package de.p2tools.p2lib.guitools.pnotification;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.ProgIconsP2Lib;
import de.p2tools.p2lib.tools.PException;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class P2Notification {

    public enum STATE {INFO, WARNING, SUCCESS, ERROR}

    static {
        Platform.runLater(() -> {
            Stage stage = P2LibConst.primaryStage;
            if (stage == null) {
                PException.throwPException(912036447, "PNotification: stage not set");
            }
            P2Notify.stage = stage;
        });
    }

    public static void addNotification(String title, String text, boolean error) {
        if (error) {
            addNotification(title, text, STATE.ERROR);
        } else {
            addNotification(title, text, STATE.INFO);
        }
    }

    public static void addNotification(String title, String text, STATE state) {
        Platform.runLater(() -> {
            final Image image = switch (state) {
                case INFO -> ProgIconsP2Lib.INFO_ICON.getImage();
                case WARNING -> ProgIconsP2Lib.WARNING_ICON.getImage();
                case SUCCESS -> ProgIconsP2Lib.SUCCESS_ICON.getImage();
                default -> ProgIconsP2Lib.ERROR_ICON.getImage();
            };
            final P2Notify notification = new P2Notify(title, text, image);
            notification.notify(notification);
        });
    }
}
