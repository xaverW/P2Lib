/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.checkForActInfos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class FoundSearchData {
    private Stage stage;

    public String searchUrl;
    public String searchUrlDownload;

    private BooleanProperty searchAct;//Act/Info sollen gesucht werden
    private BooleanProperty searchBeta;//auch beta soll gesucht werden
    private BooleanProperty searchDaily;//und Daily auch noch

    private BooleanProperty searchActAgain = new SimpleBooleanProperty(false);//gefundenes Act soll nochmal angezeigt werden

    private StringProperty lastInfoDate;//letzte angezeigte Info
    private StringProperty lastActDate;//letztes angezeigtes Act
    private StringProperty lastBetaDate;//letzte angezeigte Beta
    private StringProperty lastDailyDate;//letzte angezeigtes Daily

    private BooleanProperty foundNewInfo = new SimpleBooleanProperty(false);//neue Info wurde gefunden
    private BooleanProperty foundNewVersion = new SimpleBooleanProperty(false);//neues Act wurde gefunden
    private BooleanProperty foundNewBeta = new SimpleBooleanProperty(false);//neues Beta wurde gefunden
    private BooleanProperty foundNewDaily = new SimpleBooleanProperty(false);//neues Daily wurde gefunden

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
    private String newBetaDate = "";

    private String newDailyText = "";
    private String newDailyDate = "";

    private String urlWebsite;
    private String urlDownload;
    private String progName;
    private String progVersion;//aktuelle Programmversion
    private String progBuildDate;
    private boolean showAllways;

    public FoundSearchData(Stage stage,
                           String searchUrl,
                           String searchUrlDownload,

                           BooleanProperty searchAct,
                           BooleanProperty searchBeta, BooleanProperty searchDaily,

                           StringProperty lastInfoDate,
                           StringProperty lastActDate,
                           StringProperty lastBetaDate,
                           StringProperty lastDailyDate,

                           String urlWebsite,
                           String urlDownload,
                           String progName,
                           String progVersion,
                           String progBuildDate,
                           boolean showAllways
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
        this.progBuildDate = progBuildDate;
        if (this.lastActDate.getValue().isEmpty()) {
            //das ist die aktuelle Programmversion die läuft
            this.lastActDate.setValue(this.progBuildDate);
        }
        this.showAllways = showAllways;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getSearchUrlDownload() {
        return searchUrlDownload;
    }

    public void setSearchUrlDownload(String searchUrlDownload) {
        this.searchUrlDownload = searchUrlDownload;
    }

    public boolean isSearchAct() {
        return searchAct.get();
    }

    public BooleanProperty searchActProperty() {
        return searchAct;
    }

    public void setSearchAct(boolean searchAct) {
        this.searchAct.set(searchAct);
    }

    public boolean isSearchBeta() {
        return searchBeta.get();
    }

    public BooleanProperty searchBetaProperty() {
        return searchBeta;
    }

    public void setSearchBeta(boolean searchBeta) {
        this.searchBeta.set(searchBeta);
    }

    public boolean isSearchDaily() {
        return searchDaily.get();
    }

    public BooleanProperty searchDailyProperty() {
        return searchDaily;
    }

    public void setSearchDaily(boolean searchDaily) {
        this.searchDaily.set(searchDaily);
    }

    public boolean isSearchActAgain() {
        return searchActAgain.get();
    }

    public BooleanProperty searchActAgainProperty() {
        return searchActAgain;
    }

    public void setSearchActAgain(boolean searchActAgain) {
        this.searchActAgain.set(searchActAgain);
    }

    public String getLastInfoDate() {
        return lastInfoDate.get();
    }

    public StringProperty lastInfoDateProperty() {
        return lastInfoDate;
    }

    public void setLastInfoDate(String lastInfoDate) {
        this.lastInfoDate.set(lastInfoDate);
    }

    public String getLastActDate() {
        return lastActDate.get();
    }

    public StringProperty lastActDateProperty() {
        return lastActDate;
    }

    public void setLastActDate(String lastActDate) {
        this.lastActDate.set(lastActDate);
    }

    public String getLastBetaDate() {
        return lastBetaDate.get();
    }

    public StringProperty lastBetaDateProperty() {
        return lastBetaDate;
    }

    public void setLastBetaDate(String lastBetaDate) {
        this.lastBetaDate.set(lastBetaDate);
    }

    public String getLastDailyDate() {
        return lastDailyDate.get();
    }

    public StringProperty lastDailyDateProperty() {
        return lastDailyDate;
    }

    public void setLastDailyDate(String lastDailyDate) {
        this.lastDailyDate.set(lastDailyDate);
    }

    public boolean isFoundNewInfo() {
        return foundNewInfo.get();
    }

    public BooleanProperty foundNewInfoProperty() {
        return foundNewInfo;
    }

    public void setFoundNewInfo(boolean foundNewInfo) {
        this.foundNewInfo.set(foundNewInfo);
    }

    public boolean isFoundNewVersion() {
        return foundNewVersion.get();
    }

    public BooleanProperty foundNewVersionProperty() {
        return foundNewVersion;
    }

    public void setFoundNewVersion(boolean foundNewVersion) {
        this.foundNewVersion.set(foundNewVersion);
    }

    public boolean isFoundNewBeta() {
        return foundNewBeta.get();
    }

    public BooleanProperty foundNewBetaProperty() {
        return foundNewBeta;
    }

    public void setFoundNewBeta(boolean foundNewBeta) {
        this.foundNewBeta.set(foundNewBeta);
    }

    public boolean isFoundNewDaily() {
        return foundNewDaily.get();
    }

    public BooleanProperty foundNewDailyProperty() {
        return foundNewDaily;
    }

    public void setFoundNewDaily(boolean foundNewDaily) {
        this.foundNewDaily.set(foundNewDaily);
    }

    public FoundFileList getFoundFileListInfo() {
        return foundFileListInfo;
    }

    public void setFoundFileListInfo(FoundFileList foundFileListInfo) {
        this.foundFileListInfo = foundFileListInfo;
    }

    public FoundFileList getFoundFileListAct() {
        return foundFileListAct;
    }

    public void setFoundFileListAct(FoundFileList foundFileListAct) {
        this.foundFileListAct = foundFileListAct;
    }

    public FoundFileList getFoundFileListBeta() {
        return foundFileListBeta;
    }

    public void setFoundFileListBeta(FoundFileList foundFileListBeta) {
        this.foundFileListBeta = foundFileListBeta;
    }

    public FoundFileList getFoundFileListDaily() {
        return foundFileListDaily;
    }

    public void setFoundFileListDaily(FoundFileList foundFileListDaily) {
        this.foundFileListDaily = foundFileListDaily;
    }

    public String getNewInfoText() {
        return newInfoText;
    }

    public void setNewInfoText(String newInfoText) {
        this.newInfoText = newInfoText;
    }

    public String getNewInfoDate() {
        return newInfoDate;
    }

    public void setNewInfoDate(String newInfoDate) {
        this.newInfoDate = newInfoDate;
    }

    public String getNewVersionText() {
        return newVersionText;
    }

    public void setNewVersionText(String newVersionText) {
        this.newVersionText = newVersionText;
    }

    public String getNewVersionDate() {
        return newVersionDate;
    }

    public void setNewVersionDate(String newVersionDate) {
        this.newVersionDate = newVersionDate;
    }

    public String getNewVersionNo() {
        return newVersionNo;
    }

    public void setNewVersionNo(String newVersionNo) {
        this.newVersionNo = newVersionNo;
    }

    public String getNewBetaText() {
        return newBetaText;
    }

    public void setNewBetaText(String newBetaText) {
        this.newBetaText = newBetaText;
    }

    public String getNewBetaDate() {
        return newBetaDate;
    }

    public void setNewBetaDate(String newBetaDate) {
        this.newBetaDate = newBetaDate;
    }

    public String getNewDailyText() {
        return newDailyText;
    }

    public void setNewDailyText(String newDailyText) {
        this.newDailyText = newDailyText;
    }

    public String getNewDailyDate() {
        return newDailyDate;
    }

    public void setNewDailyDate(String newDailyDate) {
        this.newDailyDate = newDailyDate;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public void setUrlWebsite(String urlWebsite) {
        this.urlWebsite = urlWebsite;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public String getProgVersion() {
        return progVersion;
    }

    public void setProgVersion(String progVersion) {
        this.progVersion = progVersion;
    }

    public String getProgBuildDate() {
        return progBuildDate;
    }

    public void setProgBuildDate(String progBuildDate) {
        this.progBuildDate = progBuildDate;
    }

    public boolean isShowAllways() {
        return showAllways;
    }

    public void setShowAllways(boolean showAllways) {
        this.showAllways = showAllways;
    }
}
