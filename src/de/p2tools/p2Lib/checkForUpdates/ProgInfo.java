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

package de.p2tools.p2Lib.checkForUpdates;

import de.p2tools.p2Lib.tools.Log;

import java.util.ArrayList;

/**
 * Encapsulates the retrieved update information.
 */
class ProgInfo {
    private String progName = "";
    private String progUrl = "";
    private String progDownloadUrl = "";
    private int progVersion = -1;
    private String progReleaseNotes = "";
    private ArrayList<Infos> infos = new ArrayList<>(5);


    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public String getProgUrl() {
        return progUrl;
    }

    public void setProgUrl(String progUrl) {
        this.progUrl = progUrl;
    }

    public String getProgDownloadUrl() {
        return progDownloadUrl;
    }

    public void setProgDownloadUrl(String progDownloadUrl) {
        this.progDownloadUrl = progDownloadUrl;
    }

    public int getProgVersion() {
        return progVersion;
    }

    public void setProgVersion(int progVersion) {
        this.progVersion = progVersion;
    }

    public void setProgVersion(String progVersion) {
        try {
            this.progVersion = Integer.parseInt(progVersion);
        } catch (NumberFormatException ex) {
            Log.errorLog(915260145, ex, "Fehler beim Parsen der Version '" + progVersion + "'.");
            this.progVersion = -1;
        }
    }

    public String getProgReleaseNotes() {
        return progReleaseNotes;
    }

    public void setProgReleaseNotes(String progReleaseNotes) {
        this.progReleaseNotes = progReleaseNotes;
    }

    public ArrayList<Infos> getInfos() {
        return infos;
    }

    public void addProgInfo(Infos info) {
        infos.add(info);
    }

    /**
     * Tag definition for server response file
     */
    class ParserTags {
        final static String PROG = "prog";
        final static String PROG_NAME = "progName";
        final static String PROG_URL = "progUrl";
        final static String PROG_DOWNLOAD_URL = "progDownloadUrl";
        final static String PROG_VERSION = "progVersion";
        final static String PROG_RELEASE_NOTES = "releaseNotes";
        final static String PROG_INFOS = "infos";
        final static String PROG_INFOS_NUMBER = "number";
    }
}
