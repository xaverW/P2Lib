/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.tools.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class PFileName {
    private static final String STR = "__";
    private static final FastDateFormat FORMATTER_ddMMyyyyHHmmss = FastDateFormat.getInstance(STR + "yyyyMMdd_HHmmss");
    private static final FastDateFormat FORMATTER_ddMMyyyy = FastDateFormat.getInstance(STR + "yyyyMMdd");

    public static String getNextFileNameWithNo(String path, String selName, String suff) {
        String ret;

        Path dir = Paths.get(path);
        if (!Files.exists(dir)) {
            return selName;
        }

        if (suff.startsWith(".")) {
            suff = suff.replaceFirst(".", "");
        }

        String name = selName + "." + suff;
        Path baseDirectoryPath = Paths.get(path, name);
        int no = 1;

        while (Files.exists(baseDirectoryPath)) {
            name = selName + STR + no + "." + suff;
            baseDirectoryPath = Paths.get(path, name);
            ++no;
        }

        ret = baseDirectoryPath.getFileName().toString();
        return ret;
    }

    public static String getNextFileNameWithDate(String name, String suffix) {
        return getNextFileNameWithDate("", name, suffix);

    }

    public static String getNextFileNameWithDate(String path, String name, String suffix) {
        if (name == null || suffix == null) {
            return "";
        }


        String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
        if (!path.isEmpty()) {
            // erst mal versuchen mit gleichem Namen und nächstem Counter
            final String retName = checkFirstWithNewNumber(path, name, dotSuffix);
            if (!retName.isEmpty()) {
                return retName;
            }
            name = removeCounter(name);
        }


        String ret = name;
        final String date1 = FORMATTER_ddMMyyyy.format(new Date());
        final String date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date());
        final String containDate1 = getDateString(name, dotSuffix, FORMATTER_ddMMyyyy);
        final String containDate2 = getDateString(name, dotSuffix, FORMATTER_ddMMyyyyHHmmss);

        if (!name.endsWith(dotSuffix)) {
            // dann Name
            name = FilenameUtils.removeExtension(name); // evt. altes Suff entfernen
            ret = name;

        } else if (!containDate1.isEmpty()) {
            // dann Name + Datum2
            ret = cleanName(name, dotSuffix);
            ret = ret.replace(containDate1, date2);

        } else if (!containDate2.isEmpty()) {
            // dann Name
            ret = cleanName(name, dotSuffix);
            ret = ret.replace(containDate2, "");

        } else if (name.endsWith(dotSuffix)) {
            // dann Name + Datum1
            ret = cleanName(name, dotSuffix);
            ret = ret + date1;
        }

        if (!path.isEmpty()) {
            ret = getNextFileNameWithNo(path, ret, dotSuffix);
        } else {
            ret = ret + dotSuffix;
        }

        return ret;
    }

    private static String removeCounter(String name) {
        // __20181215 --> Datum darf nicht gelöscht werden
        return name.replaceAll(STR + "[0-9]{1,5}\\.", ".");
    }

    private static String checkFirstWithNewNumber(String path, String name, String dotSuffix) {

        if (!name.isEmpty() && name.endsWith(dotSuffix)) {
            Path p = Paths.get(path, name);
            if (Files.exists(p)) {
                final String n = FilenameUtils.removeExtension(removeCounter(name));
                return getNextFileNameWithNo(path, n, dotSuffix);
            }
        }

        return "";
    }

    private static String cleanName(String name, String suffix) {
        // alle "suffix" am Ende entfernen

        while (!name.equals(suffix) && name.endsWith(suffix)) {
            name = name.substring(0, name.lastIndexOf(suffix));
        }

        return name;
    }

    private static String getDateString(String name, String suffix, FastDateFormat format) {
        // Datumstring aus "name" extrahieren
        String ret = "";
        Date d = null;

        if (name.contains(STR) && name.endsWith(suffix)) {
            try {
                ret = name.substring(name.lastIndexOf(STR)).replaceAll(suffix, "");
                d = new Date(format.parse(ret).getTime());
            } catch (Exception ignore) {
                d = null;
            }
        }

        if (d != null && format.getPattern().length() == ret.length()) {
            return ret;
        }

        return "";
    }

    public static String getFilenameRelative(File file, String relative) {
        if (file == null) {
            return "";
        }

        String rel = file.toString();
        if (rel.startsWith(relative)) {
            rel = rel.replaceFirst(relative, "");
        }

        if (rel.startsWith(File.separator)) {
            rel = rel.replaceFirst(File.separator, "");
        }
        return rel;
    }
}
