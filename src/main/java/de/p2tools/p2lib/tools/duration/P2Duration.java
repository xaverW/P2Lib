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

import de.p2tools.p2lib.tools.P2StringUtils;
import de.p2tools.p2lib.tools.log.P2Log;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class P2Duration {

    private static Instant onlyPingTime = Instant.now();
    private static final DecimalFormat DF = new DecimalFormat("###,##0.00");
    private static int sum = 0;
    private static final Map<String, P2Counter> counterMap = new HashMap<>(25);
    private static final String DURATION = "DURATION";
    private static final String PING = "PING";

    public static synchronized void counterStart(String counterName) {
        P2Counter p2Counter = getCounterEntry(getName(counterName));
        //restart a previous used or start a new counter
        p2Counter.startCounter();
    }

    public static synchronized List<String> counterStop(String counterName) {
        P2Counter p2Counter = getCounterEntry(getName(counterName));

        p2Counter.count++;
        Duration duration = Duration.between(p2Counter.startTime, Instant.now());
        p2Counter.duration = p2Counter.duration.plus(duration);

        List<String> txt = new ArrayList<>();
        txt.add("       Anzahl:   " + p2Counter.count);
        txt.add("        Dauer:   " + roundDuration(duration));
        txt.add("            Ø:   " + roundDuration(p2Counter.getAverage()));
        txt.add("  Gesamtdauer:   " + roundDuration(p2Counter.duration));

        onlyPing(getClassName(), DURATION, counterName, txt, p2Counter.pingTextList);
        return txt;
    }

    private static P2Counter getCounterEntry(String counterName) {
        P2Counter p2Counter = counterMap.get(counterName);
        if (p2Counter == null) {
            //start a new counter with name: "counterName"
            p2Counter = new P2Counter(counterName);
            counterMap.put(counterName, p2Counter);
        }
        return p2Counter;
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

        P2StringUtils.appendString(list, "|  ", "-");
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
        List<P2Counter> P2CounterList = new ArrayList<>(counterMap.values());


        //die Namen auf gleiche länge bringen
        for (final P2Counter P2Counter : P2CounterList) {
            if (P2Counter.counterName.length() > max) {
                max = P2Counter.counterName.length();
            }
        }

        max++;
        for (final P2Counter P2Counter : P2CounterList) {
            while (P2Counter.counterName.length() < max) {
                P2Counter.counterName = P2Counter.counterName + " ";
            }
        }


        //die Counter ausgeben
        Collections.sort(P2CounterList);
        for (final P2Counter p2Counter : P2CounterList) {
            stringList.add(p2Counter.counterName
                    + " Anzahl: " + P2StringUtils.increaseString(2, true, p2Counter.count + "")
                    + "   ∑: " + P2StringUtils.increaseString(10, true, roundDuration(p2Counter.duration))
                    + "   Ø: " + P2StringUtils.increaseString(10, true, roundDuration(p2Counter.getAverage())));
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
