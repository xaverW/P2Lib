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


package de.p2tools.p2lib.tools.date;

import org.apache.commons.lang3.time.FastDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class P2DateConst {

    //_ = .
    //__ = :
    //___ = []

    public static final String STR__dd_MM_yyyy___HH__mm__ss = "dd.MM.yyyy HH:mm:ss";

    public static final DateTimeFormatter DT_FORMATTER__FILMLIST = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"); // "05.06.2025, 12:32"

    public static final FastDateFormat F_FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat F_FORMAT_yyyyMMdd = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat F_FORMAT_HHmmss = FastDateFormat.getInstance("HHmmss");
    public static final FastDateFormat F_FORMAT_HH__mm__ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat F_FORMAT_HHmm_ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyy___HH__mm__ss = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd___HH__mm__ss = FastDateFormat.getInstance("yyyy.MM.dd HH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyHH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyyHH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyKomma___HH_mm = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyyKomma___HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy, HH:mm:ss");
    public static final FastDateFormat F_FORMAT_yyyy = FastDateFormat.getInstance("yyyy");

//    public static final DateTimeFormatter DT_FORMATTER_d_M_yyyy = DateTimeFormatter.ofPattern("d.M.yyyy");
//    public static final DateTimeFormatter DT_FORMATTER_dd_M_yyyy = DateTimeFormatter.ofPattern("dd.M.yyyy");
//    public static final DateTimeFormatter DT_FORMATTER_d_MM_yyyy = DateTimeFormatter.ofPattern("d.MM.yyyy");
//    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//    public static final DateTimeFormatter DT_FORMATTER_dd_MM = DateTimeFormatter.ofPattern("dd.MM");
//    public static final DateTimeFormatter DT_FORMATTER_dd = DateTimeFormatter.ofPattern("dd");
//    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//    public static final DateTimeFormatter DT_FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
//    public static final DateTimeFormatter DT_FORMATTER_HH__mm__ss = DateTimeFormatter.ofPattern("HH:mm:ss");
//    public static final DateTimeFormatter DT_FORMATTER_HH__mm = DateTimeFormatter.ofPattern("HH:mm");
//    public static final DateTimeFormatter DT_FORMATTER_yyyy = DateTimeFormatter.ofPattern("yyyy");
//    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy___HH__mm = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy___HH__mm__ss = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
//    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd___HH__mm__ss = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
//    public static final DateTimeFormatter DT_FORMATTER_EEE_MMM_dd_ = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);


    // toString mit yyyy liefer bei MIN: 01.01.+1000000000
    // das Zurückparsen von yyyy wirft eine Exception: out of range
    // mit uuuu stimmts
    // https://dev.karakun.com/2020/02/21/date-format.html

    public static final DateTimeFormatter DT_FORMATTER_d_M_yyyy = DateTimeFormatter.ofPattern("d.M.uuuu");
    public static final DateTimeFormatter DT_FORMATTER_dd_M_yyyy = DateTimeFormatter.ofPattern("dd.M.uuuu");
    public static final DateTimeFormatter DT_FORMATTER_d_MM_yyyy = DateTimeFormatter.ofPattern("d.MM.uuuu");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM = DateTimeFormatter.ofPattern("dd.MM");
    public static final DateTimeFormatter DT_FORMATTER_dd = DateTimeFormatter.ofPattern("dd");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd = DateTimeFormatter.ofPattern("uuuu.MM.dd");
    public static final DateTimeFormatter DT_FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern("uuuuMMdd");
    public static final DateTimeFormatter DT_FORMATTER_HH__mm__ss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DT_FORMATTER_HH__mm = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DT_FORMATTER_yyyy = DateTimeFormatter.ofPattern("uuuu");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy___HH__mm = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy___HH__mm__ss = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd___HH__mm__ss = DateTimeFormatter.ofPattern("uuuu.MM.dd HH:mm:ss");
    public static final DateTimeFormatter DT_FORMATTER_EEE_MMM_dd_ = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu", Locale.US);


    private P2DateConst() {
    }

    // zum Testen
    public static void startTest() {
        LocalDate ld = LocalDate.now();
        print(ld);

        ld = LocalDate.of(1000, 1, 1);
        print(ld);

        ld = LocalDate.of(0, 1, 1);
        print(ld);

        ld = LocalDate.of(-1, 1, 1);
        print(ld);

        ld = LocalDate.of(2, 1, 1);
        print(ld);

        ld = LocalDate.MIN;
        print(ld);

        ld = LocalDate.MAX;
        print(ld);
    }

    private static void print(LocalDate ld) {
        try {
            System.out.println("====================");
            String s = ld.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
            System.out.println(s);
            System.out.println(LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.uuuu")));
            System.out.println("--");
            s = ld.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            System.out.println(s);
            System.out.println(LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (Exception ex) {
            System.out.println("============");
            System.out.println("   ex   ");
            System.out.println("============");
        }
    }




/*
        ====================
        23.07.2026
        2026-07-23
        --
        23.07.2026
        2026-07-23
        ====================
        01.01.1000
        1000-01-01
        --
        01.01.1000
        1000-01-01
        ====================
        01.01.0000
        0000-01-01
        --
        01.01.0001
        0001-01-01
        ====================
        01.01.-0001
        -0001-01-01
        --
        01.01.0002
        0002-01-01
        ====================
        01.01.0002
        0002-01-01
        --
        01.01.0002
        0002-01-01
        ====================
        01.01.-999999999
        -999999999-01-01
        --
        01.01.+1000000000
        ============
            ex
        ============
        ====================
        31.12.+999999999
        +999999999-12-31
        --
        31.12.+999999999
        +999999999-12-31

*/

}
