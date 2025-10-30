package de.p2tools.p2lib.alert;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class P2AlertAppThread {
    private P2AlertAppThread() {
    }

    //=======================
    //Ok Cancel
    public static boolean showAlertOkCancel(String title, String header, String content) {
        return showAlertOkCancel(null, title, header, content);
    }

    public static boolean showAlertOkCancel(Stage stage, String title, String header, String content) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanProperty booleanProperty = new SimpleBooleanProperty();
        Platform.runLater(() -> {
            booleanProperty.set(P2AlertWorker.alertOkCancel(stage == null ? P2LibConst.actStage : stage, title, header, content));
            atomicBoolean.set(false);
        });
        while (atomicBoolean.get()) {
            P2ToolsFactory.pause(500);
        }
        return booleanProperty.get();
    }

    //=======================
    //yes no
    public static P2Alert.BUTTON showAlert_yes_no(String title, String header, String content) {
        return showAlert_yes_no(null, title, header, content);
    }

    public static P2Alert.BUTTON showAlert_yes_no(Stage stage, String title, String header, String content) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        ObjectProperty<P2Alert.BUTTON> property = new SimpleObjectProperty<>();

        Platform.runLater(() -> {
            property.set(P2AlertWorker.alert_yes_no(stage == null ? P2LibConst.actStage : stage, title, header, content));
            atomicBoolean.set(false);
        });
        while (atomicBoolean.get()) {
            P2ToolsFactory.pause(500);
        }
        return property.get();
    }

    //=======================
    //Ok
    public static boolean infoAlert(String title, String header, String content) {
        return infoAlert(null, title, header, content);
    }

    public static boolean infoAlert(Stage stage, String title, String header, String content) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanProperty booleanProperty = new SimpleBooleanProperty();
        Platform.runLater(() -> {
            booleanProperty.set(P2AlertWorker.infoAlert(stage == null ? P2LibConst.actStage : stage, title, header, content, false));
            atomicBoolean.set(false);
        });
        while (atomicBoolean.get()) {
            P2ToolsFactory.pause(500);
        }
        return booleanProperty.get();
    }


    //=======================
    //Ok
    public static boolean showErrorAlert(String header, String content) {
        return showErrorAlert(P2LibConst.actStage, header, content);
    }

    public static boolean showErrorAlert(Stage stage, String header, String content) {
        return showErrorAlert(stage, "Fehler", header, content);
    }

    public static boolean showErrorAlert(String title, String header, String content) {
        return showErrorAlert(null, title, header, content);
    }

    public static boolean showErrorAlert(Stage stage, String title, String header, String content) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanProperty booleanProperty = new SimpleBooleanProperty();
        Platform.runLater(() -> {
            booleanProperty.set(P2AlertWorker.errorAlert(stage == null ? P2LibConst.actStage : stage, title, header, content));
            atomicBoolean.set(false);
        });
        while (atomicBoolean.get()) {
            P2ToolsFactory.pause(500);
        }
        return booleanProperty.get();
    }

}
