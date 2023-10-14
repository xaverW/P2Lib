/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

import de.p2tools.p2lib.tools.log.PLog;

public class AudioSize implements Comparable<AudioSize> {

    public long l = 0L;
    public String s = "";

    public AudioSize() {
    }

    void setAudioSize(AudioData audio) {
        if (audio.arr[AudioDataXml.AUDIO_SIZE].equals("<1")) {
            audio.arr[AudioDataXml.AUDIO_SIZE] = "1";
        }

        try {
            s = audio.arr[AudioDataXml.AUDIO_SIZE];
            if (audio.arr[AudioDataXml.AUDIO_SIZE].isEmpty()) {
                l = 0L;
            } else {
                l = Long.parseLong(audio.arr[AudioDataXml.AUDIO_SIZE]);
            }
        } catch (final Exception ex) {
            PLog.errorLog(649891025, ex, "String: " + audio.arr[AudioDataXml.AUDIO_SIZE]);
            l = 0L;
            s = "";
        }
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public int compareTo(AudioSize compareWith) {
        return Long.compare(l, compareWith.l);
    }
}
