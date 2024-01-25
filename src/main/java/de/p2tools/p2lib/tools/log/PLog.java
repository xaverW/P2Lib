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

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PLog {

    public static int COUNT = 0;
    public static final String LILNE1 = "############################################################";
    public static final String LILNE2 = "============================================================";
    public static final String LILNE3 = "------------------------------------------------------------";
    public static final String LILNE_EMPTY = "  ";
    public static final Instant START_TIME = Instant.now();
    static final long TO_MEGABYTE = 1000L * 1000L;
    static final LinkedList<Error> errorList = new LinkedList<>();
    static boolean progress = false;

    //Externe Tools Infos
    public static synchronized void extToolLog(String text) {
        PLogger.LogExtToolMsg(text);
        resetProgress();
    }

    //Meldungen zur Zeitmessung
    public static synchronized void durationLog(ArrayList<String> list) {
        String log = PStringUtils.appendList(list, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }

        if (P2LibConst.duration) {
            // nur dann sollen sie mit ausgegeben werden
            PLogger.LogDuration(log);
        }
    }

    //empty line
    public static synchronized void emptyLine() {
        PLogger.LogInfo("");
    }

    public static synchronized void progress(String text) {
        progress = true;
        if (!text.isEmpty()) {
            System.out.print(text + '\r');
        }
    }

    //Fehlermeldungen
    public static synchronized void errorLog(int errorNumber, Exception ex) {
        PLogger.LogSevere(addErrNr(errorNumber, ""), ex);
        error(errorNumber, ex, new String[]{});
    }

    public static synchronized void errorLog(int errorNumber, String text) {
        PLogger.LogSevere(addErrNr(errorNumber, text));
        error(errorNumber, null, new String[]{text});
    }

    public static synchronized void errorLog(int errorNumber, String[] text) {
        String log = PStringUtils.appendArray(text, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        log = errorNumber + P2LibConst.LINE_SEPARATOR + log;
        PLogger.LogSevere(log);
        error(errorNumber, null, text);
    }

    public static synchronized void errorLog(int errorNumber, Exception ex, String text) {
        PLogger.LogSevere(addErrNr(errorNumber, text), ex);
        error(errorNumber, ex, new String[]{text});
    }

    public static synchronized void errorLog(int errorNumber, Exception ex, String text[]) {
        String log = PStringUtils.appendArray(text, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        log = errorNumber + P2LibConst.LINE_SEPARATOR + log;
        PLogger.LogSevere(log, ex);
        error(errorNumber, ex, text);
    }

    //Systeminfos
    public static synchronized void sysLog(String text) {
        PLogger.LogInfo(text);
        resetProgress();
    }

    public static synchronized void sysLog(String text[]) {
        String log = PStringUtils.appendArray(text, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
        resetProgress();
    }

    public static synchronized void sysLog(List<String> list) {
        String log = PStringUtils.appendList(list, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogInfo(log);
        resetProgress();
    }

    //DebugInfos
    public static synchronized void debugLogCount(String text) {
        if (!P2LibConst.debug) {
            return;
        }
        PLogger.LogDebug(COUNT + " - " + text);
        resetProgress();
    }

    public static synchronized void debugLog(String text) {
        if (!P2LibConst.debug) {
            return;
        }
        PLogger.LogDebug(text);
        resetProgress();
    }

    public static synchronized void debugLog(String text[]) {
        if (!P2LibConst.debug) {
            return;
        }
        String log = PStringUtils.appendArray(text, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogDebug(log);
        resetProgress();
    }

    public static synchronized void debugLog(List<String> list) {
        if (!P2LibConst.debug) {
            return;
        }
        String log = PStringUtils.appendList(list, P2LibConst.LINE_SEPARATOR);
        if (log.isEmpty()) {
            return;
        }
        PLogger.LogDebug(log);
        resetProgress();
    }


    private static String addErrNr(int errNr, String txt) {
        return errNr + "  " + txt;
    }

    private static void error(int errorNumber, Exception ex, String[] texte) {
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
        countError(errorNumber, kl, ex != null);
        resetProgress();
    }

    private static void countError(int no, String classs, boolean exception) {
        for (Error e : errorList) {
            if (e.errorNr == no) {
                ++e.count;
                return;
            }
        }
        // dann gibts die Nummer noch nicht
        errorList.add(new Error(no, classs, exception));
    }

    private static void resetProgress() {
        // Leerzeile um die Progresszeile zu löschen
        if (progress) {
            System.out.print("                                                                                                             \r");
            progress = false;
        }
    }

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
}
