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

package de.p2tools.p2Lib.mtDownload;

import de.p2tools.p2Lib.guiTools.PSizeTools;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ObjectPropertyBase;

public class DownloadSize extends ObjectPropertyBase<String> implements Comparable<DownloadSize> {

    private long fileSize = 0;
    private long actFileSize = -1;

    public DownloadSize() {
        fileSize = 0L;
        makeSizeString();
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
        return (Long.compare(fileSize, downloadSize.getFileSize()));
    }

    //======================================================
    public void reset() {
        actFileSize = -1;
        makeSizeString();
        fireValueChangedEvent();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long l) {
        fileSize = l;
        if (l == 0) {
            actFileSize = -1;
        }
        makeSizeString();
        fireValueChangedEvent();
    }

    public void setFileSize(String size) {
        // im Film ist die Größe in "MB" !!
        if (size.isEmpty()) {
            actFileSize = -1;
            fileSize = 0L;

        } else {
            try {
                fileSize = (Long.valueOf(size) * 1000 * 1000);
            } catch (final Exception ex) {
                PLog.errorLog(978745320, ex, "String: " + size);
                actFileSize = -1;
                fileSize = 0L;
            }
        }
        makeSizeString();
        fireValueChangedEvent();
    }

    public String getFileSizeString() {
        String sizeStr;
        if (fileSize > 0) {
            sizeStr = PSizeTools.getSize(fileSize);

        } else {
            sizeStr = "";
        }

        return sizeStr;
    }

    public long getActFileSize() {
        return actFileSize;
    }

    public void setActFileSize(long l) {
        actFileSize = l;
        if (fileSize < actFileSize) {
            //kann bei m3u8-URL passieren
            fileSize = l;
        }
        makeSizeString();
        fireValueChangedEvent();
    }

    public void addActFileSize(long l) {
        actFileSize += l;
        if (fileSize < actFileSize) {
            //kann bei m3u8-URL passieren
            fileSize = actFileSize;
        }
        makeSizeString();
        fireValueChangedEvent();
    }

    private void makeSizeString() {
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

        super.setValue(sizeStr);
    }
}
