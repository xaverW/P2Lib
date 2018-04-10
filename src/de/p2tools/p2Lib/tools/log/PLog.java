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
import de.p2tools.p2Lib.tools.PStringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class PLog {

    public static final String LILNE1 = "################################################################################";
    public static final String LILNE2 = "================================================================================";
    public static final String LILNE3 = "--------------------------------------------------------------------------------";
    static String FEHLER = "Fehler(" + PConst.progName + "): ";

    static final long TO_MEGABYTE = 1000L * 1000L;
    public static final Date startZeit = new Date(System.currentTimeMillis());
    static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    static final LinkedList<Error> fehlerListe = new LinkedList<>();
    static final ArrayList<String> logList = new ArrayList<>();
    static boolean progress = false;

    static class Error {
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

    /*
    Fehlermeldungen
     */

    public static synchronized void errorLog(int fehlerNummer, Exception ex) {
        PLogger.LogSevere(addErrNr(fehlerNummer, ""), ex);
        fehlermeldung_(fehlerNummer, ex, new String[]{});
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text) {
        PLogger.LogSevere(addErrNr(fehlerNummer, text), ex);
        fehlermeldung_(fehlerNummer, ex, new String[]{text});
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        log = fehlerNummer + "\n" + log;
        PLogger.LogSevere(log, ex);
        fehlermeldung_(fehlerNummer, ex, text);
    }

    public static synchronized void errorLog(int fehlerNummer, String text) {
        PLogger.LogSevere(addErrNr(fehlerNummer, text));
        fehlermeldung_(fehlerNummer, null, new String[]{text});
    }

    public static synchronized void errorLog(int fehlerNummer, String[] text) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        log = fehlerNummer + "\n" + log;
        PLogger.LogSevere(log);
        fehlermeldung_(fehlerNummer, null, text);
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
        resetProgress();
    }

    public static synchronized void sysLog(String text[]) {
        String log = PStringUtils.appendArray(text, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
        resetProgress();
    }

    public static synchronized void sysLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
        resetProgress();
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
        resetProgress();
    }

    public static synchronized void userLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, "\n");
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogUserMsg(log);
        SysMsg.sysMsg(list);
        resetProgress();
    }

    public static synchronized void userLog(String text) {
        PLogger.LogUserMsg(text);
        SysMsg.sysMsg(text);
        resetProgress();
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
        resetProgress();
    }

    public static synchronized void progress(String text) {
        progress = true;
        if (!text.isEmpty()) {
            System.out.print(text + '\r');
        }
    }

    private static void resetProgress() {
        // Leerzeile um die Progresszeile zu löschen
        if (progress) {
            System.out.print("                                                                                                             \r");
            progress = false;
        }
    }


}
