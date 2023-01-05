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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PFileSize implements Comparable<PFileSize> {
    private static final long GBYTE = 1000L * 1000L * 1000L;
    private static final long MBYTE = 1000L * 1000L;
    private static final long KBYTE = 1000L;
    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    private long sizeL = 0;
    private String sizeStr = "";

    public PFileSize(long sizeL) {
        this.sizeL = sizeL;
        convertToStr();
    }

    public PFileSize(String sizeStr) {
        try {
            sizeL = Long.parseLong(sizeStr);
        } catch (Exception ex) {
            sizeL = 0;
        }
        convertToStr();
    }

    public static String convertToStr(long sizeL) {

        if (sizeL <= 0) {
            return "";
        }

        if (sizeL < KBYTE) {
            return sizeL + " B";

        } else if (sizeL < MBYTE) {
            double size = 1.0 * sizeL / KBYTE;
            return formatter.format(size) + " kB";

        } else if (sizeL < GBYTE) {
            double size = 1.0 * sizeL / MBYTE;
            return formatter.format(size) + " MB";

        } else {
            double size = 1.0 * sizeL / GBYTE;
            return formatter.format(size) + " GB";
        }
    }

    public static long convertToLong(String size) {
        if (size.isEmpty()) {
            return 0;
        }
        size = size.replace(",", ".");
        try {
            if (size.endsWith(" B")) {
                size = size.replace(" B", "");
                return Double.valueOf(size).longValue();
            }
            if (size.endsWith(" kB")) {
                size = size.replace(" kB", "");
                return Double.valueOf(Double.valueOf(size) * KBYTE).longValue();
            }
            if (size.endsWith(" MB")) {
                size = size.replace(" MB", "");
                return Double.valueOf(Double.valueOf(size) * MBYTE).longValue();
            }
            if (size.endsWith(" GB")) {
                size = size.replace(" GB", "");
                return Double.valueOf(Double.valueOf(size) * GBYTE).longValue();
            } else {
                return Double.valueOf(size).longValue();
            }
        } catch (Exception ex) {

        }
        return 0;
    }

    private void convertToStr() {
        sizeStr = convertToStr(sizeL);
    }

    private void convertToLong() {
        sizeL = convertToLong(sizeStr);
    }


    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PFileSize)) {
            return false;
        }

        final PFileSize other = (PFileSize) obj;
        return other.sizeL == sizeL;
    }

    public long getSizeL() {
        return sizeL;
    }

    public void setSizeL(long sizeL) {
        this.sizeL = sizeL;
        convertToStr();
    }

    public void addSizeL(long sizeL) {
        this.sizeL += sizeL;
        convertToStr();
    }

//    public String getSizeStr() {
//        return sizeStr;
//    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
        convertToLong();
    }

    @Override
    public String toString() {
        return sizeStr;
    }

    @Override
    public int compareTo(PFileSize o) {
        if (sizeL < o.sizeL) {
            return -1;
        }
        if (sizeL == o.sizeL) {
            return 0;
        }
        return 1;
    }
}
