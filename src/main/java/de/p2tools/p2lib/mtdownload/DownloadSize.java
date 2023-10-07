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

    private long actuallySize = 0L; // ist der Wert des aktuell laufenden Downloads (Downloadteil bei Abbrüchen)
    private Long targetSize = 0L; // ist der Wert aus der URL/geladenen Datei, also Org-Größe

    public DownloadSize() {
    }

    @Override
    public void setValue(DownloadSizeData v) {
        super.setValue(v);
        targetSize = v.l;
    }

    @Override
    public DownloadSizeData getValue() {
        return super.getValue();
    }

    @Override
    public final DownloadSizeData get() {
        return new DownloadSizeData(targetSize, getString());
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
        return (targetSize.compareTo(ll.targetSize));
    }

    @Override
    public String toString() {
        return getString();
    }

    public void setTargetSize(String size) {
        // im Film ist die Größe in "MB" !!
        if (size.isEmpty()) {
            actuallySize = 0L;
            targetSize = 0L;
        } else {
            try {
                targetSize = Long.valueOf(size);
                targetSize = targetSize * 1000 * 1000;
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                targetSize = 0L;
            }
        }

        fireValueChangedEvent();
    }

    public void setFileTargetSize(long l) {
        targetSize = l;
        fireValueChangedEvent();
    }

    public long getTargetSize() {
        return targetSize;
    }

    public void setActuallySize(long l) {
        actuallySize = l;
        if (targetSize < actuallySize) {
            //kann bei m3u8-URL passieren
            targetSize = actuallySize;
        }
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        actuallySize += l;
        if (targetSize < actuallySize) {
            //kann bei m3u8-URL passieren
            targetSize = actuallySize;
        }
        fireValueChangedEvent();
    }

    public long getActuallySize() {
        return actuallySize;
    }

    public void clearSize() {
        targetSize = 0L;
        actuallySize = 0L;
    }

    public void resetActFileSize() {
        actuallySize = 0L;
        fireValueChangedEvent();
    }

    private String getString() {
        if (actuallySize <= 0) {
            if (targetSize > 0) {
                return SizeTools.getSize(targetSize);
            } else {
                return "";
            }
        }

        // dann gibts eine aktSize > 0
        if (targetSize == actuallySize) {
            // ist bei m3u8-URLs so, die wachsen
            return SizeTools.getSize(targetSize);
        }

        if (targetSize > 0) {
            return SizeTools.getSize(actuallySize) + " von " + SizeTools.getSize(targetSize);
        }

        return SizeTools.getSize(actuallySize);
    }
}
