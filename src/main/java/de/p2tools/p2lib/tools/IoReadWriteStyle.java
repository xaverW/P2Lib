/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.scene.Scene;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class IoReadWriteStyle {
    public static String STYLE_START = "/* Programm weites style sheet */\n" + "\n";
    public static String STYLE = ".root {\n" + "  -fx-font-size: ##pt ;\n" + "}\n";
    public static String STYLE_END = "\n";
    private static final ArrayList<String> list = new ArrayList<>();

    private IoReadWriteStyle() {
    }

    public static boolean readStyle(Path filePath, Scene scene) {
        boolean ret = readData(filePath, scene);
        P2Log.sysLog(list);
        list.clear();
        return ret;
    }

    public static synchronized void writeStyle(Path filePath, int size) {
        writeData(filePath, size);
        P2Log.sysLog(list);
        list.clear();
    }

    private static boolean readData(Path filePath, Scene scene) {
        P2Duration.counterStart("Style lesen");
        list.add("Start Lesen von: " + filePath.toAbsolutePath());
        boolean ret = false;

        if (Files.exists(filePath)) {
            try {
                scene.getStylesheets().removeAll(filePath.toUri().toString());
                scene.getStylesheets().add(filePath.toUri().toString());
                ret = true;
            } catch (NullPointerException ex) {
                P2Log.errorLog(987549987, "style.css not found");
            }
        }

        list.add("gelesen!");
        P2Duration.counterStop("Style lesen");
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
            P2Log.errorLog(987010268, ex);
        }
    }
}
