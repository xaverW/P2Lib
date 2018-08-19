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

package de.p2tools.p2Lib.dialog;

import de.p2tools.p2Lib.PConst;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Optional;

public class PAlert {

    public enum BUTTON {UNKNOWN, YES, NO, CANCEL}


    public static boolean showAlert(String title, String header, String content) {
        return showAlert(PConst.primaryStage, title, header, content);
    }

    public static boolean showAlert(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static BUTTON showAlert_yes_no(String title, String header, String content) {
        return showAlert_yes_no(PConst.primaryStage, title, header, content);
    }

    public static BUTTON showAlert_yes_no(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnYes, btnNo);
        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(btnNo)).setDefaultButton(false);

        BUTTON ret = BUTTON.NO;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        }

        return ret;
    }


    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content) {
        return showAlert_yes_no_cancel(PConst.primaryStage, title, header, content, true);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, String content, boolean noBtn) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (noBtn) {
            alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        } else {
            alert.getButtonTypes().setAll(btnYes, btnCancel);
        }

        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);

        BUTTON ret = BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = BUTTON.CANCEL;
        }

        return ret;
    }

    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content,
                                                 boolean noBtn, String yesButton, String noButton) {
        return showAlert_yes_no_cancel(PConst.primaryStage, title, header, content, noBtn, yesButton, noButton);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, String content,
                                                 boolean noBtn, String yesButton, String noButton) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        ButtonType btnYes = new ButtonType(yesButton, ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType(noButton, ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (noBtn) {
            alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        } else {
            alert.getButtonTypes().setAll(btnYes, btnCancel);
        }

        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);

        BUTTON ret = BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = BUTTON.CANCEL;
        }

        return ret;
    }

    public static BUTTON showAlert_yes_no_cancel(String title, String header, TextFlow content, boolean noBtn) {
        return showAlert_yes_no_cancel(PConst.primaryStage, title, header, content, noBtn);
    }

    public static BUTTON showAlert_yes_no_cancel(Stage stage, String title, String header, TextFlow content, boolean noBtn) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.getDialogPane().setContent(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (noBtn) {
            alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        } else {
            alert.getButtonTypes().setAll(btnYes, btnCancel);
        }

        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);

        BUTTON ret = BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = BUTTON.CANCEL;
        }

        return ret;
    }


    public static boolean showHelpAlert(String header, TextFlow content) {
        return showHelpAlert(PConst.primaryStage, header, content);
    }

    public static boolean showHelpAlert(Stage stage, String header, TextFlow content) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, "Hilfe", header);

        alert.getDialogPane().setContent(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showHelpAlert(String header, String content) {
        return showHelpAlert(PConst.primaryStage, header, content);
    }

    public static boolean showHelpAlert(Stage stage, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, "Hilfe", header);

        ScrollPane scroll = new ScrollPane();
        TextArea ta = new TextArea(content);
        ta.setEditable(false);
        ta.setWrapText(true);
        scroll.setContent(ta);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        alert.getDialogPane().setContent(scroll);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoAlert(String title, String header, String content) {
        return showInfoAlert(PConst.primaryStage, title, header, content);
    }

    public static boolean showInfoAlert(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, title, header);

        ScrollPane scroll = new ScrollPane();
        TextArea ta = new TextArea(content);
        ta.setEditable(false);
        scroll.setContent(ta);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        alert.getDialogPane().setContent(scroll);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoAlert(String title, String header, String content, boolean txtArea) {
        return showInfoAlert(PConst.primaryStage, title, header, content, txtArea);
    }

    public static boolean showInfoAlert(Stage stage, String title, String header, String content, boolean txtArea) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, title, header);

        if (txtArea) {
            ScrollPane scroll = new ScrollPane();
            scroll.setContent(new TextArea(content));
            scroll.setFitToHeight(true);
            scroll.setFitToWidth(true);
            alert.getDialogPane().setContent(scroll);
        } else {
            alert.setContentText(content);
        }

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showErrorAlert(String header, String content) {
        return showErrorAlert(PConst.primaryStage, header, content);
    }

    public static boolean showErrorAlert(Stage stage, String header, String content) {
        return showErrorAlert(stage, "Fehler", header, content);
    }

    public static boolean showErrorAlert(String title, String header, String content) {
        return showErrorAlert(PConst.primaryStage, title, header, content);
    }

    public static boolean showErrorAlert(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.ERROR, title, header);
        alert.setContentText(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoNoSelection() {
        return showInfoNoSelection(PConst.primaryStage);
    }

    public static boolean showInfoNoSelection(Stage stage) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, "keine Auswahl", "Es wurden nichts markiert.");
        alert.setContentText("Zeile auswählen!");

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private static Alert getAlert(Stage stage, Alert.AlertType alertType, String title, String header) {
        final Alert alert = new Alert(alertType);
        alert.setResizable(true); // todo bei Oracle Jdk10 unter Linux geht der Dialog nur manchmal auf, stimmt was beim JDK nicht
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }
}
