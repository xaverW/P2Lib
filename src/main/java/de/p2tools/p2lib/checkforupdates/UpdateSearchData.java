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


package de.p2tools.p2lib.checkforupdates;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

public class UpdateSearchData {

    private String updateUrl = "";
    private int progVersionNo = 0;
    private int progBuildNo = 0;
    private IntegerProperty updateVersionShown = null;
    private IntegerProperty updateBuildNoShown = null;
    private IntegerProperty updateInfoNoShown = null;
    private BooleanProperty searchUpdate = null;

    public UpdateSearchData(String updateUrl, int progVersionNo, int progBuildNo,
                            IntegerProperty updateVersionShown, IntegerProperty updateBuildNoShown,
                            IntegerProperty updateInfoNoShown, BooleanProperty searchUpdate) {
        this.updateUrl = updateUrl;
        this.progVersionNo = progVersionNo;
        this.progBuildNo = progBuildNo;
        this.updateVersionShown = updateVersionShown;
        this.updateBuildNoShown = updateBuildNoShown;
        this.updateInfoNoShown = updateInfoNoShown;
        this.searchUpdate = searchUpdate;
    }

    public UpdateSearchData(String updateUrl, int progVersionNo, int progBuildNo) {
        this.updateUrl = updateUrl;
        this.progVersionNo = progVersionNo;
        this.progBuildNo = progBuildNo;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public int getProgVersionNo() {
        return progVersionNo;
    }

    public void setProgVersionNo(int progVersionNo) {
        this.progVersionNo = progVersionNo;
    }

    public int getProgBuildNo() {
        return progBuildNo;
    }

    public void setProgBuildNo(int progBuildNo) {
        this.progBuildNo = progBuildNo;
    }

    public int getUpdateVersionShown() {
        return updateVersionShown.get();
    }

    public IntegerProperty updateVersionShownProperty() {
        return updateVersionShown;
    }

    public void setUpdateVersionShown(int updateVersionShown) {
        this.updateVersionShown.set(updateVersionShown);
    }

    public int getUpdateBuildNoShown() {
        return updateBuildNoShown.get();
    }

    public IntegerProperty updateBuildNoShownProperty() {
        return updateBuildNoShown;
    }

    public void setUpdateBuildNoShown(int updateBuildNoShown) {
        this.updateBuildNoShown.set(updateBuildNoShown);
    }

    public int getUpdateInfoNoShown() {
        return updateInfoNoShown.get();
    }

    public IntegerProperty updateInfoNoShownProperty() {
        return updateInfoNoShown;
    }

    public void setUpdateInfoNoShown(int updateInfoNoShown) {
        this.updateInfoNoShown.set(updateInfoNoShown);
    }

    public boolean isSearchUpdate() {
        return searchUpdate.get();
    }

    public BooleanProperty searchUpdateProperty() {
        return searchUpdate;
    }

    public void setSearchUpdate(boolean searchUpdate) {
        this.searchUpdate.set(searchUpdate);
    }
}
