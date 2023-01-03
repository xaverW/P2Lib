/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib;

import org.apache.commons.lang3.time.FastDateFormat;

import java.time.format.DateTimeFormatter;

public class P2Factory {
    private P2Factory() {
    }

    //___ = []
    //__ = :
    //_ = .

    public static final FastDateFormat F_FORMAT_dd_MM_yyyy = FastDateFormat.getInstance("dd.MM.yyyy");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat F_FORMAT_HH__mm__ss = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat F_FORMAT_dd_MM_yyyy___HH_mm_ss = FastDateFormat.getInstance("dd.MM.yyyy HH.mm.ss");
    public static final FastDateFormat F_FORMAT_yyyy_MM_dd___HH_mm_ss = FastDateFormat.getInstance("yyyy.MM.dd HH.mm.ss");

    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter DT_FORMATTER_dd_MM_yyyy___HH_mm_ss = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
    public static final DateTimeFormatter DT_FORMATTER_yyyy_MM_dd___HH_mm_ss = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");
    public static final DateTimeFormatter DT_FORMATTER_yyyy = DateTimeFormatter.ofPattern("yyyy");
}
