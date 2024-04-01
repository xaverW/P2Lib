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


package de.p2tools.p2lib.tools.log;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.PStringUtils;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import de.p2tools.p2lib.tools.duration.PDuration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class P2LogMessage {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static void startMsg(String progName, ArrayList<String> addInfo) {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(P2Log.LILNE1);
        // Logo
        list.add("");
        list.addAll(P2Logo.LOGO_LIST);
        list.add("");

        // Startzeit
        list.add(P2Log.LILNE2);
        list.add("");
        list.add("Programmstart: " + formatter.format(P2Log.START_TIME));

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("");

        // Speicher
        final long totalMem = Runtime.getRuntime().totalMemory();
        list.add("totalMemory: " + totalMem / P2Log.TO_MEGABYTE + " MB");
        final long maxMem = Runtime.getRuntime().maxMemory();
        list.add("maxMemory: " + maxMem / P2Log.TO_MEGABYTE + " MB");
        final long freeMem = Runtime.getRuntime().freeMemory();
        list.add("freeMemory: " + freeMem / P2Log.TO_MEGABYTE + " MB");

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("");

        // Programmversion
        list.add(progName + ProgramToolsFactory.getProgVersionString());
        String compile = ProgramToolsFactory.getCompileDate();
        if (!compile.isEmpty()) {
            list.add("Compiled: " + compile);
        }
        if (P2LibConst.debug) {
            list.add("-->  DEBUG  <--");
        }

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("");

        // BS
        list.add("Betriebssystem: " + System.getProperty("os.name"));
        list.add("Bs-Version:     " + System.getProperty("os.version"));
        list.add("Bs-Architektur: " + System.getProperty("os.arch"));

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("");

        // Javaversion
        list.add("Java");
        final String[] java = ProgramToolsFactory.getJavaVersion();
        for (String ja : java) {
            list.add(ja);
        }

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("");

        // Zusatzinfo, extra für jedes Programm
        if (addInfo != null && !addInfo.isEmpty()) {
            list.addAll(addInfo);
        }

        list.add("");
        list.add(P2Log.LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        P2Log.sysLog("");
        P2Log.sysLog(list);
        P2Log.sysLog("");
    }

    public static void endMsg() {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(P2Log.LILNE1);
        list.add("");
        list.add("Programm beendet");
        list.add("=================");

        // Laufzeit ausgeben
        Duration duration = Duration.between(P2Log.START_TIME, Instant.now());
        int minute = (int) duration.getSeconds() / 60;

        list.add("");
        list.add(P2Log.LILNE2);
        list.add("  --> Beginn: " + formatter.format(P2Log.START_TIME));
        list.add("  --> Fertig: " + formatter.format(Instant.now()));
        list.add("  --> Dauer[min]: " + (minute == 0 ? "<1" : minute));
        list.add(P2Log.LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // Fehlermeldungen
        list.add(P2Log.LILNE2);
        list.addAll(printErrorMsg());
        list.add(P2Log.LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // jetzt noch die Durations
        list.add(P2Log.LILNE2);
        list.addAll(PDuration.getCounter());
        list.add(P2Log.LILNE2);

        list.add("");
        list.add("");
        list.add("und Tschuess");
        list.add("");
        list.add(P2Log.LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        P2Log.sysLog("");
        P2Log.sysLog(list);
        P2Log.sysLog("");
    }

    public static synchronized ArrayList<String> printErrorMsg() {
        int max = 0;
        ArrayList<String> list = new ArrayList<>();
        if (P2Log.errorList.isEmpty()) {
            list.add("Keine Fehler :)");
            return list;
        }
        list.add("Fehler:");
        list.add("===========");
        // Fehler ausgeben
        int i_1;
        int i_2;
        for (P2Log.Error e : P2Log.errorList) {
            if (e.callClass.length() > max) {
                max = e.callClass.length();
            }
        }
        max++;
        for (P2Log.Error e : P2Log.errorList) {
            while (e.callClass.length() < max) {
                e.callClass = e.callClass + ' ';
            }
        }
        for (int i = 1; i < P2Log.errorList.size(); ++i) {
            for (int k = i; k > 0; --k) {
                i_1 = P2Log.errorList.get(k - 1).errorNr;
                i_2 = P2Log.errorList.get(k).errorNr;
                // if (str1.compareToIgnoreCase(str2) > 0) {
                if (i_1 < i_2) {
                    P2Log.errorList.add(k - 1, P2Log.errorList.remove(k));
                } else {
                    break;
                }
            }
        }
        for (P2Log.Error e : P2Log.errorList) {
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
