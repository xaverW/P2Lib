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

package de.p2tools.p2lib.mtdownload;

import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.ObjectPropertyBase;

public class DownloadSize extends ObjectPropertyBase<DownloadSizeData> implements Comparable<DownloadSize> {

    private long fileSizeLoaded = -1L; // ist der Wert des aktuell laufenden Downloads (Teil bei Abbrüchen)
    private Long fileSizeUrl = 0L; // ist der Wert aus der URL, also Org-Größe

    public DownloadSize() {
    }

    @Override
    public void setValue(DownloadSizeData v) {
        super.setValue(v);
        fileSizeUrl = v.l;
    }

    @Override
    public DownloadSizeData getValue() {
        return super.getValue();
    }

    @Override
    public final DownloadSizeData get() {
        return new DownloadSizeData(fileSizeUrl, getString());
    }

    @Override
    public Object getBean() {
        return DownloadSize.this;
    }

    @Override
    public String getName() {
        return "DownloadSize";
    }

    @Override
    public int compareTo(DownloadSize ll) {
        return (fileSizeUrl.compareTo(ll.fileSizeUrl));
    }

    @Override
    public String toString() {
        return getString();
    }

    public void setFileSizeUrl(String size) {
        // im Film ist die Größe in "MB" !!
        if (size.isEmpty()) {
            fileSizeLoaded = -1L;
            fileSizeUrl = 0L;
        } else {
            try {
                fileSizeUrl = Long.valueOf(size);
                fileSizeUrl = fileSizeUrl * 1000 * 1000;
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                fileSizeUrl = 0L;
            }
        }

        fireValueChangedEvent();
    }

    public void setFileSize(long l) {
        fileSizeUrl = l;
        fireValueChangedEvent();
    }

    public long getFileSizeUrl() {
        return fileSizeUrl;
    }

    public void setFileSizeLoaded(long l) {
        fileSizeLoaded = l;
        if (fileSizeUrl < fileSizeLoaded) {
            //kann bei m3u8-URL passieren
            fileSizeUrl = 0L;
        }
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        fileSizeLoaded += l;
        if (fileSizeUrl < fileSizeLoaded) {
            //kann bei m3u8-URL passieren
            fileSizeUrl = 0L;
        }
        fireValueChangedEvent();
    }

    public long getFileSizeLoaded() {
        return fileSizeLoaded;
    }

    public void resetActFileSize() {
        fileSizeLoaded = -1L;
        fireValueChangedEvent();
    }

    private String getString() {
        String sizeStr;
        if (fileSizeLoaded <= 0) {
            if (fileSizeUrl > 0) {
                sizeStr = SizeTools.getSize(fileSizeUrl);
            } else {
                sizeStr = "";
            }
        } else if (fileSizeUrl > 0) {
            sizeStr = SizeTools.getSize(fileSizeLoaded) + " von " + SizeTools.getSize(fileSizeUrl);
        } else {
            sizeStr = SizeTools.getSize(fileSizeLoaded);
        }
        return sizeStr;
    }
}
