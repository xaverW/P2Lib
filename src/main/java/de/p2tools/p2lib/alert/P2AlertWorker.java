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

import javafx.beans.property.BooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Optional;

public class P2AlertWorker {

    //=======================
    //Ok Cancel
    static boolean alertOkCancel(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    //=======================
    //yes no
    static P2Alert.BUTTON alert_yes_no(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnYes, btnNo);
        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(btnNo)).setDefaultButton(false);

        P2Alert.BUTTON ret = P2Alert.BUTTON.NO;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        }

        return ret;
    }

    //=======================
    //yes no
    static P2Alert.BUTTON alert_yes_no_remember(Stage stage, String title, String header, String content, BooleanProperty remember) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);
        alert.setContentText(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnYes, btnNo);
        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(btnNo)).setDefaultButton(false);

        P2Alert.BUTTON ret = P2Alert.BUTTON.NO;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        }
        return ret;
    }

    public static P2Alert.BUTTON alert_yes_no_remember(Stage stage, String title, String header,
                                                       String content, BooleanProperty remember, String optOutMsg) {
        final Alert alert = getAlert(stage, Alert.AlertType.CONFIRMATION, title, header);

        //Need to force the alert to layout in order to grab the graphic,
        //as we are replacing the dialog pane with a custom pane
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        //Create a new dialog pane that has a checkbox instead of the hide/show details button
        //Use the supplied callback for the action of the checkbox
        alert.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                CheckBox chkOptOut = new CheckBox();
                chkOptOut.setText(optOutMsg);
                chkOptOut.selectedProperty().bindBidirectional(remember);
                return chkOptOut;
            }
        });

        //Fool the dialog into thinking there is some expandable content
        //a Group won't take up any space if it has no children
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);

        //Reset the dialog graphic using the default style
        alert.getDialogPane().setGraphic(graphic);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setContentText(content);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnYes, btnNo);
        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(btnNo)).setDefaultButton(false);

        P2Alert.BUTTON ret = P2Alert.BUTTON.NO;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        }
        return ret;
    }

    //=======================
    //yes no Cancel
    static P2Alert.BUTTON alert_yes_no_cancel(Stage stage, String title, String header, String content, boolean noBtn) {
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

        P2Alert.BUTTON ret = P2Alert.BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = P2Alert.BUTTON.CANCEL;
        }

        return ret;
    }

    static P2Alert.BUTTON alert_yes_no_cancel(Stage stage, String title, String header, String content, boolean noBtn,
                                              String yesButton, String noButton) {
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

        P2Alert.BUTTON ret = P2Alert.BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = P2Alert.BUTTON.CANCEL;
        }

        return ret;
    }


    static P2Alert.BUTTON alert_yes_no_cancel(Stage stage, String title, String header, TextFlow content, boolean noBtn) {
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

        P2Alert.BUTTON ret = P2Alert.BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = P2Alert.BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = P2Alert.BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = P2Alert.BUTTON.CANCEL;
        }

        return ret;
    }


    //=======================
    // Help
    static boolean helpAlert(Stage stage, String header, TextFlow content) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, "Hilfe", header);

        alert.getDialogPane().setContent(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    static boolean helpAlert(Stage stage, String header, String content) {
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

    //=======================
    // Ok
    static boolean infoAlert(Stage stage, String title, String header, String content, boolean txtArea) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, title, header);

        if (txtArea) {
            ScrollPane scroll = new ScrollPane();
            TextArea ta = new TextArea(content);
            ta.setEditable(false);
            scroll.setContent(ta);
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

    //=======================
    // Ok
    static boolean errorAlert(Stage stage, String title, String header, String content) {
        final Alert alert = getAlert(stage, Alert.AlertType.ERROR, title, header);
        alert.setContentText(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    //=======================
    // Ok

    static boolean infoNoSelection(Stage stage) {
        final Alert alert = getAlert(stage, Alert.AlertType.INFORMATION, "keine Auswahl", "Es wurde nichts markiert.");
        alert.setContentText("Zeile auswählen!");

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private static Alert getAlert(Stage stage, Alert.AlertType alertType, String title, String header) {
        final Alert alert = new Alert(alertType);
        // aktuelles Gnome mag das nicht :(
        // alert.setResizable(true);

        // bei Oracle Jdk10 unter Linux geht der Dialog nur manchmal auf, stimmt was beim JDK nicht
        // so solls gehen:
        // this.resizable = true
        // this.onShown = {
        //      Platform.runLater {
        //          setResizable(false)
        //      }
        //  }

        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }
}