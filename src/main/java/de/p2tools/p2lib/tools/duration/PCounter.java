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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class PCounter implements Comparable<PCounter> {

    String counterName;
    List<String> pingTextList = new ArrayList<>();
    int count = 0;
    Duration duration = Duration.ZERO;
    Instant pingTime;
    Instant startTime;

    PCounter(String counterName) {
        this.counterName = counterName;
        startCounter();
    }

    void startCounter() {
        pingTextList.clear();
        pingTime = Instant.now();
        startTime = Instant.now();
    }

    long getAverage() {
        if (count == 0) {
            return 0L;
        }

        final long average = duration.toMillis() / count;
        return average;
    }

    void pingTime() {
        pingTime = Instant.now();
    }

    @Override
    public int compareTo(PCounter o) {
        return counterName.compareTo(o.counterName);
    }
}
