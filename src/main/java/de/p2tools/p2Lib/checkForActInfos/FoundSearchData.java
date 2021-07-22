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
    private BooleanProperty searchAct;
    private BooleanProperty searchBeta;
    private BooleanProperty searchDaily;

    private BooleanProperty searchActAgain = new SimpleBooleanProperty(false);

    private StringProperty lastInfo;
    private StringProperty lastAct;
    private StringProperty lastBeta;
    private StringProperty lastDaily;

    private BooleanProperty foundNewInfo = new SimpleBooleanProperty(false);
    private BooleanProperty foundNewVersion = new SimpleBooleanProperty(false);
    private BooleanProperty foundNewBeta = new SimpleBooleanProperty(false);
    private BooleanProperty foundNewDaily = new SimpleBooleanProperty(false);

    private FoundFileList foundFileListInfo = new FoundFileList();
    private FoundFileList foundFileListAct = new FoundFileList();
    private FoundFileList foundFileListBeta = new FoundFileList();
    private FoundFileList foundFileListDaily = new FoundFileList();

    private String newInfoText = "";
    private String newInfoNo = "";

    private String newVersionText = "";
    private String newVersionNo = "";

    private String newBetaText = "";
    private String newBetaNo = "";

    private String newDailyText = "";
    private String newDailyNo = "";

    private String urlWebsite;
    private String urlDownload;
    private String progName;
    private String progVersion;

    public FoundSearchData(Stage stage,
                           BooleanProperty searchAct,
                           BooleanProperty searchBeta, BooleanProperty searchDaily,

                           StringProperty lastInfo,
                           StringProperty lastACt,
                           StringProperty lastBeta,
                           StringProperty lastDaily,

                           String urlWebsite,
                           String urlDownload,
                           String progName,
                           String progVersion
    ) {

        this.stage = stage;

        this.searchAct = searchAct;
        this.searchBeta = searchBeta;
        this.searchDaily = searchDaily;

        this.lastInfo = lastInfo;
        this.lastAct = lastACt;
        this.lastBeta = lastBeta;
        this.lastDaily = lastDaily;

        this.urlWebsite = urlWebsite;
        this.urlDownload = urlDownload;
        this.progName = progName;
        this.progVersion = progVersion;
        if (this.lastAct.getValue().isEmpty()) {
            //das ist die aktuelle Programmversion die läuft
            this.lastAct.setValue(this.progVersion);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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

    public String getLastInfo() {
        return lastInfo.get();
    }

    public StringProperty lastInfoProperty() {
        return lastInfo;
    }

    public void setLastInfo(String lastInfo) {
        this.lastInfo.set(lastInfo);
    }

    public String getLastAct() {
        return lastAct.get();
    }

    public StringProperty lastActProperty() {
        return lastAct;
    }

    public void setLastAct(String lastAct) {
        this.lastAct.set(lastAct);
    }

    public String getLastBeta() {
        return lastBeta.get();
    }

    public StringProperty lastBetaProperty() {
        return lastBeta;
    }

    public void setLastBeta(String lastBeta) {
        this.lastBeta.set(lastBeta);
    }

    public String getLastDaily() {
        return lastDaily.get();
    }

    public StringProperty lastDailyProperty() {
        return lastDaily;
    }

    public void setLastDaily(String lastDaily) {
        this.lastDaily.set(lastDaily);
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

    public String getNewInfoNo() {
        return newInfoNo;
    }

    public void setNewInfoNo(String newInfoNo) {
        this.newInfoNo = newInfoNo;
    }

    public String getNewVersionText() {
        return newVersionText;
    }

    public void setNewVersionText(String newVersionText) {
        this.newVersionText = newVersionText;
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

    public String getNewBetaNo() {
        return newBetaNo;
    }

    public void setNewBetaNo(String newBetaNo) {
        this.newBetaNo = newBetaNo;
    }

    public String getNewDailyText() {
        return newDailyText;
    }

    public void setNewDailyText(String newDailyText) {
        this.newDailyText = newDailyText;
    }

    public String getNewDailyNo() {
        return newDailyNo;
    }

    public void setNewDailyNo(String newDailyNo) {
        this.newDailyNo = newDailyNo;
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
}
