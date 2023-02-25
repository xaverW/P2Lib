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

import java.util.ArrayList;

/**
 * Encapsulates the retrieved update information.
 */
class ProgUpdateData {
    private String progName = "";
    private String progUrl = "";
    private String progDownloadUrl = "";
    private int progVersion = -1;
    private int progBuildNo = -1;
    private String progBuildDate = "";
    private String progReleaseNotes = "";
    private String showText = "";
    private String versionText = "";
    private ArrayList<ProgUpdateInfoData> infos = new ArrayList<>(5);
    private ArrayList<String> downloads = new ArrayList<>(3);


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
            PLog.errorLog(915260145, ex, "Fehler beim Parsen der Version '" + progVersion + "'.");
            this.progVersion = -1;
        }
    }

    public int getProgBuildNo() {
        return progBuildNo;
    }

    public void setProgBuildNo(int progBuildNo) {
        this.progBuildNo = progBuildNo;
    }

    public void setProgBuildNo(String progBuildNo) {
        try {
            this.progBuildNo = Integer.parseInt(progBuildNo);
        } catch (NumberFormatException ex) {
            PLog.errorLog(975120369, ex, "Fehler beim Parsen der BuildNo '" + progBuildNo + "'.");
            this.progBuildNo = -1;
        }
    }

    public String getProgBuildDate() {
        return progBuildDate;
    }

    public void setProgBuildDate(String progBuildDate) {
        this.progBuildDate = progBuildDate;
    }

    public String getProgReleaseNotes() {
        return progReleaseNotes;
    }

    public void setProgReleaseNotes(String progReleaseNotes) {
        this.progReleaseNotes = progReleaseNotes;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public ArrayList<ProgUpdateInfoData> getInfos() {
        return infos;
    }

    public void addProgInfo(ProgUpdateInfoData info) {
        infos.add(info);
    }

    public ArrayList<String> getDownloads() {
        return downloads;
    }

    public void addDownloads(String down) {
        downloads.add(down);
    }

    /**
     * Tag definition for server response file
     */
    class ParserTags {
        final static String PROG = "prog";
        final static String PROG_NAME = "progName";
        final static String PROG_URL = "progUrl";
        final static String PROG_DOWNLOAD_URL = "progDownloadUrl";
        final static String PROG_DOWNLOAD = "progDownload";
        final static String PROG_VERSION = "progVersion";
        final static String PROG_BUILD_NO = "progBuild";
        final static String PROG_BUILD_DATE = "buildDate";
        final static String PROG_RELEASE_NOTES = "releaseNotes";
        final static String PROG_INFOS = "infos";
        final static String PROG_INFOS_NUMBER = "number";
    }
}
