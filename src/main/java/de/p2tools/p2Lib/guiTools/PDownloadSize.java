/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2Lib.guiTools;

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ObjectPropertyBase;

public class PDownloadSize extends ObjectPropertyBase<String> implements Comparable<PDownloadSize> {

    private long actFileSize = -1;
    private long fileSize = 0;

    public PDownloadSize() {
    }

    public PDownloadSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public PDownloadSize(long fileSize, long actFileSize) {
        this.fileSize = fileSize;
        this.actFileSize = actFileSize;
    }

    @Override
    public void setValue(String v) {
        super.setValue(v);
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public final String get() {
        return getSizeString();
    }

    @Override
    public Object getBean() {
        return PDownloadSize.this;
    }

    @Override
    public String getName() {
        return "PDownloadSize";
    }

    @Override
    public int compareTo(PDownloadSize pDownloadSize) {
        return (Long.compare(fileSize, pDownloadSize.fileSize));
    }

    @Override
    public String toString() {
        return fileSize + "";
    }

    //======================================================
    public void reset() {
        actFileSize = -1;
        fireValueChangedEvent();
    }

    public void setFileSize(String size) {
        if (size.isEmpty()) {
            actFileSize = -1;
            fileSize = 0;

        } else {
            try {
                fileSize = Long.valueOf(size);
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                fileSize = 0;
            }
        }
        fireValueChangedEvent();
    }

    public void setFileSize(long l) {
        fileSize = l;
        fireValueChangedEvent();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setActFileSize(long l) {
        actFileSize = l;
        if (fileSize < actFileSize) {
            //kann bei m3u8-URL passieren
            fileSize = 0;
        }
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        actFileSize += l;
        if (fileSize < actFileSize) {
            //kann bei m3u8-URL passieren
            fileSize = 0;
        }
        fireValueChangedEvent();
    }

    public long getActFileSize() {
        return actFileSize;
    }

    public String getHumanReadAbleFileSize() {
        return PSizeTools.humanReadableByteCount(fileSize, true);
    }

    public String getSizeString() {
        String sizeStr;
        if (actFileSize <= 0) {
            if (fileSize > 0) {
                sizeStr = PSizeTools.getSize(fileSize);
            } else {
                sizeStr = "";
            }
        } else if (fileSize > 0) {
            sizeStr = PSizeTools.getSize(actFileSize) + " von " + PSizeTools.getSize(fileSize);
        } else {
            sizeStr = PSizeTools.getSize(actFileSize);
        }
        return sizeStr;
    }
}
