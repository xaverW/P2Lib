package de.p2tools.p2lib.dialogs;

import de.p2tools.p2lib.tools.P2ToolsFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class P2DirFileChooserAppThread {
    private P2DirFileChooserAppThread() {
    }

    public static String DirChooser(Stage stage, String txtPath) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        StringProperty stringProperty = new SimpleStringProperty();
        Platform.runLater(() -> {
            stringProperty.set(P2DirFileChooser.DirChooser(stage, txtPath));
            atomicBoolean.set(false);
        });
        while (atomicBoolean.get()) {
            P2ToolsFactory.pause(500);
        }
        return stringProperty.get();
    }
}
