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

package de.p2tools.p2lib.tools.duration;

import de.p2tools.p2lib.tools.PStringUtils;
import de.p2tools.p2lib.tools.log.P2Log;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class PDuration {

    private static Instant onlyPingTime = Instant.now();
    private static final DecimalFormat DF = new DecimalFormat("###,##0.00");
    private static int sum = 0;
    private static final Map<String, PCounter> counterMap = new HashMap<>(25);
    private static final String DURATION = "DURATION";
    private static final String PING = "PING";

    public static synchronized void counterStart(String counterName) {
        PCounter pCounter = getCounterEntry(getName(counterName));
        //restart a previous used or start a new counter
        pCounter.startCounter();
    }

    public static synchronized List<String> counterStop(String counterName) {
        PCounter pCounter = getCounterEntry(getName(counterName));

        pCounter.count++;
        Duration duration = Duration.between(pCounter.startTime, Instant.now());
        pCounter.duration = pCounter.duration.plus(duration);

        List<String> txt = new ArrayList<>();
        txt.add("       Anzahl:   " + pCounter.count);
        txt.add("        Dauer:   " + roundDuration(duration));
        txt.add("            Ø:   " + roundDuration(pCounter.getAverage()));
        txt.add("  Gesamtdauer:   " + roundDuration(pCounter.duration));

        onlyPing(getClassName(), DURATION, counterName, txt, pCounter.pingTextList);
        return txt;
    }

    private static PCounter getCounterEntry(String counterName) {
        PCounter pCounter = counterMap.get(counterName);
        if (pCounter == null) {
            //start a new counter with name: "counterName"
            pCounter = new PCounter(counterName);
            counterMap.put(counterName, pCounter);
        }
        return pCounter;
    }

    private static String getName(String counterName) {
        return getCallerClass() + "." + counterName;
    }

    private static String getCallerClass() {
        String s = "";
        try {
            StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
            String rawFQN = stElements[4].toString().split("\\(")[0];
            s = Class.forName(rawFQN.substring(0, rawFQN.lastIndexOf('.'))).getSimpleName();
        } catch (ClassNotFoundException ex) {
        }
        return s;
    }

//    public static synchronized void counterStopAndLog(String counterName) {
//        // stimmt "getCallerClass" nicht!!!!!!!!!!!!!
//        List<String> txt = counterStop(counterName);
//        txt.add(0, counterName);
//        txt.add(0, "==============================================================");
//        txt.add("==============================================================");
//        PLog.sysLog(txt.toArray(new String[txt.size()]));
//    }

    public synchronized static void onlyPing(String text) {
        onlyPing(getClassName(), PING, text, null, null);
    }

    private static void onlyPing(String className, String kind, String text, List<String> extraText, List<String> pingText) {
        final Instant now = Instant.now();

        ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add(kind + " " + sum++ + ":  " + text + "  [" + roundDuration(Duration.between(onlyPingTime, now)) + "]");
        list.add("Klasse:  " + className);

        if (pingText != null && !pingText.isEmpty()) {
            pingText.stream().forEach(s -> list.add(s));
        }

        if (extraText != null && !extraText.isEmpty()) {
            extraText.stream().forEach(txt -> list.add("  " + txt));
        }

        list.add(P2Log.LILNE3);

        PStringUtils.appendString(list, "|  ", "-");
        P2Log.durationLog(list);

        onlyPingTime = now;
    }

    public static synchronized ArrayList<String> getCounter() {
        ArrayList<String> stringList = new ArrayList<>();

        if (counterMap.isEmpty()) {
            stringList.add("keine Counter");
            return stringList;
        }


        int max = 0;
        stringList.add("Counter:");
        stringList.add("============");
        List<PCounter> PCounterList = new ArrayList<>(counterMap.values());


        //die Namen auf gleiche länge bringen
        for (final PCounter PCounter : PCounterList) {
            if (PCounter.counterName.length() > max) {
                max = PCounter.counterName.length();
            }
        }

        max++;
        for (final PCounter PCounter : PCounterList) {
            while (PCounter.counterName.length() < max) {
                PCounter.counterName = PCounter.counterName + " ";
            }
        }


        //die Counter ausgeben
        Collections.sort(PCounterList);
        for (final PCounter pCounter : PCounterList) {
            stringList.add(pCounter.counterName
                    + " Anzahl: " + PStringUtils.increaseString(2, true, pCounter.count + "")
                    + "   ∑: " + PStringUtils.increaseString(10, true, roundDuration(pCounter.duration))
                    + "   Ø: " + PStringUtils.increaseString(10, true, roundDuration(pCounter.getAverage())));
        }

        return stringList;
    }

    private static String getClassName() {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final String className = methodCaller.getClassName() + "." + methodCaller.getMethodName();
        String kl;
        try {
            kl = className;
            while (kl.contains(".")) {
                if (Character.isUpperCase(kl.charAt(0))) {
                    break;
                } else {
                    kl = kl.substring(kl.indexOf(".") + 1);
                }
            }
        } catch (final Exception ignored) {
            kl = className;
        }
        return kl;
    }

    private static String roundDuration(Duration duration) {
        return roundDuration(duration.toMillis());
    }

    private static String roundDuration(long millis) {
        String ret;
        if (millis > 1_000.0) {
            ret = DF.format(millis / 1_000.0) + " s";
        } else {
            ret = DF.format(millis) + " ms";
        }

        return ret;
    }

}
