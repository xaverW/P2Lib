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

import de.p2tools.p2Lib.tools.PStringUtils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class PDuration {

    private static Instant onlyPingTime = Instant.now();
    private static final DecimalFormat DF = new DecimalFormat("###,##0.00");
    private static int sum = 0;
    private static final ArrayList<Counter> COUNTER_LIST = new ArrayList<>();

    private static class Counter {

        String counterName;
        List<String> pingText = new ArrayList<>();
        int count;
        Duration duration = Duration.ZERO;
        Instant pingTime;
        Instant startTime;

        Counter(String counterName, int count) {
            this.counterName = counterName;
            this.count = count;
            startCounter();
        }

        void startCounter() {
            pingTime = Instant.now();
            startTime = Instant.now();
        }

        void startPingTime() {
            pingTime = Instant.now();
        }
    }

    public static synchronized void counterStart(String text) {
        Counter usedCounter = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.counterName.equals(text)) {
                usedCounter = c;
                break;
            }
        }
        if (usedCounter == null) {
            COUNTER_LIST.add(new Counter(text, 0));
        } else {
            usedCounter.pingText.clear();
            usedCounter.startCounter();
        }
    }

    public static synchronized void counterStop(String text) {
        String extra = "";
        Counter usedCounter = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.counterName.equals(text)) {
                usedCounter = c;
                break;
            }
        }
        if (usedCounter == null) {
            return;
        }

        usedCounter.count++;
        try {
            Duration duration = Duration.between(usedCounter.startTime, Instant.now());
            usedCounter.duration = usedCounter.duration.plus(duration);
            extra = usedCounter.counterName + " Anzahl: " + usedCounter.count + "   Dauer: " + roundDuration(usedCounter.duration);

//                final long time = Math.round(new Date().getTime() - usedCounter.startTime.getTime());
//                usedCounter.time += time;
//                extra = usedCounter.counterName + " Anzahl: " + usedCounter.count + "   Dauer: " + roundDuration(time);
        } catch (final Exception ex) {
        }
        onlyPing(getClassName(), text, extra, usedCounter.pingText);
    }

    public static synchronized void counterPing(String text) {
        Counter usedCounter = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.counterName.equals(text)) {
                usedCounter = c;
                break;
            }
        }
        if (usedCounter == null) {
            return;
        }

        try {
            Duration duration = Duration.between(usedCounter.pingTime, Instant.now());
            usedCounter.startPingTime();
            String extra = "  --> Ping Dauer: " + roundDuration(duration);
            usedCounter.pingText.add(extra);

//                final long time = Math.round(new Date().getTime() - usedCounter.pingTime.getTime());
//                usedCounter.startPingTime();
//                String extra = "  --> Ping Dauer: " + roundDuration(time);
//                usedCounter.pingText.add(extra);
        } catch (final Exception ex) {
        }
    }

    public synchronized static void onlyPing(String text) {
        onlyPing(getClassName(), text, "", null);
    }

    private static void onlyPing(String className, String text, String extra, List<String> pingText) {
        final Instant now = Instant.now();
//        long second;
//        try {
//            second = Duration.between(now, stopTimeStatic).toMillis();
//        } catch (final Exception ex) {
//            second = -1;
//        }

        ArrayList<String> list = new ArrayList<>();
        list.add(PLog.LILNE3);
        list.add("DURATION " + sum++ + ":  " + text + "  [" + roundDuration(Duration.between(onlyPingTime, now)) + "]");
        list.add("  Klasse:  " + className);
        if (pingText != null && !pingText.isEmpty()) {
            pingText.stream().forEach(s -> list.add(s));
        }
        if (!extra.isEmpty()) {
            list.add("  " + extra);
        }
        list.add(PLog.LILNE3);

        PStringUtils.appendString(list, "|  ", "-");
        PLog.durationLog(list);

        onlyPingTime = now;
    }

    public static synchronized ArrayList<String> getCounter() {
        ArrayList<String> list = new ArrayList<>();
        if (COUNTER_LIST.isEmpty()) {
            list.add("keine Counter");
            return list;
        }

        int max = 0;
        list.add("Counter:");
        list.add("============");

        for (final Counter counter : COUNTER_LIST) {
            if (counter.counterName.length() > max) {
                max = counter.counterName.length();
            }
        }
        max++;
        for (final Counter c : COUNTER_LIST) {
            while (c.counterName.length() < max) {
                c.counterName = c.counterName + " ";
            }
        }

        for (final Counter counter : COUNTER_LIST) {
            list.add("  " + counter.counterName + " Anzahl: " + counter.count + "   Gesamtdauer: " + roundDuration(counter.duration));
        }

        return list;
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
        long millis = duration.toMillis();
        String ret;
        if (millis > 1_000.0) {
            ret = DF.format(millis / 1_000.0) + " s";
        } else {
            ret = DF.format(millis) + " ms";
        }

        return ret;
    }

}
