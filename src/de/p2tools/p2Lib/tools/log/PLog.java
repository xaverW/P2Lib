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

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.tools.Functions;
import de.p2tools.p2Lib.tools.PStringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class PLog {

    public static String LOGO = "\n" +
            "\n" +
            " ██████╗ ██████╗ ████████╗ ██████╗  ██████╗ ██╗     ███████╗\n" +
            " ██╔══██╗╚════██╗╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝\n" +
            " ██████╔╝ █████╔╝   ██║   ██║   ██║██║   ██║██║     ███████╗\n" +
            " ██╔═══╝ ██╔═══╝    ██║   ██║   ██║██║   ██║██║     ╚════██║\n" +
            " ██║     ███████╗   ██║   ╚██████╔╝╚██████╔╝███████╗███████║\n" +
            " ╚═╝     ╚══════╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝\n";

    public static final String LILNE1 = "################################################################################";
    public static final String LILNE2 = "================================================================================";

    private static final long TO_MEGABYTE = 1000L * 1000L;
    public static final Date startZeit = new Date(System.currentTimeMillis());
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private static String FEHLER = "Fehler(" + PConst.progName + "): ";
    private static final LinkedList<Error> fehlerListe = new LinkedList<>();
    private static final ArrayList<String> logList = new ArrayList<>();
    private static boolean progress = false;

    private static class Error {
        String callClass = "";
        int errorNr = 0;
        int count = 0;
        boolean ex = false;

        public Error(int errorNr, String callClass, boolean ex) {
            this.errorNr = errorNr;
            this.callClass = callClass;
            this.ex = ex;
            this.count = 1;
        }
    }

    public static void versionMsg(String progName) {
        ArrayList<String> arrayList = new ArrayList<>(25);

        arrayList.add(LILNE1);
        arrayList.add(LOGO);
        arrayList.add("");

        arrayList.add(LILNE2);
        arrayList.add("Programmstart: " + dateFormatter.format(PLog.startZeit));
        arrayList.add(LILNE2);
        arrayList.add("");

        final long totalMem = Runtime.getRuntime().totalMemory();
        arrayList.add("totalMemory: " + totalMem / TO_MEGABYTE + " MB");
        final long maxMem = Runtime.getRuntime().maxMemory();
        arrayList.add("maxMemory: " + maxMem / TO_MEGABYTE + " MB");
        final long freeMem = Runtime.getRuntime().freeMemory();
        arrayList.add("freeMemory: " + freeMem / TO_MEGABYTE + " MB");
        arrayList.add("");
        arrayList.add(LILNE2);
        arrayList.add("");

        //Version
        arrayList.add(progName + Functions.getProgVersionString());
        String compile = Functions.getCompileDate();
        if (!compile.isEmpty()) {
            arrayList.add("Compiled: " + compile);
        }

        arrayList.add("");
        arrayList.add(LILNE2);
        arrayList.add("");
        arrayList.add("Java");
        final String[] java = Functions.getJavaVersion();
        for (String ja : java) {
            arrayList.add(ja);
        }
        arrayList.add("");
        arrayList.add(LILNE1);
        arrayList.add("");

        sysLog("");
        sysLog("");
        sysLog(arrayList);
    }

    public static void endMsg() {
        ArrayList<String> arrayList = new ArrayList<>(25);

        arrayList.add(LILNE1);
        arrayList.add("Programm beendet");
        arrayList.add("=================");

        // Laufzeit ausgeben
        Date stopZeit = new Date(System.currentTimeMillis());
        int minuten;
        try {
            minuten = Math.round((stopZeit.getTime() - PLog.startZeit.getTime()) / (1000 * 60));
        } catch (Exception ex) {
            minuten = -1;
        }
        arrayList.add("");
        arrayList.add(LILNE2);
        arrayList.add("  --> Beginn: " + dateFormatter.format(PLog.startZeit));
        arrayList.add("  --> Fertig: " + dateFormatter.format(stopZeit));
        arrayList.add("  --> Dauer[Min]: " + (minuten == 0 ? "<1" : minuten));
        arrayList.add(LILNE2);
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        // Fehlermeldungen
        arrayList.add(LILNE2);
        arrayList.addAll(printErrorMsg());
        arrayList.add(LILNE2);
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");


        // jetzt noch die Durations
        arrayList.add(LILNE2);
        arrayList.addAll(Duration.getCounter());
        arrayList.add(LILNE2);

        arrayList.add("");
        arrayList.add("");
        arrayList.add("und Tschuess");
        arrayList.add("");
        arrayList.add(LILNE1);
        arrayList.add("");

        sysLog("");
        sysLog("");
        sysLog(arrayList);
    }

    public static synchronized ArrayList<String> printErrorMsg() {
        int max = 0;
        ArrayList<String> list = new ArrayList<>();
        if (fehlerListe.isEmpty()) {
            list.add("Keine Fehler :)");
            return list;
        }
        list.add("Fehler:");
        list.add("===========");
        // Fehler ausgeben
        int i_1;
        int i_2;
        for (Error e : fehlerListe) {
            if (e.callClass.length() > max) {
                max = e.callClass.length();
            }
        }
        max++;
        for (Error e : fehlerListe) {
            while (e.callClass.length() < max) {
                e.callClass = e.callClass + ' ';
            }
        }
        for (int i = 1; i < fehlerListe.size(); ++i) {
            for (int k = i; k > 0; --k) {
                i_1 = fehlerListe.get(k - 1).errorNr;
                i_2 = fehlerListe.get(k).errorNr;
                // if (str1.compareToIgnoreCase(str2) > 0) {
                if (i_1 < i_2) {
                    fehlerListe.add(k - 1, fehlerListe.remove(k));
                } else {
                    break;
                }
            }
        }
        for (Error e : fehlerListe) {
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

    // Fehlermeldung mit Exceptions
    public static synchronized void errorLog(int fehlerNummer, Exception ex) {
        PLogger.LogSevere(ex);
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text) {
        PLogger.LogSevere(text, ex);
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text[]) {
        PLogger.LogSevere(text.toString(), ex);
    }

    // Fehlermeldungen
    public static synchronized void errorLog(int fehlerNummer, String text) {
        PLogger.LogSevere(text);
    }

    public static synchronized void errorLog(int fehlerNummer, String[] text) {
        PLogger.LogSevere(text.toString());
    }

    public static synchronized void sysLog(String text) {
        PLogger.LogInfo(text);
    }

    public static synchronized void sysLog(String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
    }

    public static synchronized void sysLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
    }

    public static synchronized void userLog(String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);

        SysMsg.sysMsg(text);
    }

    public static synchronized void userLog(String text) {
        PLogger.LogInfo(text);
        SysMsg.sysMsg(text);
    }

    public static synchronized void durationLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }

        PLogger.LogDuration(log);
    }


    private static void addFehlerNummer(int nr, String classs, boolean exception) {
        for (Error e : fehlerListe) {
            if (e.errorNr == nr) {
                ++e.count;
                return;
            }
        }
        // dann gibts die Nummer noch nicht
        fehlerListe.add(new Error(nr, classs, exception));
    }

    private static void fehlermeldung_(int fehlerNummer, Exception ex, String[] texte) {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final String klasse = methodCaller.getClassName() + '.' + methodCaller.getMethodName();
        String kl;
        try {
            kl = klasse;
            while (kl.contains(".")) {
                if (Character.isUpperCase(kl.charAt(0))) {
                    break;
                } else {
                    kl = kl.substring(kl.indexOf('.') + 1);
                }
            }
        } catch (Exception ignored) {
            kl = klasse;
        }
        addFehlerNummer(fehlerNummer, kl, ex != null);
        if (ex != null || PConst.debug) {
            // Exceptions immer ausgeben
            resetProgress();
            String x, z;
            if (ex != null) {
                x = "!";
            } else {
                x = "=";
            }
            z = "*";
            logList.add(x + x + x + x + x + x + x + x + x + x
                    + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x);

            try {
                // Stacktrace
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                    if (ex != null) {
                        ex.printStackTrace(pw);
                    }
                    pw.flush();
                    sw.flush();
                    logList.add(sw.toString());
                }
            } catch (Exception ignored) {
            }

            logList.add(z + " Fehlernr: " + fehlerNummer);
            if (ex != null) {
                logList.add(z + " Exception: " + ex.getMessage());
            }
            logList.add(z + ' ' + FEHLER + kl);
            for (String aTexte : texte) {
                logList.add(z + "           " + aTexte);
            }
            logList.add("");
            printLog();
        }
    }

    public static synchronized void progress(String texte) {
        progress = true;
        if (!texte.isEmpty()) {
            System.out.print(texte + '\r');
        }
    }

    private static void resetProgress() {
        // Leerzeile um die Progresszeile zu löschen
        if (progress) {
            System.out.print("                                                                                                             \r");
            progress = false;
        }
    }

    private static void systemmeldung_(String[] texte) {
        resetProgress();
        final String z = ". ";
        if (texte.length <= 1) {
            logList.add(z + ' ' + texte[0]);
        } else {
            String zeile = "---------------------------------------";
            String txt;
            logList.add(z + zeile);
            for (String aTexte : texte) {
                txt = "| " + aTexte;
                logList.add(z + txt);
            }
            logList.add(z + zeile);
        }
        printLog();
    }

    private static void printLog() {
//        logList.forEach(System.out::println);
        logList.clear();
    }
}
