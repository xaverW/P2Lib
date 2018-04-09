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

    public static final String LILNE1 = "################################################################################";
    public static final String LILNE2 = "================================================================================";
    public static final String LILNE3 = "--------------------------------------------------------------------------------";
    private static String FEHLER = "Fehler(" + PConst.progName + "): ";

    private static final long TO_MEGABYTE = 1000L * 1000L;
    public static final Date startZeit = new Date(System.currentTimeMillis());
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

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

    public static void versionMsg(String progName, ArrayList<String> addInfo) {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(LILNE1);
        // Logo
        list.add("");
        list.addAll(P2Logo.LOGO_LIST);
        list.add("");

        // Startzeit
        list.add(LILNE2);
        list.add("");
        list.add("Programmstart: " + dateFormatter.format(PLog.startZeit));

        list.add("");
        list.add(LILNE2);
        list.add("");

        // Speicher
        final long totalMem = Runtime.getRuntime().totalMemory();
        list.add("totalMemory: " + totalMem / TO_MEGABYTE + " MB");
        final long maxMem = Runtime.getRuntime().maxMemory();
        list.add("maxMemory: " + maxMem / TO_MEGABYTE + " MB");
        final long freeMem = Runtime.getRuntime().freeMemory();
        list.add("freeMemory: " + freeMem / TO_MEGABYTE + " MB");

        list.add("");
        list.add(LILNE2);
        list.add("");

        // Programmversion
        list.add(progName + Functions.getProgVersionString());
        String compile = Functions.getCompileDate();
        if (!compile.isEmpty()) {
            list.add("Compiled: " + compile);
        }

        list.add("");
        list.add(LILNE2);
        list.add("");

        // BS
        list.add("Betriebssystem: " + System.getProperty("os.name"));
        list.add("Bs-Version:     " + System.getProperty("os.version"));
        list.add("Bs-Architektur: " + System.getProperty("os.arch"));

        list.add("");
        list.add(LILNE2);
        list.add("");

        // Javaversion
        list.add("Java");
        final String[] java = Functions.getJavaVersion();
        for (String ja : java) {
            list.add(ja);
        }

        list.add("");
        list.add(LILNE2);
        list.add("");

        // Zusatzinfo, extra für jedes Programm
        if (addInfo != null && !addInfo.isEmpty()) {
            list.addAll(addInfo);
        }

        list.add("");
        list.add(LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        sysLog("");
        sysLog(list);
        sysLog("");
    }

    public static void endMsg() {
        ArrayList<String> list = new ArrayList<>(25);

        list.add(LILNE1);
        list.add("");
        list.add("Programm beendet");
        list.add("=================");

        // Laufzeit ausgeben
        Date stopZeit = new Date(System.currentTimeMillis());
        int minuten;
        try {
            minuten = Math.round((stopZeit.getTime() - PLog.startZeit.getTime()) / (1000 * 60));
        } catch (Exception ex) {
            minuten = -1;
        }
        list.add("");
        list.add(LILNE2);
        list.add("  --> Beginn: " + dateFormatter.format(PLog.startZeit));
        list.add("  --> Fertig: " + dateFormatter.format(stopZeit));
        list.add("  --> Dauer[Min]: " + (minuten == 0 ? "<1" : minuten));
        list.add(LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // Fehlermeldungen
        list.add(LILNE2);
        list.addAll(printErrorMsg());
        list.add(LILNE2);
        list.add("");
        list.add("");
        list.add("");

        // jetzt noch die Durations
        list.add(LILNE2);
        list.addAll(Duration.getCounter());
        list.add(LILNE2);

        list.add("");
        list.add("");
        list.add("und Tschuess");
        list.add("");
        list.add(LILNE1);

        PStringUtils.appendString(list, "#  ", "#");

        sysLog("");
        sysLog(list);
        sysLog("");
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

    /*
    Fehlermeldungen
     */

    public static synchronized void errorLog(int fehlerNummer, Exception ex) {
        PLogger.LogSevere(addErrNr(fehlerNummer, ""), ex);
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text) {
        PLogger.LogSevere(addErrNr(fehlerNummer, text), ex);
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        log = fehlerNummer + "\n" + log;
        PLogger.LogSevere(log, ex);
    }

    public static synchronized void errorLog(int fehlerNummer, String text) {
        PLogger.LogSevere(addErrNr(fehlerNummer, text));
    }

    public static synchronized void errorLog(int fehlerNummer, String[] text) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        log = fehlerNummer + "\n" + log;
        PLogger.LogSevere(log);
    }

    /*
    empty line
     */
    public static synchronized void emptyLine() {
        PLogger.LogInfo("");
    }

    /*
    Systeminfos
     */
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

    /*
    Infos die auch der User im Tab Meldungen sieht
     */
    public static synchronized void userLog(String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }

        PLogger.LogUserMsg(log);
        SysMsg.sysMsg(text);
    }

    public static synchronized void userLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogUserMsg(log);
        SysMsg.sysMsg(list);
    }

    public static synchronized void userLog(String text) {
        PLogger.LogUserMsg(text);
        SysMsg.sysMsg(text);
    }

    /*
    Meldungen zur Zeitmessung
     */
    public static synchronized void durationLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }

        PLogger.LogDuration(log);
    }

    private static String addErrNr(int errNr, String txt) {
        return errNr + "  " + txt;
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
