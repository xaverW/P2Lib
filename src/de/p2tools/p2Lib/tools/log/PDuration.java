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
        int count = 0;
        Duration duration = Duration.ZERO;
        Instant pingTime;
        Instant startTime;

        Counter(String counterName) {
            this.counterName = counterName;
            startCounter();
        }

        void startCounter() {
            pingTime = Instant.now();
            startTime = Instant.now();
        }

        void pingTime() {
            pingTime = Instant.now();
        }
    }

    public static synchronized void counterStart(String counterName) {
        Counter usedCounter = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.counterName.equals(counterName)) {
                usedCounter = c;
                break;
            }
        }

        if (usedCounter == null) {
            // start a new counter with name: "counterName"
            COUNTER_LIST.add(new Counter(counterName));

        } else {
            // restart a previous used counter an clear the selected text
            usedCounter.pingText.clear();
            usedCounter.startCounter();
        }
    }

    public static synchronized void counterStop(String text) {
        String extraText = "";
        Counter usedCounter = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.counterName.equals(text)) {
                usedCounter = c;
                break;
            }
        }

        if (usedCounter == null) {
            // should not happen
            return;
        }

        usedCounter.count++;
        Duration duration = Duration.between(usedCounter.startTime, Instant.now());
        usedCounter.duration = usedCounter.duration.plus(duration);
        List<String> txt = new ArrayList<>();
        txt.add(usedCounter.counterName);
        txt.add("Anzahl: " + usedCounter.count +
                "  Dauer: " + roundDuration(duration) +
                "  Gesamtdauer: " + roundDuration(usedCounter.duration));

        onlyPing(getClassName(), text, txt, usedCounter.pingText);
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
            // should not happen
            return;
        }

        Duration duration = Duration.between(usedCounter.pingTime, Instant.now());
        usedCounter.pingTime();
        String extra = "  --> Ping Dauer: " + roundDuration(duration);
        usedCounter.pingText.add(extra);

    }

    public synchronized static void onlyPing(String text) {
        onlyPing(getClassName(), text, null, null);
    }

    private static void onlyPing(String className, String text, List<String> extraText, List<String> pingText) {
        final Instant now = Instant.now();

        ArrayList<String> list = new ArrayList<>();
        list.add(PLog.LILNE3);
        list.add("DURATION " + sum++ + ":  " + text + "  [" + roundDuration(Duration.between(onlyPingTime, now)) + "]");
        list.add("  Klasse:  " + className);

        if (pingText != null && !pingText.isEmpty()) {
            pingText.stream().forEach(s -> list.add(s));
        }

        if (extraText != null && !extraText.isEmpty()) {
            extraText.stream().forEach(txt -> list.add("  " + txt));
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
        for (final Counter counter : COUNTER_LIST) {
            while (counter.counterName.length() < max) {
                counter.counterName = counter.counterName + " ";
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
