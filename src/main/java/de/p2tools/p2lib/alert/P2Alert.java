/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.alert;

import de.p2tools.p2lib.P2LibConst;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class P2Alert extends P2AlertWorker {

    public enum BUTTON {UNKNOWN, YES, NO, CANCEL}


    //=======================
    //Ok Cancel
    public static boolean showAlertOkCancel(String title, String header, String content) {
        return alertOkCancel(P2LibConst.primaryStage, title, header, content);
    }

    public static boolean showAlertOkCancel(Stage stage, String title, String header, String content) {
        return alertOkCancel(stage, title, header, content);
    }

    //=======================
    //yes no
    public static BUTTON showAlert_yes_no(String title, String header, String content) {
        return alert_yes_no(P2LibConst.primaryStage, title, header, content);
    }

    public static BUTTON showAlert_yes_no(Stage stage, String title, String header, String content) {
        return alert_yes_no(stage, title, header, content);
    }


    //=======================
    //yes no Cancel
    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content) {
        return alert_yes_no_cancel(P2LibConst.primaryStage, title, header, content, true);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, String content, boolean noBtn) {
        return alert_yes_no_cancel(stage, title, header, content, noBtn);
    }

    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content, boolean noBtn,
                                                 String yesButton, String noButton) {

        return alert_yes_no_cancel(P2LibConst.primaryStage, title, header, content, noBtn, yesButton, noButton);
    }


    //=======================
    //yes no Cancel
    public static BUTTON showAlert_yes_no_cancel(String title, String header, TextFlow content, boolean noBtn) {
        return alert_yes_no_cancel(P2LibConst.primaryStage, title, header, content, noBtn);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, TextFlow content, boolean noBtn) {
        return alert_yes_no_cancel(stage, title, header, content, noBtn);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, String content, boolean noBtn,
                                                 String yesButton, String noButton) {

        return alert_yes_no_cancel(stage, title, header, content, noBtn,
                yesButton, noButton);
    }


    //=======================
    //Help
    public static boolean showHelpAlert(String header, TextFlow content) {
        return helpAlert(P2LibConst.primaryStage, header, content);
    }

    public static boolean showHelpAlert(Stage stage, String header, TextFlow content) {
        return helpAlert(stage, header, content);
    }

    public static boolean showHelpAlert(String header, String content) {
        return helpAlert(P2LibConst.primaryStage, header, content);
    }

    public static boolean showHelpAlert(Stage stage, String header, String content) {
        return helpAlert(stage, header, content);
    }

    //=======================
    //Ok
    public static boolean showInfoAlert(String title, String header, String content) {
        return infoAlert(P2LibConst.primaryStage, title, header, content, false);
    }

    public static boolean showInfoAlert(String title, String header, String content, boolean txtArea) {
        return infoAlert(P2LibConst.primaryStage, title, header, content, txtArea);
    }

    public static boolean showInfoAlert(Stage stage, String title, String header, String content) {
        return infoAlert(stage, title, header, content, false);
    }

    public static boolean showInfoAlert(Stage stage, String title, String header, String content, boolean txtArea) {
        return infoAlert(stage, title, header, content, txtArea);
    }

    //=======================
    //Ok
    public static boolean showErrorAlert(String header, String content) {
        return errorAlert(P2LibConst.primaryStage, "Fehler", header, content);
    }

    public static boolean showErrorAlert(Stage stage, String header, String content) {
        return errorAlert(stage, "Fehler", header, content);
    }

    public static boolean showErrorAlert(String title, String header, String content) {
        return errorAlert(P2LibConst.primaryStage, title, header, content);
    }

    public static boolean showErrorAlert(Stage stage, String title, String header, String content) {
        return errorAlert(stage, title, header, content);
    }

    //=======================
    //Ok
    public static boolean showInfoNoSelection() {
        return infoNoSelection(P2LibConst.primaryStage);
    }

    public static boolean showInfoNoSelection(Stage stage) {
        return infoNoSelection(stage);
    }
}
