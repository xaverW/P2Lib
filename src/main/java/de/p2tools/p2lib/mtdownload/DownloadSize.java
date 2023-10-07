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

    private long fileActuallySize = 0L; // ist der Wert des aktuell laufenden Downloads (Downloadteil bei Abbrüchen)
    private Long fileTargetSize = 0L; // ist der Wert aus der URL/geladenen Datei, also Org-Größe

    public DownloadSize() {
    }

    @Override
    public void setValue(DownloadSizeData v) {
        super.setValue(v);
        fileTargetSize = v.l;
    }

    @Override
    public DownloadSizeData getValue() {
        return super.getValue();
    }

    @Override
    public final DownloadSizeData get() {
        return new DownloadSizeData(fileTargetSize, getString());
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
        return (fileTargetSize.compareTo(ll.fileTargetSize));
    }

    @Override
    public String toString() {
        return getString();
    }

    public void setFileTargetSize(String size) {
        // im Film ist die Größe in "MB" !!
        if (size.isEmpty()) {
            fileActuallySize = 0L;
            fileTargetSize = 0L;
        } else {
            try {
                fileTargetSize = Long.valueOf(size);
                fileTargetSize = fileTargetSize * 1000 * 1000;
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                fileTargetSize = 0L;
            }
        }

        fireValueChangedEvent();
    }

    public void setFileSizeUrl(long l) {
        fileTargetSize = l;
        fireValueChangedEvent();
    }

    public long getFileTargetSize() {
        return fileTargetSize;
    }

    public void setFileActuallySize(long l) {
        fileActuallySize = l;
        if (fileTargetSize < fileActuallySize) {
            //kann bei m3u8-URL passieren
            fileTargetSize = fileActuallySize;
        }
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        fileActuallySize += l;
        if (fileTargetSize < fileActuallySize) {
            //kann bei m3u8-URL passieren
            fileTargetSize = fileActuallySize;
        }
        fireValueChangedEvent();
    }

    public long getFileActuallySize() {
        return fileActuallySize;
    }

    public void resetActFileSize() {
        fileActuallySize = 0L;
        fireValueChangedEvent();
    }

    private String getString() {
        if (fileActuallySize <= 0) {
            if (fileTargetSize > 0) {
                return SizeTools.getSize(fileTargetSize);
            } else {
                return "";
            }
        }

        // dann gibts eine aktSize > 0
        if (fileTargetSize == fileActuallySize) {
            // ist bei m3u8-URLs so, die wachsen
            return SizeTools.getSize(fileTargetSize);
        }

        if (fileTargetSize > 0) {
            return SizeTools.getSize(fileActuallySize) + " von " + SizeTools.getSize(fileTargetSize);
        }

        return SizeTools.getSize(fileActuallySize);
    }
}
