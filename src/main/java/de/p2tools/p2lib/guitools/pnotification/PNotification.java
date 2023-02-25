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


package de.p2tools.p2lib.guitools.pnotification;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.PException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * changed by Xaver W.
 * 27.07.2018
 */
public class PNotification {

    public enum STATE {INFO, WARNING, SUCCESS, ERROR}

    private static Notification.Notifier notifier;

    static {
        Platform.runLater(() -> {
            Stage stage = P2LibConst.primaryStage;
            if (stage == null) {
                PException.throwPException(912036447, "PNotification: stage not set");
            }

            Notification.setStage(stage);
            notifier = NotifierBuilder.create()
                    .popupLocation(Pos.BOTTOM_RIGHT)
                    .popupLifeTime(Duration.millis(5000))
                    .build();
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
            final Image image;
            switch (state) {
                case INFO:
                    image = Notification.INFO_ICON;
                    break;
                case WARNING:
                    image = Notification.WARNING_ICON;
                    break;
                case SUCCESS:
                    image = Notification.SUCCESS_ICON;
                    break;
                case ERROR:
                default:
                    image = Notification.ERROR_ICON;
            }

            final Notification notification =
                    NotificationBuilder.create().title(title).message(text).image(image).build();
            notifier.notify(notification);
        });
    }

}






