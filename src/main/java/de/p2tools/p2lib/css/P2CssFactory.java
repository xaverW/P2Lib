package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;
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

    public static void addP2CssToScene(Scene scene) {
        if (scene != null) {
            switch (P2LibConst.cssProp.get()) {
                case CSS_0 -> scene.getStylesheets().setAll(P2CssFactory__CSS_0.getList());
                case CSS_1 -> scene.getStylesheets().setAll(P2CssFactory__CSS_1.getList());
                case CSS_2 -> scene.getStylesheets().setAll(P2CssFactory__CSS_2.getList());
            }

            // entweder Größe oder 0 zum löschen
            scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
        }
    }
}
