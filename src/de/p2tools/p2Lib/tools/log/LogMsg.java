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


package de.p2tools.p2Lib.tools.log;

import de.p2tools.p2Lib.tools.Functions;
import de.p2tools.p2Lib.tools.PStringUtils;

import java.util.ArrayList;
import java.util.Date;

public class LogMsg {


    public static void startMsg(String progName, ArrayList<String> addInfo) {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(PLog.LILNE1);
        // Logo
        list.add("");
        list.addAll(P2Logo.LOGO_LIST);
        list.add("");

        // Startzeit
        list.add(PLog.LILNE2);
        list.add("");
        list.add("Programmstart: " + PLog.dateFormatter.format(PLog.startTime));

        list.add("");
        list.add(PLog.LILNE2);
        list.add("");

        // Speicher
        final long totalMem = Runtime.getRuntime().totalMemory();
        list.add("totalMemory: " + totalMem / PLog.TO_MEGABYTE + " MB");
        final long maxMem = Runtime.getRuntime().maxMemory();
        list.add("maxMemory: " + maxMem / PLog.TO_MEGABYTE + " MB");
        final long freeMem = Runtime.getRuntime().freeMemory();
        list.add("freeMemory: " + freeMem / PLog.TO_MEGABYTE + " MB");

        list.add("");
        list.add(PLog.LILNE2);
        list.add("");

        // Programmversion
        list.add(progName + Functions.getProgVersionString());
        String compile = Functions.getCompileDate();
        if (!compile.isEmpty()) {
            list.add("Compiled: " + compile);
        }

        list.add("");
        list.add(PLog.LILNE2);
        list.add("");

        // BS
        list.add("Betriebssystem: " + System.getProperty("os.name"));
        list.add("Bs-Version:     " + System.getProperty("os.version"));
        list.add("Bs-Architektur: " + System.getProperty("os.arch"));

        list.add("");
        list.add(PLog.LILNE2);
        list.add("");

        // Javaversion
        list.add("Java");
        final String[] java = Functions.getJavaVersion();
        for (String ja : java) {
            list.add(ja);
        }

        list.add("");
        list.add(PLog.LILNE2);
        list.add("");

        // Zusatzinfo, extra für jedes Programm
        if (addInfo != null && !addInfo.isEmpty()) {
            list.addAll(addInfo);
        }

        list.add("");
        list.add(PLog.LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        PLog.sysLog("");
        PLog.sysLog(list);
        PLog.sysLog("");
    }

    public static void endMsg() {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(PLog.LILNE1);
        list.add("");
        list.add("Programm beendet");
        list.add("=================");

        // Laufzeit ausgeben
        Date stopTime = new Date(System.currentTimeMillis());
        int minute;
        try {
            minute = Math.round((stopTime.getTime() - PLog.startTime.getTime()) / (1000 * 60));
        } catch (Exception ex) {
            minute = -1;
        }
        list.add("");
        list.add(PLog.LILNE2);
        list.add("  --> Beginn: " + PLog.dateFormatter.format(PLog.startTime));
        list.add("  --> Fertig: " + PLog.dateFormatter.format(stopTime));
        list.add("  --> Dauer[Min]: " + (minute == 0 ? "<1" : minute));
        list.add(PLog.LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // Fehlermeldungen
        list.add(PLog.LILNE2);
        list.addAll(printErrorMsg());
        list.add(PLog.LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // jetzt noch die Durations
        list.add(PLog.LILNE2);
        list.addAll(Duration.getCounter());
        list.add(PLog.LILNE2);

        list.add("");
        list.add("");
        list.add("und Tschuess");
        list.add("");
        list.add(PLog.LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        PLog.sysLog("");
        PLog.sysLog(list);
        PLog.sysLog("");
    }

    public static synchronized ArrayList<String> printErrorMsg() {
        int max = 0;
        ArrayList<String> list = new ArrayList<>();
        if (PLog.errorList.isEmpty()) {
            list.add("Keine Fehler :)");
            return list;
        }
        list.add("Fehler:");
        list.add("===========");
        // Fehler ausgeben
        int i_1;
        int i_2;
        for (PLog.Error e : PLog.errorList) {
            if (e.callClass.length() > max) {
                max = e.callClass.length();
            }
        }
        max++;
        for (PLog.Error e : PLog.errorList) {
            while (e.callClass.length() < max) {
                e.callClass = e.callClass + ' ';
            }
        }
        for (int i = 1; i < PLog.errorList.size(); ++i) {
            for (int k = i; k > 0; --k) {
                i_1 = PLog.errorList.get(k - 1).errorNr;
                i_2 = PLog.errorList.get(k).errorNr;
                // if (str1.compareToIgnoreCase(str2) > 0) {
                if (i_1 < i_2) {
                    PLog.errorList.add(k - 1, PLog.errorList.remove(k));
                } else {
                    break;
                }
            }
        }
        for (PLog.Error e : PLog.errorList) {
            String strEx;
            if (e.ex) {
                strEx = "  Ex! ";
            } else {
                strEx = "      ";
            }
            list.add(strEx + e.callClass + " Fehlernummer: " + e.errorNr + " Anzahl: " + e.count);
        }
        return list;
    }
}
