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


package de.p2tools.p2Lib.checkForUpdates;

import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateTime;
import de.p2tools.p2Lib.tools.file.PFileUtils;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AutoUpdate {
    private void AutoUpdate(){
    }

    public static boolean update(String configDir, String progDirUrl){
        boolean ret = false;
        String tmpDir =PFileUtils.addsPath( configDir,
                "update_"+ new PLocalDate().getDateTime(PLocalDateTime.FORMAT_dd_MM_yyyy_HH_mm_ss));
        Path tmpPath = Path.of(tmpDir);
        if (tmpPath.toFile().exists()){
            return false;
        }

        if (PFileUtils.fileIsDirectoryExist(progDirUrl)){
            // Dann ist ein lokaler Pfad
        }

        return ret;
    }
}
