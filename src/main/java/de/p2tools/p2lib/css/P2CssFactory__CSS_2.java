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
        };
    }

    private static String[] getListDark() {
        return new String[]{
        };
    }
}
