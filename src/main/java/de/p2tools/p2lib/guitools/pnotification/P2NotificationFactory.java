/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class P2NotificationFactory {
    private P2NotificationFactory() {
    }

    /**
     * Reorder the popup Notifications on screen so that the latest Notification will stay on top
     */
    static void preOrder(Pos popupLocation, double spacingY) {
        if (P2Notify.popups.isEmpty()) return;
        double actPos = getStartPos(P2Notify.stageRef, popupLocation, P2Notify.offsetY); // liefert den obersten/untersten Startpunkt

        for (int i = 0; i < P2Notify.popups.size(); ++i) {
            Popup pop = P2Notify.popups.get(i);
            switch (popupLocation) {
                case TOP_LEFT:
                case TOP_CENTER:
                case TOP_RIGHT: {
                    pop.setY(actPos);
                    actPos = actPos + pop.getHeight() + spacingY;
                }
                break;

                case BOTTOM_LEFT:
                case BOTTOM_CENTER:
                case BOTTOM_RIGHT:
                default: {
                    actPos = actPos - pop.getHeight();
                    pop.setY(actPos);
                    actPos = actPos - spacingY;
                }
                break;
            }
        }
//        double old = 0;
//        for (int i = 0; i < P2Notify.popups.size(); ++i) {
//            double d = P2Notify.popups.get(i).getY();
//            System.out.println("d - " + i + " " + d + " " + (d - old));
//            old = d;
//        }
    }

    static double getX(Stage stageRef, Pos popupLocation, double offsetX, double width) {
        if (null == stageRef) return calcX(popupLocation, offsetX, width,
                0.0, Screen.getPrimary().getBounds().getWidth());
        return calcX(popupLocation, offsetX, width, stageRef.getX(), stageRef.getWidth());
    }

    static double getStartPos(Stage stageRef, Pos popupLocation, double offsetY) {
        if (null == stageRef)
            return calcStartPosY(popupLocation, offsetY, 0.0, Screen.getPrimary().getBounds().getHeight());
        return calcStartPosY(popupLocation, offsetY, stageRef.getY(), stageRef.getHeight());
    }

    static double calcStartPosY(Pos popupLocation, double offsetY,
                                final double TOP, final double TOTAL_HEIGHT) {
        return switch (popupLocation) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> TOP + offsetY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> TOP + (TOTAL_HEIGHT) / 2 - offsetY;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> TOP + TOTAL_HEIGHT - offsetY;
            default -> 0.0;
        };
    }


    static double getY(Stage stageRef, Pos popupLocation, double offsetY, double height) {
        if (null == stageRef)
            return calcY(popupLocation, offsetY, height, 0.0, Screen.getPrimary().getBounds().getHeight());
        return calcY(popupLocation, offsetY, height, stageRef.getY(), stageRef.getHeight());
    }

    static double calcX(Pos popupLocation, double offsetX, double width,
                        final double LEFT, final double TOTAL_WIDTH) {
        return switch (popupLocation) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> LEFT + offsetX;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> LEFT + (TOTAL_WIDTH - width) * 0.5 - offsetX;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> LEFT + TOTAL_WIDTH - width - offsetX;
            default -> 0.0;
        };
    }

    static double calcY(Pos popupLocation, double offsetY, double height,
                        final double TOP, final double TOTAL_HEIGHT) {
        return switch (popupLocation) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> TOP + offsetY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> TOP + (TOTAL_HEIGHT - height) / 2 - offsetY;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> TOP + TOTAL_HEIGHT - height - offsetY;
            default -> 0.0;
        };
    }
}
