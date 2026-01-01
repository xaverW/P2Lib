package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P2CssFactory__CSS_2 {

    private static String cssPath;

    private P2CssFactory__CSS_2() {
    }

    public static List<String> getList() {
        cssPath = P2CssFactory.PATH_CSS + P2CssFactory.CSS.CSS_2.getPath();

        // zuerst ALLES
        ArrayList<String> setList = new ArrayList<>(Arrays.asList(getListAll()));

        // wenn nötig: DARK
        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(Arrays.asList(getListDark()));
        }

        // vom Programm: ALLES
        setList.addAll(Arrays.asList(P2LibConst.cssFile));

        // wenn nötig: DARK
        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(Arrays.asList(P2LibConst.cssFileDark));
        }

        return setList;
    }

    private static String[] getListAll() {
        return new String[]{
                "de/p2tools/p2lib/css/css_1/p2Css__all.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___fx.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___button.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___gui.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___maskerPane.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___toggleSwitch.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___table.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___tabPane.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___p2Notify.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___smallGui.css",
                "de/p2tools/p2lib/css/css_1/p2Css__all___dialog.css"
        };
    }

    private static String[] getListDark() {
        return new String[]{
                "de/p2tools/p2lib/css/css_1/p2Css__dark___all.css",
                "de/p2tools/p2lib/css/css_1/p2Css__dark___fx.css",
                "de/p2tools/p2lib/css/css_1/p2Css__dark___table.css"
        };
    }
}
