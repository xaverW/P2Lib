/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.configFile;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.scene.Scene;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class IoReadWriteStyle {
    public static String STYLE_START = "/* Programmweites style sheet */\n" +
            "\n";
    public static String STYLE =
            ".root {\n" +
                    "  -fx-font-size: ##pt ;\n" +
                    "}\n";
    public static String STYLE_END = "\n";

    private static final ArrayList<String> list = new ArrayList<>();

    private IoReadWriteStyle() {
    }

    public static boolean readStyle(Path filePath, Scene scene) {
        boolean ret = readData(filePath, scene);
        PLog.sysLog(list);
        list.clear();
        return ret;
    }

    public static synchronized void writeStyle(Path filePath, int size) {
        writeData(filePath, size);
        PLog.sysLog(list);
        list.clear();
    }

    private static boolean readData(Path filePath, Scene scene) {
        PDuration.counterStart("Style lesen");
        list.add("Start Lesen von: " + filePath.toAbsolutePath());
        boolean ret = false;

        if (Files.exists(filePath)) {
            try {
                // das geht alles :)
//                File f = new File("/home/emil/daten/software/java-fx-11/MTInfo/MTInfo/p2Style.css");
//                scene.getStylesheets().add("file:///" + f.getAbsolutePath());
//                scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
//                String uri = f.toURI().toString();
//                scene.getStylesheets().add(uri);

                scene.getStylesheets().removeAll(filePath.toUri().toString());
                scene.getStylesheets().add(filePath.toUri().toString());
                ret = true;
            } catch (NullPointerException ex) {
                PLog.errorLog(987549987, "style.css not found");
            }
        }

        list.add("gelesen!");
        PDuration.counterStop("Style lesen");
        return ret;
    }

    private static void writeData(Path filePath, int size) {

        try (BufferedWriter bufferedWriter = (
                new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(filePath), StandardCharsets.UTF_8)))) {

            list.add("Start Schreiben nach: " + filePath.toAbsolutePath());

            String style = STYLE_START;
            if (size > 0) {
                style += STYLE.replace("##", size + "");
            }
            style += STYLE_END;

            bufferedWriter.write(style);
            list.add("geschrieben!");
        } catch (final Exception ex) {
            PLog.errorLog(987010268, ex);
        }
    }
}
