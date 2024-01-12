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


package de.p2tools.p2lib.tools.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class PFileName {
    private static final String STR = "__";
    private static final FastDateFormat FORMATTER_PRE_ddMMyyyyHHmmss = FastDateFormat.getInstance("yyyyMMdd_HHmmss" + STR);
    private static final FastDateFormat FORMATTER_PRE_ddMMyyyyHHmm = FastDateFormat.getInstance("yyyyMMdd_HHmm" + STR);
    private static final FastDateFormat FORMATTER_PRE_ddMMyyyy = FastDateFormat.getInstance("yyyyMMdd" + STR);
    private static final FastDateFormat FORMATTER_ddMMyyyyHHmmss = FastDateFormat.getInstance(STR + "yyyyMMdd_HHmmss");
    private static final FastDateFormat FORMATTER_ddMMyyyyHHmm = FastDateFormat.getInstance(STR + "yyyyMMdd_HHmm");
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

    public static String getNextFileNameWithDate(String name, String suffix, boolean second) {
        if (name.isEmpty()) {
            String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
            name = System.getProperty("user.home");
            name = PFileUtils.addsPath(name, "Infos" + dotSuffix);
        }

        String onlyName = FilenameUtils.getName(name);
        String onlyPath = FilenameUtils.getFullPath(name);
        return getNextFileNameWithDate(onlyPath, onlyName, suffix, second);
    }

    public static String getNextFileNameWithDate(String onlyPath, String onlyFileName, String suffix, boolean second) {
        if (onlyPath == null || onlyFileName == null || suffix == null) {
            return "";
        }

        String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
        onlyFileName = PFileUtils.getFileName(onlyFileName);//erst mal vom evtl. Pfad reinigen
        String filenameNoSuffix = cleanName(onlyFileName, dotSuffix);
        String ret;

        final String date1_pre = FORMATTER_PRE_ddMMyyyy.format(new Date());
        final String date2_pre;
        if (second) {
            date2_pre = FORMATTER_PRE_ddMMyyyyHHmmss.format(new Date());
        } else {
            date2_pre = FORMATTER_PRE_ddMMyyyyHHmm.format(new Date());
        }
        final String date1 = FORMATTER_ddMMyyyy.format(new Date());
        final String date2;
        if (second) {
            date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date());
        } else {
            date2 = FORMATTER_ddMMyyyyHHmm.format(new Date());
        }

        final String containDatePre1 = getDateString(filenameNoSuffix, FORMATTER_PRE_ddMMyyyy);
        final String containDatePre2;
        if (second) {
            containDatePre2 = getDateString(filenameNoSuffix, FORMATTER_PRE_ddMMyyyyHHmmss);
        } else {
            containDatePre2 = getDateString(filenameNoSuffix, FORMATTER_PRE_ddMMyyyyHHmm);
        }
        final String containDate1 = getDateString(filenameNoSuffix, FORMATTER_ddMMyyyy);
        final String containDate2;
        if (second) {
            containDate2 = getDateString(filenameNoSuffix, FORMATTER_ddMMyyyyHHmmss);
        } else {
            containDate2 = getDateString(filenameNoSuffix, FORMATTER_ddMMyyyyHHmm);
        }

        if (!onlyFileName.endsWith(dotSuffix)) {
            //dann erst mal damit
            ret = PFileUtils.addsPath(onlyPath, filenameNoSuffix + dotSuffix);
            return ret;
        }

        if (!containDatePre1.isEmpty()) {
            ret = filenameNoSuffix.replace(containDatePre1, "");
            ret = ret + date1;

        } else if (!containDate1.isEmpty()) {
            ret = filenameNoSuffix.replace(containDate1, "");
            ret = date2_pre + ret;

        } else if (!containDatePre2.isEmpty()) {
            ret = filenameNoSuffix.replace(containDatePre2, "");
            ret = ret + date2;

        } else if (!containDate2.isEmpty()) {
            ret = filenameNoSuffix.replace(containDate2, "");

        } else {
            ret = date1_pre + filenameNoSuffix;
        }

        ret = ret + dotSuffix;
        ret = PFileUtils.addsPath(onlyPath, ret);
        return ret;
    }

    public static String getNextFileNameWithDateWithOutPath(String onlyFileName, String suffix) {
        if (onlyFileName == null || suffix == null) {
            return "";
        }

        String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
        onlyFileName = PFileUtils.getFileName(onlyFileName);//erst mal vom evtl. Pfad reinigen
        String filenameNoSuffix = cleanName(onlyFileName, dotSuffix);
        String ret;

        final String date1_pre = FORMATTER_PRE_ddMMyyyy.format(new Date());
        final String date2_pre = FORMATTER_PRE_ddMMyyyyHHmmss.format(new Date());
        final String date1 = FORMATTER_ddMMyyyy.format(new Date());
        final String date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date());

        final String containDatePre1 = getDateString(filenameNoSuffix, FORMATTER_PRE_ddMMyyyy);
        final String containDatePre2 = getDateString(filenameNoSuffix, FORMATTER_PRE_ddMMyyyyHHmmss);
        final String containDate1 = getDateString(filenameNoSuffix, FORMATTER_ddMMyyyy);
        final String containDate2 = getDateString(filenameNoSuffix, FORMATTER_ddMMyyyyHHmmss);

        if (!onlyFileName.endsWith(dotSuffix)) {
            //dann erst mal damit
            ret = filenameNoSuffix + dotSuffix;
            return ret;
        }

        if (!containDatePre1.isEmpty()) {
            ret = filenameNoSuffix.replace(containDatePre1, "");
            ret = ret + date1;

        } else if (!containDate1.isEmpty()) {
            ret = filenameNoSuffix.replace(containDate1, "");
            ret = date2_pre + ret;

        } else if (!containDatePre2.isEmpty()) {
            ret = filenameNoSuffix.replace(containDatePre2, "");
            ret = ret + date2;

        } else if (!containDate2.isEmpty()) {
            ret = filenameNoSuffix.replace(containDate2, "");

        } else {
            ret = date1_pre + filenameNoSuffix;
        }

        ret = ret + dotSuffix;
        return ret;
    }

    private static String removeCounter(String name) {
        // __20181215 --> Datum darf nicht gelöscht werden
        return name.replaceAll(STR + "[0-9]{1,5}\\.", ".");
    }

    private static String cleanName(String name, String suffix) {
        // alle "suffix" am Ende entfernen
        return FilenameUtils.removeExtension(name);
    }

    private static String getDateString(String name, FastDateFormat format) {
        // Datumstring aus "name" extrahieren
        String ret = "";
        Date d = null;

        //gar nicht enthalten
        if (!name.contains(STR)) {
            return "";
        }

        //am Ende
        try {
            ret = name.substring(name.lastIndexOf(STR));
            d = new Date(format.parse(ret).getTime());
        } catch (Exception ignore) {
            d = null;
        }
        if (d != null && format.getPattern().length() == ret.length()) {
            return ret;
        }

        //dann vielleicht am Anfang
        try {
            ret = name.substring(0, name.indexOf(STR) + STR.length());
            d = new Date(format.parse(ret).getTime());
        } catch (Exception ignore) {
            d = null;
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
            if (rel.startsWith(File.separator)) {
                rel = rel.replaceFirst(File.separator, "");
            }
        }

        return rel;
    }
}
