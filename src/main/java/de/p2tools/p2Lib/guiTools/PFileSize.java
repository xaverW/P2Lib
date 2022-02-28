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

public class PFileSize extends ObjectPropertyBase implements Comparable<PFileSize> {
    private Long sizeL = 0L;
    private String sizeS = "";

    /**
     * damit wird nach long l sortiert und der Text s angezeigt
     *
     * @param sizeL
     */
    public PFileSize(long sizeL) {
        this.sizeL = sizeL;
        sizeS = PSizeTools.humanReadableByteCount(sizeL, true);
    }

    public PFileSize() {
    }

    @Override
    public Object getBean() {
        return PFileSize.this;
    }

    @Override
    public String getName() {
        return "PFileSize";
    }

    //================================================================
    public long getSizeL() {
        return sizeL;
    }

    public String getSizeS() {
        return sizeS;
    }

    public void setFileSize(long l) {
        this.sizeL = l;
        sizeS = PSizeTools.humanReadableByteCount(l, true);
        fireValueChangedEvent();
    }

    public void setFileSize(String size) {
        if (size.isEmpty()) {
            setFileSize(0);
        } else {
            try {
                setFileSize(Long.valueOf(size));
            } catch (final Exception ex) {
                PLog.errorLog(201354780, ex, "String: " + size);
                setFileSize(0);
            }
        }
    }

    @Override
    public int compareTo(PFileSize d) {
        return d.sizeL.compareTo(sizeL);
    }

    @Override
    public String toString() {
        return sizeL + "";
    }
}
