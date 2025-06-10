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

package de.p2tools.p2lib.audio;


import de.p2tools.p2lib.audio.audiodata.AudioData;
import de.p2tools.p2lib.audio.audiodata.AudioList;
import de.p2tools.p2lib.tools.date.P2DateGmtFactory;

import java.time.Duration;
import java.time.LocalDateTime;

public class P2AudioListFactory {
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    private P2AudioListFactory() {
    }

    public static int getAge(String[] metaData) {
        int ret = get(metaData);
        if (ret < 0) {
            ret = 0;
        }
        return ret;
    }

    private static int get(String[] metaData) {
        if (!metaData[AudioList.META_GMT].isEmpty()) {
            Duration dur = Duration.between(getDate(metaData), LocalDateTime.now());
            return (int) dur.toSeconds();

        } else {
            Duration dur = Duration.between(getDate(metaData), LocalDateTime.now());
            return (int) dur.toSeconds();
        }
    }

    public static LocalDateTime getDate(String[] metaData) {
        if (!metaData[AudioList.META_GMT].isEmpty()) {
            return P2DateGmtFactory.getLocalDateTimeFromGmt(metaData[AudioList.META_GMT]);

        } else {
            return P2DateGmtFactory.getLocalDateTimeFromGmt(metaData[AudioList.META_LOCAL]);
        }
    }

    public static synchronized long countNewAudios(AudioList audioList) {
        return audioList.stream().filter(AudioData::isNewAudio).count();
    }
}
