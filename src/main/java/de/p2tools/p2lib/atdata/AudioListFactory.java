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


package de.p2tools.p2lib.atdata;

import de.p2tools.p2lib.tools.date.DateFactory;
import de.p2tools.p2lib.tools.date.PLDateTimeFactory;
import de.p2tools.p2lib.tools.log.PLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AudioListFactory {
    private AudioListFactory() {
    }

    /**
     * Check if available AudioList is older than a specified value.
     *
     * @return true if too old or if the list is empty.
     */
    public static boolean isTooOld(String strAge, int max) {
        if (strAge.isEmpty()) {
            //dann ist das Alter nicht gesetzt
            PLog.sysLog("Die Audioliste hat kein Alter gespeichert -> Neue laden");
            return true;
        }
        long age = getAge(strAge);
        return age >= max;
    }

    public static boolean isNotFromToday(String strDate) {
        LocalDateTime listDate = PLDateTimeFactory.fromString(strDate, DateFactory.DT_FORMATTER_dd_MM_yyyy___HH__mm);
        LocalDate act = listDate.toLocalDate(); //2015-11-??
        LocalDate today = LocalDate.now(); //2015-11-23
        return !act.equals(today);
    }

    /**
     * Get the age of the film list.
     *
     * @return Age in seconds.
     */
    public static long getAge(String strDate) {
        LocalDateTime listDate = PLDateTimeFactory.fromString(strDate, DateFactory.DT_FORMATTER_dd_MM_yyyy___HH__mm);
        LocalDateTime now = LocalDateTime.now();
        return listDate.until(now, ChronoUnit.DAYS);
    }

    public static synchronized long countNewAudios(AudioList audioList) {
        return audioList.stream().filter(AudioData::isNewAudio).count();
    }
}
