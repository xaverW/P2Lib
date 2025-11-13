package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class P2CssFactory {

    private static final String PATH_CSS = "de/p2tools/p2lib/css/css_1/";
    public static CSS css = CSS.CSS_0;

    public enum CSS {
        CSS_0("Standard Java"), CSS_1("P2-1");
        private final String text;


        CSS(String suff) {
            this.text = suff;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    private P2CssFactory() {
    }

    public static void setCss(CSS css) {
        P2CssFactory.css = css;
    }

    public void setCssToScene(Scene scene) {
        addCss(scene, css);
    }

    private void addCss(Scene scene, CSS css) {
        String cssDir;
        switch (css) {
            case CSS_0 -> cssDir = PATH_CSS + "css_0/";
            case CSS_1 -> cssDir = PATH_CSS + "css_1/p2Css.css";
            default -> cssDir = PATH_CSS + "css_0/";
        }
    }

    private void addCssToScene(Scene scene, Set<String> fileList) {

        if (scene != null) {
            scene.getStylesheets().setAll(fileList);

            // entweder Größe oder 0 zum löschen
            scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
        }
    }

    public static void addP2CssToScene(Scene scene) {
        List<String> list = new ArrayList<>();
        list.add(PATH_CSS + "p2Css_button.css");
        list.add(PATH_CSS + "p2Css_maskerPane.css");
        list.add(PATH_CSS + "p2Css_toggleSwitch.css");
        list.add(PATH_CSS + "p2Css_p2Notify.css");
        list.add(PATH_CSS + "p2Css_table.css");
        list.add(PATH_CSS + "p2Css.css");
        list.add(PATH_CSS + "p2Css_toolButton.css");
        list.add(PATH_CSS + "p2Css_smallGui.css");
        list.add(PATH_CSS + "p2Css_dialog.css");
        list.add(PATH_CSS + "p2Css_gui.css");

        if (P2LibConst.darkMode.getValue()) {
            list.add(PATH_CSS + "p2Css_dark.css");
            list.add(PATH_CSS + "p2Css_darkTable.css");
        }

        if (P2LibConst.blackWhite.getValue()) {
            if (P2LibConst.darkMode.getValue()) {
                list.add(PATH_CSS + "p2Css_bw_b.css");
            } else {
                list.add(PATH_CSS + "p2Css_bw_w.css");
            }
        }

        // und die vom Programm
        list.addAll(Arrays.asList(P2LibConst.cssFile));
        if (P2LibConst.darkMode.getValue()) {
            list.addAll(Arrays.asList(P2LibConst.cssFileDark));
        }

        if (scene != null) {
            scene.getStylesheets().setAll(list);

            // entweder Größe oder 0 zum löschen
            scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
        }
    }
}
