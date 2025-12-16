package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.P2ColorFactory;
import javafx.scene.Scene;

public class P2CssFactory {

    public static final String PATH_CSS = "de/p2tools/p2lib/css/";
    private static String cssPath = PATH_CSS;

    public enum CSS {
        CSS_0("Standard", "css_0/"),
        CSS_1("P2-1", "css_1/"),
        CSS_2("P2-2", "css_2/");

        private final String text;
        private final String path;

        CSS(String text, String path) {
            this.text = text;
            this.path = path;
        }

        @Override
        public String toString() {
            return text;
        }

        public String getText() {
            return text;
        }

        public String getPath() {
            return path;
        }
    }

    private P2CssFactory() {
    }

    public static void addP2CssToScene() {
        if (P2LibConst.primaryStage != null && P2LibConst.primaryStage.getScene() != null) {
            Scene scene = P2LibConst.primaryStage.getScene();
            addP2CssToScene(scene);
        }
    }

    public static void addP2CssToScene(Scene scene) {
        if (scene == null) {
            return;
        }

        switch (P2LibConst.cssProp.get()) {
            case CSS_0 -> scene.getStylesheets().setAll(P2CssFactory__CSS_0.getList());
            case CSS_1 -> scene.getStylesheets().setAll(P2CssFactory__CSS_1.getList());
            case CSS_2 -> scene.getStylesheets().setAll(P2CssFactory__CSS_2.getList());
        }

        String gui = P2ColorFactory.getColor(P2LibConst.guiColor.getValueSafe());
        if (!gui.isEmpty()) {
            gui = "-pGuiColor: " + gui + ";";
            if (P2LibConst.primaryStage != null && P2LibConst.primaryStage.getScene() != null) {
                // Gui-Root-Color
                // -pGuiColor: red;
                P2LibConst.primaryStage.getScene().getRoot().setStyle(gui);
            }
            scene.getRoot().setStyle(gui);
        }
        // entweder Größe oder 0 zum Löschen
        scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
    }
}
