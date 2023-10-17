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


package de.p2tools.p2lib.checkforactinfos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class FoundSearchData {
    private Stage stage;

    public String searchUrl;
    public String searchUrlDownload;

    private final BooleanProperty searchAct;//Act/Info sollen gesucht werden
    private final BooleanProperty searchBeta;//auch beta soll gesucht werden
    private final BooleanProperty searchDaily;//und Daily auch noch

    private final BooleanProperty searchActAgain = new SimpleBooleanProperty(false);//gefundenes Act soll nochmal angezeigt werden

    private final StringProperty lastInfoDate;//letzte angezeigte Info
    private final StringProperty lastActDate;//letztes angezeigtes Act
    private final StringProperty lastBetaDate;//letzte angezeigte Beta
    private final StringProperty lastDailyDate;//letzte angezeigtes Daily

    private final BooleanProperty foundNewInfo = new SimpleBooleanProperty(false);//neue Info wurde gefunden
    private final BooleanProperty foundNewVersion = new SimpleBooleanProperty(false);//neues Act wurde gefunden
    private final BooleanProperty foundNewBeta = new SimpleBooleanProperty(false);//neues Beta wurde gefunden
    private final BooleanProperty foundNewDaily = new SimpleBooleanProperty(false);//neues Daily wurde gefunden

    private FoundFileList foundFileListInfo = new FoundFileList();//Liste der gefundenen neuen Infos
    private FoundFileList foundFileListAct = new FoundFileList();//Liste der gefundenen neuen Acts
    private FoundFileList foundFileListBeta = new FoundFileList();//Liste der gefundenen neuen Betas
    private FoundFileList foundFileListDaily = new FoundFileList();//Liste der gefundenen neuen Dailys

    private String newInfoText = "";//Text der neuen Infos
    private String newInfoDate = "";//neue Nummer der neuen Infos, Datum: 2021.05.01 oder 2021.05.01_1

    private String newVersionText = "";
    private String newVersionDate = "";
    private String newVersionNo = "";

    private String newBetaText = "";
    private String newBetaVersion = "";
    private String newBetaBuildNo = "";
    private String newBetaDate = "";

    private String newDailyText = "";
    private String newDailyVersion = "";
    private String newDailyBuild = "";
    private String newDailyDate = "";

    private String urlWebsite;
    private String urlDownload;
    private String progName;
    private String progVersion;//aktuelle Programmversion
    private String progBuildNo;
    private String progBuildDate;

    private final StringProperty downloadDir;

    private boolean showAlways;

    public FoundSearchData(final Stage stage,
                           final String searchUrl,
                           final String searchUrlDownload,

                           final BooleanProperty searchAct,
                           final BooleanProperty searchBeta, final BooleanProperty searchDaily,

                           final StringProperty lastInfoDate,
                           final StringProperty lastActDate,
                           final StringProperty lastBetaDate,
                           final StringProperty lastDailyDate,

                           final String urlWebsite,
                           final String urlDownload,
                           final String progName,
                           final String progVersion,
                           final String progBuildNo,
                           final String progBuildDate,
                           final StringProperty downloadDir,
                           final boolean showAlways
    ) {

        this.stage = stage;

        this.searchUrl = searchUrl;
        this.searchUrlDownload = searchUrlDownload;

        this.searchAct = searchAct;
        this.searchBeta = searchBeta;
        this.searchDaily = searchDaily;

        this.lastInfoDate = lastInfoDate;
        this.lastActDate = lastActDate;
        this.lastBetaDate = lastBetaDate;
        this.lastDailyDate = lastDailyDate;

        this.urlWebsite = urlWebsite;
        this.urlDownload = urlDownload;
        this.progName = progName;
        this.progVersion = progVersion;
        this.progBuildNo = progBuildNo;
        this.progBuildDate = progBuildDate;
        this.downloadDir = downloadDir;
        if (this.lastActDate.getValue().isEmpty()) {
            //das ist die aktuelle Programmversion die läuft
            this.lastActDate.setValue(this.progBuildDate);
        }
        if (this.lastBetaDate.getValue().isEmpty()) {
            //das ist die aktuelle Programmversion die läuft
            this.lastBetaDate.setValue(this.progBuildDate);
        }
        if (this.lastDailyDate.getValue().isEmpty()) {
            //das ist die aktuelle Programmversion die läuft
            this.lastDailyDate.setValue(this.progBuildDate);
        }
        this.showAlways = showAlways;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(final String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getSearchUrlDownload() {
        return searchUrlDownload;
    }

    public void setSearchUrlDownload(final String searchUrlDownload) {
        this.searchUrlDownload = searchUrlDownload;
    }

    public boolean isSearchAct() {
        return searchAct.get();
    }

    public BooleanProperty searchActProperty() {
        return searchAct;
    }

    public void setSearchAct(final boolean searchAct) {
        this.searchAct.set(searchAct);
    }

    public boolean isSearchBeta() {
        return searchBeta.get();
    }

    public BooleanProperty searchBetaProperty() {
        return searchBeta;
    }

    public void setSearchBeta(final boolean searchBeta) {
        this.searchBeta.set(searchBeta);
    }

    public boolean isSearchDaily() {
        return searchDaily.get();
    }

    public BooleanProperty searchDailyProperty() {
        return searchDaily;
    }

    public void setSearchDaily(final boolean searchDaily) {
        this.searchDaily.set(searchDaily);
    }

    public boolean isSearchActAgain() {
        return searchActAgain.get();
    }

    public BooleanProperty searchActAgainProperty() {
        return searchActAgain;
    }

    public void setSearchActAgain(final boolean searchActAgain) {
        this.searchActAgain.set(searchActAgain);
    }

    public String getLastInfoDate() {
        return lastInfoDate.get();
    }

    public StringProperty lastInfoDateProperty() {
        return lastInfoDate;
    }

    public void setLastInfoDate(final String lastInfoDate) {
        this.lastInfoDate.set(lastInfoDate);
    }

    public String getLastActDate() {
        return lastActDate.get();
    }

    public StringProperty lastActDateProperty() {
        return lastActDate;
    }

    public void setLastActDate(final String lastActDate) {
        this.lastActDate.set(lastActDate);
    }

    public String getLastBetaDate() {
        return lastBetaDate.get();
    }

    public StringProperty lastBetaDateProperty() {
        return lastBetaDate;
    }

    public void setLastBetaDate(final String lastBetaDate) {
        this.lastBetaDate.set(lastBetaDate);
    }

    public String getLastDailyDate() {
        return lastDailyDate.get();
    }

    public StringProperty lastDailyDateProperty() {
        return lastDailyDate;
    }

    public void setLastDailyDate(final String lastDailyDate) {
        this.lastDailyDate.set(lastDailyDate);
    }

    public boolean isFoundNewInfo() {
        return foundNewInfo.get();
    }

    public BooleanProperty foundNewInfoProperty() {
        return foundNewInfo;
    }

    public void setFoundNewInfo(final boolean foundNewInfo) {
        this.foundNewInfo.set(foundNewInfo);
    }

    public boolean isFoundNewVersion() {
        return foundNewVersion.get();
    }

    public BooleanProperty foundNewVersionProperty() {
        return foundNewVersion;
    }

    public void setFoundNewVersion(final boolean foundNewVersion) {
        this.foundNewVersion.set(foundNewVersion);
    }

    public boolean isFoundNewBeta() {
        return foundNewBeta.get();
    }

    public BooleanProperty foundNewBetaProperty() {
        return foundNewBeta;
    }

    public void setFoundNewBeta(final boolean foundNewBeta) {
        this.foundNewBeta.set(foundNewBeta);
    }

    public boolean isFoundNewDaily() {
        return foundNewDaily.get();
    }

    public BooleanProperty foundNewDailyProperty() {
        return foundNewDaily;
    }

    public void setFoundNewDaily(final boolean foundNewDaily) {
        this.foundNewDaily.set(foundNewDaily);
    }

    public FoundFileList getFoundFileListInfo() {
        return foundFileListInfo;
    }

    public void setFoundFileListInfo(final FoundFileList foundFileListInfo) {
        this.foundFileListInfo = foundFileListInfo;
    }

    public FoundFileList getFoundFileListAct() {
        return foundFileListAct;
    }

    public void setFoundFileListAct(final FoundFileList foundFileListAct) {
        this.foundFileListAct = foundFileListAct;
    }

    public FoundFileList getFoundFileListBeta() {
        return foundFileListBeta;
    }

    public void setFoundFileListBeta(final FoundFileList foundFileListBeta) {
        this.foundFileListBeta = foundFileListBeta;
    }

    public FoundFileList getFoundFileListDaily() {
        return foundFileListDaily;
    }

    public void setFoundFileListDaily(final FoundFileList foundFileListDaily) {
        this.foundFileListDaily = foundFileListDaily;
    }

    public String getNewInfoText() {
        return newInfoText;
    }

    public void setNewInfoText(final String newInfoText) {
        this.newInfoText = newInfoText;
    }

    public String getNewInfoDate() {
        return newInfoDate;
    }

    public void setNewInfoDate(final String newInfoDate) {
        this.newInfoDate = newInfoDate;
    }

    public String getNewVersionText() {
        return newVersionText;
    }

    public void setNewVersionText(final String newVersionText) {
        this.newVersionText = newVersionText;
    }

    public String getNewVersionDate() {
        return newVersionDate;
    }

    public void setNewVersionDate(final String newVersionDate) {
        this.newVersionDate = newVersionDate;
    }

    public String getNewVersionNo() {
        return newVersionNo;
    }

    public void setNewVersionNo(final String newVersionNo) {
        this.newVersionNo = newVersionNo;
    }

    public String getNewBetaText() {
        return newBetaText;
    }

    public void setNewBetaText(final String newBetaText) {
        this.newBetaText = newBetaText;
    }

    public String getNewBetaVersion() {
        return newBetaVersion;
    }

    public void setNewBetaVersion(final String newBetaVersion) {
        this.newBetaVersion = newBetaVersion;
    }

    public String getNewBetaBuildNo() {
        return newBetaBuildNo;
    }

    public void setNewBetaBuildNo(final String newBetaBuildNo) {
        this.newBetaBuildNo = newBetaBuildNo;
    }

    public String getNewBetaDate() {
        return newBetaDate;
    }

    public void setNewBetaDate(final String newBetaDate) {
        this.newBetaDate = newBetaDate;
    }

    public String getNewDailyText() {
        return newDailyText;
    }

    public void setNewDailyText(final String newDailyText) {
        this.newDailyText = newDailyText;
    }

    public String getNewDailyVersion() {
        return newDailyVersion;
    }

    public void setNewDailyVersion(final String newDailyVersion) {
        this.newDailyVersion = newDailyVersion;
    }

    public String getNewDailyBuild() {
        return newDailyBuild;
    }

    public void setNewDailyBuild(final String newDailyBuild) {
        this.newDailyBuild = newDailyBuild;
    }

    public String getNewDailyDate() {
        return newDailyDate;
    }

    public void setNewDailyDate(final String newDailyDate) {
        this.newDailyDate = newDailyDate;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public void setUrlWebsite(final String urlWebsite) {
        this.urlWebsite = urlWebsite;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(final String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(final String progName) {
        this.progName = progName;
    }

    public String getProgVersion() {
        return progVersion;
    }

    public void setProgVersion(final String progVersion) {
        this.progVersion = progVersion;
    }

    public String getProgBuildNo() {
        return progBuildNo;
    }

    public void setProgBuildNo(final String progBuildNo) {
        this.progBuildNo = progBuildNo;
    }

    public String getProgBuildDate() {
        return progBuildDate;
    }

    public void setProgBuildDate(final String progBuildDate) {
        this.progBuildDate = progBuildDate;
    }

    public String getDownloadDir() {
        return downloadDir.get();
    }

    public StringProperty downloadDirProperty() {
        return downloadDir;
    }

    public void setDownloadDir(final String downloadDir) {
        this.downloadDir.set(downloadDir);
    }

    public boolean isShowAlways() {
        return showAlways;
    }

    public void setShowAlways(final boolean showAlways) {
        this.showAlways = showAlways;
    }
}
