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

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class P2FileName {
    private static final String STR = "__";

    private static final DateTimeFormatter FORMATTER_PRE_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd" + STR);
    private static final DateTimeFormatter FORMATTER_PRE_DOT_yyyyMMdd = DateTimeFormatter.ofPattern("yyyy.MM.dd" + STR);
    private static final DateTimeFormatter FORMATTER_PRE_yyyyMMdd_HHmm = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm" + STR);
    private static final DateTimeFormatter FORMATTER_PRE_DOT_yyyyMMdd_HHmm = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm" + STR);
    private static final DateTimeFormatter FORMATTER_PRE_yyyyMMdd_HHmmss = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss" + STR);
    private static final DateTimeFormatter FORMATTER_PRE_DOT_yyyyMMdd_HHmmss = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm:ss" + STR);

    private static final DateTimeFormatter FORMATTER_AFTER_yyyyMMdd = DateTimeFormatter.ofPattern(STR + "yyyyMMdd");
    private static final DateTimeFormatter FORMATTER_AFTER_DOT_yyyyMMdd = DateTimeFormatter.ofPattern(STR + "yyyy.MM.dd");
    private static final DateTimeFormatter FORMATTER_AFTER_yyyyMMdd_HHmm = DateTimeFormatter.ofPattern(STR + "yyyyMMdd_HHmm");
    private static final DateTimeFormatter FORMATTER_AFTER_DOT_yyyyMMdd_HHmm = DateTimeFormatter.ofPattern(STR + "yyyy.MM.dd_HH:mm");
    private static final DateTimeFormatter FORMATTER_AFTER_yyyyMMdd_HHmmss = DateTimeFormatter.ofPattern(STR + "yyyyMMdd_HHmmss");
    private static final DateTimeFormatter FORMATTER_AFTER_DOT_yyyyMMdd_HHmmss = DateTimeFormatter.ofPattern(STR + "yyyy.MM.dd_HH:mm:ss");

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

//    public static String getNextFileNameWithNo(String path, String selName, String suff) {
//        String ret;
//
//        Path dir = Paths.get(path);
//        if (!Files.exists(dir)) {
//            return selName;
//        }
//
//        if (suff.startsWith(".")) {
//            suff = suff.replaceFirst(".", "");
//        }
//
//        String name = selName + "." + suff;
//        Path baseDirectoryPath = Paths.get(path, name);
//        int no = 1;
//
//        while (Files.exists(baseDirectoryPath)) {
//            name = selName + STR + no + "." + suff;
//            baseDirectoryPath = Paths.get(path, name);
//            ++no;
//        }
//
//        ret = baseDirectoryPath.getFileName().toString();
//        return ret;
//    }

    public static String getNextFileNameWithDate(String name, String suffix) {
        if (name.isEmpty()) {
            String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
            name = System.getProperty("user.home");
            name = P2FileUtils.addsPath(name, "Infos" + dotSuffix);
        }

        String onlyName = FilenameUtils.getName(name);
        String onlyPath = FilenameUtils.getFullPath(name);
        return getNextFileNameWithDate(onlyPath, onlyName, suffix);
    }

    public static String getNextFileNameWithDate(String onlyPath, String onlyFileName, String suffix) {
        if (onlyPath == null || onlyFileName == null || suffix == null) {
            return "";
        }

//        String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
//        onlyFileName = P2FileUtils.getFileName(onlyFileName);//erst mal vom evtl. Pfad reinigen
//        String filenameNoSuffix = cleanName(onlyFileName, dotSuffix);
//        String ret;
//
//        final String date1_pre = FORMATTER_PRE_yyyyMMdd.format(LocalDate.now());
//        final String date2_pre;
//        if (second) {
//            date2_pre = FORMATTER_PRE_yyyyMMdd_HHmmss.format(LocalDate.now());
//        } else {
//            date2_pre = FORMATTER_PRE_yyyyMMdd_HHmm.format(LocalDate.now());
//        }
//        final String date1 = FORMATTER_AFTER_yyyyMMdd.format(LocalDate.now());
//        final String date2;
//        if (second) {
//            date2 = FORMATTER_AFTER_yyyyMMdd_HHmmss.format(LocalDate.now());
//        } else {
//            date2 = FORMATTER_AFTER_yyyyMMdd_HHmm.format(LocalDate.now());
//        }
//
//        final String containDatePre1 = getDateString(filenameNoSuffix, FORMATTER_PRE_yyyyMMdd);
//        final String containDatePre2;
//        if (second) {
//            containDatePre2 = getDateString(filenameNoSuffix, FORMATTER_PRE_yyyyMMdd_HHmmss);
//        } else {
//            containDatePre2 = getDateString(filenameNoSuffix, FORMATTER_PRE_yyyyMMdd_HHmm);
//        }
//        final String containDate1 = getDateString(filenameNoSuffix, FORMATTER_AFTER_yyyyMMdd);
//        final String containDate2;
//        if (second) {
//            containDate2 = getDateString(filenameNoSuffix, FORMATTER_AFTER_yyyyMMdd_HHmmss);
//        } else {
//            containDate2 = getDateString(filenameNoSuffix, FORMATTER_AFTER_yyyyMMdd_HHmm);
//        }
//
//        if (!onlyFileName.endsWith(dotSuffix)) {
//            //dann erst mal damit
//            ret = P2FileUtils.addsPath(onlyPath, filenameNoSuffix + dotSuffix);
//            return ret;
//        }
//
//        if (!containDatePre1.isEmpty()) {
//            ret = filenameNoSuffix.replace(containDatePre1, "");
//            ret = ret + date1;
//
//        } else if (!containDate1.isEmpty()) {
//            ret = filenameNoSuffix.replace(containDate1, "");
//            ret = date2_pre + ret;
//
//        } else if (!containDatePre2.isEmpty()) {
//            ret = filenameNoSuffix.replace(containDatePre2, "");
//            ret = ret + date2;
//
//        } else if (!containDate2.isEmpty()) {
//            ret = filenameNoSuffix.replace(containDate2, "");
//
//        } else {
//            ret = date1_pre + filenameNoSuffix;
//        }
//        ret = ret + dotSuffix;
//        ret = P2FileUtils.addsPath(onlyPath, ret);

        String fileName = getNextFileNameWithDateWithOutPath(onlyFileName, suffix, false);
        fileName = P2FileUtils.addsPath(onlyPath, fileName);
        return fileName;
    }

    public static String getNextFileNameWithDateWithOutPath(String onlyFileName, String suffix) {
        return getNextFileNameWithDateWithOutPath(onlyFileName, suffix, false);
    }

    public static String getNextFileNameWithDateWithOutPath(String onlyFileName, String suffix, boolean yesterday) {
        if (onlyFileName == null || suffix == null) {
            return "";
        }

        String dotSuffix = suffix.startsWith(".") ? suffix : "." + suffix;
        onlyFileName = P2FileUtils.getFileName(onlyFileName);//erst mal vom evtl. Pfad reinigen
        String filenameNoSuffix = cleanName(onlyFileName, dotSuffix);
        String ret;

        LocalDateTime date = yesterday ? LocalDateTime.now().minusDays(1) : LocalDateTime.now();

        final String datePre = FORMATTER_PRE_yyyyMMdd.format(date);
        final String dateDotPre = FORMATTER_PRE_DOT_yyyyMMdd.format(date);
        final String dateTimePre = FORMATTER_PRE_yyyyMMdd_HHmmss.format(date);
        final String dateTimeDotPre = FORMATTER_PRE_DOT_yyyyMMdd_HHmmss.format(date);

        final String dateAfter = FORMATTER_AFTER_yyyyMMdd.format(date);
        final String dateDotAfter = FORMATTER_AFTER_DOT_yyyyMMdd.format(date);
        final String dateTimeAfter = FORMATTER_AFTER_yyyyMMdd_HHmmss.format(date);
        final String dateTimeDotAfter = FORMATTER_AFTER_DOT_yyyyMMdd_HHmmss.format(date);

        final String containDatePre = getDateString(filenameNoSuffix, FORMATTER_PRE_yyyyMMdd);
        final String containDateDotPre = getDateString(filenameNoSuffix, FORMATTER_PRE_DOT_yyyyMMdd);
        final String containDateTimePre = getDateString(filenameNoSuffix, FORMATTER_PRE_yyyyMMdd_HHmmss);
        final String containDateTimeDotPre = getDateString(filenameNoSuffix, FORMATTER_PRE_DOT_yyyyMMdd_HHmmss);

        final String containDateAfter = getDateString(filenameNoSuffix, FORMATTER_AFTER_yyyyMMdd);
        final String containDateDotAfter = getDateString(filenameNoSuffix, FORMATTER_AFTER_DOT_yyyyMMdd);
        final String containDateTimeAfter = getDateString(filenameNoSuffix, FORMATTER_AFTER_yyyyMMdd_HHmmss);
        final String containDateTimeDotAfter = getDateString(filenameNoSuffix, FORMATTER_AFTER_DOT_yyyyMMdd_HHmmss);

        if (!onlyFileName.endsWith(dotSuffix)) {
            //dann erst mal damit
            ret = filenameNoSuffix + dotSuffix;
            return ret;
        }

        if (!containDatePre.isEmpty()) {
            ret = dateDotPre + filenameNoSuffix.replace(containDatePre, "");

        } else if (!containDateDotPre.isEmpty()) {
            ret = dateTimePre + filenameNoSuffix.replace(containDateDotPre, "");

        } else if (!containDateTimePre.isEmpty()) {
            ret = dateTimeDotPre + filenameNoSuffix.replace(containDateTimePre, "");

        } else if (!containDateTimeDotPre.isEmpty()) {
            ret = filenameNoSuffix.replace(containDateTimeDotPre, "") + dateAfter;

        } else if (!containDateAfter.isEmpty()) {
            ret = filenameNoSuffix.replace(containDateAfter, "") + dateDotAfter;

        } else if (!containDateDotAfter.isEmpty()) {
            ret = filenameNoSuffix.replace(containDateDotAfter, "") + dateTimeAfter;

        } else if (!containDateTimeAfter.isEmpty()) {
            ret = filenameNoSuffix.replace(containDateTimeAfter, "") + dateTimeDotAfter;

        } else if (!containDateTimeDotAfter.isEmpty()) {
            ret = datePre + filenameNoSuffix.replace(containDateTimeDotAfter, "");

        } else {
            ret = datePre + filenameNoSuffix;
        }

        ret = ret + dotSuffix;
        return ret;
    }

    private static String cleanName(String name, String suffix) {
        // alle "suffix" am Ende entfernen
        return FilenameUtils.removeExtension(name);
    }

    private static String getDateString(String fileName, DateTimeFormatter dateTimeFormatter) {
        // Datumstring aus "name" extrahieren, Datum/Sekunden können sich ja geändert haben

        if (!fileName.contains(STR)) {
            //gar nicht enthalten
            return "";
        }

        //am Ende
        try {
            String ret = fileName.substring(fileName.lastIndexOf(STR));
            LocalDate localDate = LocalDate.parse(ret, dateTimeFormatter);
            return ret;
        } catch (Exception ignore) {
        }

        //dann vielleicht am Anfang
        try {
            String ret = fileName.substring(0, fileName.indexOf(STR) + STR.length());
            LocalDate localDate = LocalDate.parse(ret, dateTimeFormatter);
            return ret;
        } catch (Exception ignore) {
        }

        return "";
    }
}
