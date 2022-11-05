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

package de.p2tools.p2Lib.MTDownload;

import de.p2tools.p2Lib.guiTools.PSizeTools;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ObjectPropertyBase;

public class DownloadSize extends ObjectPropertyBase<Long> implements Comparable<DownloadSize> {

    private long actFileSize = -1;

    public DownloadSize() {
    }

    public DownloadSize(long fileSize) {
        setValue(fileSize);
    }

    public DownloadSize(long fileSize, long actFileSize) {
        setValue(fileSize);
        this.actFileSize = actFileSize;
    }

    @Override
    public Object getBean() {
        return DownloadSize.this;
    }

    @Override
    public String getName() {
        return "PDownloadSize";
    }

    @Override
    public int compareTo(DownloadSize downloadSize) {
        return (Long.compare(getValue(), downloadSize.getValue()));
    }

    @Override
    public String toString() {
        return getValue() + "";
    }

    //======================================================
    public void reset() {
        actFileSize = -1;
        fireValueChangedEvent();
    }

    public Long getFileSize() {
        return getValue();
    }

    public void setFileSize(String size) {
        if (size.isEmpty()) {
            actFileSize = -1;
            setValue(0L);

        } else {
            try {
                setValue(Long.valueOf(size));
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                actFileSize = -1;
                setValue(0L);
            }
        }
        fireValueChangedEvent();
    }

    public void setFileSize(long l) {
        setValue(l);
        if (l == 0) {
            actFileSize = -1;
        }
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        actFileSize += l;
        if (getValue() < actFileSize) {
            //kann bei m3u8-URL passieren
            setValue(actFileSize);
        }
        fireValueChangedEvent();
    }

    public long getActFileSize() {
        return actFileSize;
    }

    public void setActFileSize(long l) {
        actFileSize = l;
        if (getValue() < actFileSize) {
            //kann bei m3u8-URL passieren
            setValue(l);
        }
        fireValueChangedEvent();
    }

    public String getHumanReadAbleFileSize() {
        return PSizeTools.humanReadableByteCount(getValue(), true);
    }

    public String getActSizeString() {
        String sizeStr;
        if (actFileSize <= 0) {
            if (getValue() > 0) {
                sizeStr = PSizeTools.getSize(getValue());
            } else {
                sizeStr = "";
            }

        } else if (getValue() > 0) {
            sizeStr = PSizeTools.getSize(actFileSize) + " von " + PSizeTools.getSize(getValue());

        } else {
            sizeStr = PSizeTools.getSize(actFileSize);
        }

        return sizeStr;
    }

    public String getSizeString() {
        String sizeStr;
        if (getValue() > 0) {
            sizeStr = PSizeTools.getSize(getValue());

        } else {
            sizeStr = "";
        }

        return sizeStr;
    }
}
