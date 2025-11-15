package de.p2tools.p2lib.css;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P2CssFactory {

    private static final String PATH_CSS = "de/p2tools/p2lib/css/";
    public static String CSS_ALL = "/css/";
    public static String CSS_DARK = "/css_d/";
    public static String CSS_WHITE = "/css_bw/";
    public static String CSS_BLACK_WHITE_DARK = "/css_bw_d/";


    public enum CSS {
        CSS_0("Standard", "css_0"),
        CSS_1("P2-1", "css_1"),
        CSS_2("P2-2", "css_2");

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
//        list.add(PATH_CSS + "p2Css_button.css");
//        list.add(PATH_CSS + "p2Css_maskerPane.css");
//        list.add(PATH_CSS + "p2Css_toggleSwitch.css");
//        list.add(PATH_CSS + "p2Css_p2Notify.css");
//        list.add(PATH_CSS + "p2Css_table.css");
//        list.add(PATH_CSS + "p2Css.css");
//        list.add(PATH_CSS + "p2Css_toolButton.css");
//        list.add(PATH_CSS + "p2Css_smallGui.css");
//        list.add(PATH_CSS + "p2Css_dialog.css");
//        list.add(PATH_CSS + "p2Css_gui.css");
//
//        if (P2LibConst.darkMode.getValue()) {
//            list.add(PATH_CSS + "p2Css_dark.css");
//            list.add(PATH_CSS + "p2Css_darkTable.css");
//        }
//
//        if (P2LibConst.blackWhite.getValue()) {
//            if (P2LibConst.darkMode.getValue()) {
//                list.add(PATH_CSS + "p2Css_bw_b.css");
//            } else {
//                list.add(PATH_CSS + "p2Css_bw_w.css");
//            }
//        }
//
//        // und die vom Programm
//        list.addAll(Arrays.asList(P2LibConst.cssFile));
//        if (P2LibConst.darkMode.getValue()) {
//            list.addAll(Arrays.asList(P2LibConst.cssFileDark));
//        }


        String cssDir;

        // ================================
        // von der P2Lib
        cssDir = PATH_CSS + P2LibConst.cssProp.get().getPath();
        ArrayList<String> setList = new ArrayList<>(getListFromJar(cssDir + CSS_ALL));

        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(getListFromJar(cssDir + CSS_DARK));
        }

        if (P2LibConst.blackWhite.getValue()) {
            if (P2LibConst.darkMode.getValue()) {
                setList.addAll(getListFromJar(cssDir + CSS_BLACK_WHITE_DARK));
            } else {
                setList.addAll(getListFromJar(cssDir + CSS_WHITE));
            }
        }

        // ================================
        // und jetzt  noch vom Programm
        cssDir = P2LibConst.cssProgramPath + P2LibConst.cssProp.get().getPath();
        setList.addAll(getProgramFileList(cssDir + CSS_ALL));

        if (P2LibConst.darkMode.getValue()) {
            setList.addAll(getProgramFileList(cssDir + CSS_DARK));
        }

        if (P2LibConst.blackWhite.getValue()) {
            if (P2LibConst.darkMode.getValue()) {
                setList.addAll(getProgramFileList(cssDir + CSS_BLACK_WHITE_DARK));
            } else {
                setList.addAll(getProgramFileList(cssDir + CSS_WHITE));
            }
        }

        if (scene != null) {
            scene.getStylesheets().setAll(setList);

            // entweder Größe oder 0 zum löschen
            scene.getRoot().setStyle("-fx-font-size: " + P2LibConst.fontSize.get() + " ;"); // .root { -fx-font-size: 12pt ;}
        }
    }

    private static ArrayList<String> getListFromJar(String cssDir) {
        List<Path> result = null;

        // geht beides!
        //        try {
        //            // get path of the current running JAR
        //            String jarPath = P2CssFactory.class.getProtectionDomain()
        //                    .getCodeSource()
        //                    .getLocation()
        //                    .toURI()
        //                    .getPath();
        //
        //            // file walks JAR
        //            URI uri = URI.create("jar:file:" + jarPath);
        //            try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
        //                Path f = fs.getPath(cssDir);
        //                result = Files.walk(fs.getPath(cssDir))
        //                        .filter(Files::isRegularFile)
        //                        .toList();
        //            }
        //            P2Log.sysLog("Es wurden : " + result.size() + " CSS-Dateien gefunden.");
        //        } catch (Exception exception) {
        //            P2Log.errorLog(645121459, "Die CSS-Files konnten nicht geladen werden");
        //        }

        try {
            // get path of the current running JAR
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path path = Path.of(classLoader.getResource(cssDir).getPath());
            String jarPath = path.toString();
            // file walks JAR
            URI uri = URI.create("jar:" + jarPath);
            try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path f = fs.getPath(cssDir);
                result = Files.walk(fs.getPath(cssDir))
                        .filter(Files::isRegularFile)
                        .sorted()
                        .toList();
            }
            P2Log.sysLog("Es wurden (getListFromJar): " + result.size() + " CSS-Dateien gefunden.");
        } catch (Exception exception) {
            P2Log.errorLog(645121459, "Die CSS-Files konnten nicht geladen werden");
        }

        ArrayList<String> arrayList = new ArrayList<>();
        if (result != null) {
            result.forEach(p -> arrayList.add(p.toString()));
        }
        return arrayList;
    }

    private static ArrayList<String> getProgramFileList(String cssDir) {
        ArrayList<String> arrayList = new ArrayList<>();

        // Das braucht beim Start aud dem ClassFile
        List<String> filenames = new ArrayList<>();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(cssDir);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
            while ((resource = br.readLine()) != null) {
                arrayList.add(cssDir + resource);
            }
            P2Log.sysLog("Es wurden (getProgramFileList): " + filenames.size() + " CSS-Dateien gefunden.");
        } catch (Exception ex) {
            P2Log.errorLog(108975462, "Die CSS-Files konnten nicht geladen werden");
        }
        arrayList.addAll(filenames);

        arrayList.addAll(getListFromJar(cssDir)); // brauchts beim Start aus dem FatJar!!
        return arrayList;
    }
}
