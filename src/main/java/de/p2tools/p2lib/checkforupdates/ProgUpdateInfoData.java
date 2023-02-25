/*
 * P2Tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.checkforupdates;

import de.p2tools.p2lib.tools.log.PLog;

/**
 * Encapsulates the retrieved update information.
 */
class ProgUpdateInfoData {
    private String info = "";
    private int infoNr = 0;

    public ProgUpdateInfoData(String info, String infoNr) {
        this.info = info;
        try {
            this.infoNr = Integer.parseInt(infoNr);
        } catch (NumberFormatException ex) {
            PLog.errorLog(915454102, ex, "Fehler beim Parsen der Info-Nr '" + infoNr + "'.");
            this.infoNr = -1;
        }

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getInfoNr() {
        return infoNr;
    }

    public void setInfoNr(int infoNr) {
        this.infoNr = infoNr;
    }
}
