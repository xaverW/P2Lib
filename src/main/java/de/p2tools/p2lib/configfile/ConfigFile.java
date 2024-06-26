/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2lib.configfile;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigFile {

    private String filePath = "";
    private final InputStreamReader isr;
    private String xmlStart = P2LibConst.CONFIG_XML_START;
    private final boolean backup;//Backup laden / Backup speichern
    private String backupHeader = "", backupText = "";//Text der angezeigt wird, wenn das Backup geladen werden soll
    private final ArrayList<P2Data> p2Data;
    private final ArrayList<P2DataList> p2DataList;

    public ConfigFile(String filePath, boolean backup) {
        this.filePath = filePath;
        this.isr = null;
        this.backup = backup;
        this.p2DataList = new ArrayList<>();
        this.p2Data = new ArrayList<>();
    }

    public ConfigFile(String filePath, String xmlStart, boolean backup) {
        this.filePath = filePath;
        if (!xmlStart.isEmpty()) {
            this.xmlStart = xmlStart;
        }
        this.isr = null;
        this.backup = backup;
        this.p2DataList = new ArrayList<>();
        this.p2Data = new ArrayList<>();
    }

    public ConfigFile(InputStreamReader isr, boolean backup) {
        this.isr = isr;
        this.backup = backup;
        this.p2DataList = new ArrayList<>();
        this.p2Data = new ArrayList<>();
    }

    public void clearConfigFile() {
        //muss, wenn gebraucht, überschieben werden!!
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public InputStreamReader getIsr() {
        return isr;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackupHeader(String backupHeader) {
        this.backupHeader = backupHeader;
    }

    public String getBackupHeader() {
        return backupHeader;
    }

    public void setBackupText(String backupText) {
        this.backupText = backupText;
    }

    public String getBackupText() {
        return backupText;
    }

    public void addConfigs(P2Data p2Data) {
        this.p2Data.add(p2Data);
    }

    public void addConfigs(P2DataList configsData) {
        p2DataList.add(configsData);
    }

    public ArrayList<P2DataList> getpDataList() {
        return p2DataList;
    }

    public ArrayList<P2Data> getpData() {
        return p2Data;
    }

    public void setXmlStart(String xmlStart) {
        this.xmlStart = xmlStart;
    }

    public String getXmlStart() {
        return xmlStart;
    }
}
