/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.mediathek.download;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class P2InfoFile {
    public static void writeInfoFile(String destPath, String destPathFile, String fileNameWithoutSuffix,
                                     String url, String downloadSize,
                                     String channel, String theme, String title, String date, String time,
                                     String duration, String urlWebsite, String description) {
        P2Log.sysLog(new String[]{"Infofile schreiben nach: ", destPath});

        new File(destPath).mkdirs();
        // final Path path = Paths.get(download.getFileNameWithoutSuffix() + ".txt");
        final Path path = getInfoFilePath(fileNameWithoutSuffix);
        if (path == null) {
            return;
        }

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(path));
             OutputStreamWriter osw = new OutputStreamWriter(dos);
             BufferedWriter br = new BufferedWriter(osw)) {

            br.write("Sender" + ":        " + channel);
            br.write(P2LibConst.LINE_SEPARATOR);
            br.write("Thema" + ":         " + theme);
            br.write(P2LibConst.LINE_SEPARATORx2);
            br.write("Titel" + ":         " + title);
            br.write(P2LibConst.LINE_SEPARATORx2);
            br.write("Datum" + ":         " + date);
            br.write(P2LibConst.LINE_SEPARATOR);
            br.write("Zeit" + ":          " + time);
            br.write(P2LibConst.LINE_SEPARATOR);
            br.write("Dauer [min]" + ":   " + duration);
            br.write(P2LibConst.LINE_SEPARATOR);
            br.write("Größe [MB]" + ":    " + downloadSize);
            br.write(P2LibConst.LINE_SEPARATORx2);

            br.write("Website" + P2LibConst.LINE_SEPARATOR);
            br.write(urlWebsite);
            br.write(P2LibConst.LINE_SEPARATORx2);

            br.write("Url" + P2LibConst.LINE_SEPARATOR);
            br.write(url);
            br.write(P2LibConst.LINE_SEPARATORx2);

            if (!description.isEmpty()) {
                int anz = 0;
                for (final String s : description.split(" ")) {
                    anz += s.length();
                    br.write(s + ' ');
                    if (anz > 50) {
                        br.write(P2LibConst.LINE_SEPARATOR);
                        anz = 0;
                    }
                }
            }
            br.write(P2LibConst.LINE_SEPARATORx2);
            br.flush();
            P2Log.sysLog(new String[]{"Infofile", "  geschrieben"});
        } catch (final IOException ex) {
            P2Log.errorLog(975410369, destPathFile);
        }
    }

    public static Path getInfoFilePath(String fileNameWithoutSuffix) {
        Path path;
        try {
            path = Paths.get(getInfoFileStr(fileNameWithoutSuffix));
        } catch (Exception ex) {
            path = null;
            P2Log.errorLog(987451202, "InfofilePath");
        }
        return path;
    }

    private static String getInfoFileStr(String fileNameWithoutSuffix) {
        return fileNameWithoutSuffix + ".txt";
    }
}
