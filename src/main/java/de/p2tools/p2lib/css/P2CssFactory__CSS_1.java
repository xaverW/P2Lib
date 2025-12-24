package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P2CssFactory__CSS_1 {

    private static String cssPath;

    private P2CssFactory__CSS_1() {
    }

    public static List<String> getList() {
        cssPath = P2CssFactory.PATH_CSS + P2CssFactory.CSS.CSS_1.getPath();

        // ================================
        // von der P2Lib
        ArrayList<String> setList = new ArrayList<>(Arrays.asList(getListAll()));

        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(Arrays.asList(getListDark()));

            if (P2LibConst.guiTheme1.getValue()) {
                setList.addAll(Arrays.asList(getListDarkBlackWhite()));
            }

        } else {
            if (P2LibConst.guiTheme1.getValue()) {
                setList.addAll(Arrays.asList(getListBlackWhite()));
            }
        }

        // und noch die vom Programm
        setList.addAll(Arrays.asList(getListProgramAll()));
        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(Arrays.asList(getListProgramDark()));
        }

        return setList;
    }

    private static String[] getListAll() {
        return new String[]{};
    }
//    private static String[] getListAll() {
//        return new String[]{cssPath + "p2Css__all___button.css",
//                cssPath + "p2Css__all___maskerPane.css",
//                cssPath + "p2Css__all___toggleSwitch.css",
//                cssPath + "p2Css__all___p2Notify.css",
//                cssPath + "p2Css__all___table.css",
//                cssPath + "p2Css__all___tabPane.css",
//                cssPath + "p2Css__all.css",
//                cssPath + "p2Css__all___toolButton.css",
//                cssPath + "p2Css__all___smallGui.css",
//                cssPath + "p2Css__all___dialog.css",
//                cssPath + "p2Css__all___gui.css"};
//    }

    private static String[] getListDark() {
        return new String[]{};
    }
//    private static String[] getListDark() {
//        return new String[]{cssPath + "p2Css__dark___all.css",
//                cssPath + "p2Css__dark___table.css"};
//    }

    private static String[] getListBlackWhite() {
        return new String[]{};
    }

    private static String[] getListDarkBlackWhite() {
        return new String[]{};
    }
//    private static String[] getListBlackWhite() {
//        return new String[]{cssPath + "p2Css__dark__white___all.css"};
//    }
//
//    private static String[] getListDarkBlackWhite() {
//        return new String[]{cssPath + "p2Css__dark__black___all.css"};
//    }

    // Und aus dem Programm
    private static String[] getListProgramAll() {
        return P2LibConst.cssFile;
    }

    private static String[] getListProgramDark() {
        return P2LibConst.cssFileDark;
    }
}
