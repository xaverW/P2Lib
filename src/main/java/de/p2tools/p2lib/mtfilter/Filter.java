/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.mtfilter;

import java.util.regex.Pattern;

public class Filter {
    public static String FILTER_REG_EX = "#:";
    public static String FILTER_EXCLUDE = "!:";

    public String filter = "";
    public String[] filterArr = {""};
    public boolean isFilterAnd = false;
    public boolean isExact = false;
    public Pattern pattern = null;
    public boolean isEmpty = true;//wenn er wirklich leer ist
    public boolean isQuick = false;//wenn er als "Quick" laufen kann
    public boolean exclude = false;

    public Filter() {
    }

    public Filter(String filter, boolean makeArr) {
        this.filter = filter.trim();
        this.filterArr = new String[]{this.filter};
        if (makeArr) {
            //Sender, Thema, Titel, ..
            makeFilterArray();
        } else {
            //Url, Datum
            makeFilter();
        }
    }

    public Filter(String filter, boolean isExact, boolean makeArr) {
        this.filter = filter.trim();
        this.filterArr = new String[]{this.filter};
        this.isExact = isExact;
        if (makeArr) {
            makeFilterArray();
        } else {
            makeFilter();
        }
    }

    public void makeFilterArray() {
        if (filter.isEmpty()) {
            filterArr = new String[]{""};
            pattern = null;
            isEmpty = true;
            return;
        }

        setValues();
        if (pattern != null) {
            filterArr = new String[]{filter};

        } else if (isExact) {
            //dann gibts nur einen Filtereintrag
            filterArr = new String[]{filter.trim().toLowerCase()};
            isQuick = true;

        } else {
            if (filter.trim().startsWith("\"") &&
                    filter.trim().endsWith("\"") &&
                    filter.length() > 1 /*sonst meldet er auch schon " */) {
                filterArr = new String[]{filter.substring(1, filter.length() - 1)};
                //System.out.println(filterArr[0]);

            } else if (filter.contains(":")) {
                isFilterAnd = true;
                filterArr = filter.split(":");
            } else {
                isFilterAnd = false;
                filterArr = filter.split(",");
            }

            for (int i = 0; i < filterArr.length; ++i) {
                filterArr[i] = filterArr[i].trim().toLowerCase();
            }
            if (filterArr.length == 1) {
                //dann gibts nur einen Filtereintrag
                isQuick = true;
            }
        }

        checkArray();
    }

    public void makeFilter() {
        // keine Auftrennung mit ":" oder "," für z.B. URLs
        if (filter.isEmpty()) {
            filterArr = new String[]{""};
            pattern = null;
            isEmpty = true;
            return;
        }

        setValues();
        if (pattern != null) {
            filterArr = new String[]{filter};

        } else {
            //dann gibts nur einen Filtereintrag, egal ob exact
            filterArr = new String[]{filter.trim().toLowerCase()};
            isQuick = true;
        }

        checkArray();
    }

    private void setValues() {
        isEmpty = false;
        isQuick = false;
        pattern = makePattern(filter);
        exclude = isExclusion(filter);
        if (exclude) {
            filter = filter.substring(FILTER_EXCLUDE.length());
        }
    }

    private void checkArray() {
        if (filterArr == null || filterArr.length == 0) {
            filterArr = new String[]{""};
            pattern = null;
            isEmpty = true;
        }
    }

    public static Pattern makePattern(String filter) {
        Pattern p = null;
        try {
            if (isPattern(filter)) {
                p = Pattern.compile(filter.substring(FILTER_REG_EX.length()),
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
            }
        } catch (final Exception ex) {
            p = null;
        }
        return p;
    }

    public static boolean isPattern(String searchText) {
        return searchText.startsWith(FILTER_REG_EX);
    }

    public static boolean isExclusion(String searchText) {
        return searchText.startsWith(FILTER_EXCLUDE);
    }
}

