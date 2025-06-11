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


package de.p2tools.p2lib.mediathek.film;

import de.p2tools.p2lib.mediathek.download.MtHttpClientProxy;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Objects;

public class SearchFilmlistUpdate {
    private final int COUNT_MAX = 60 * 15;
    private int countTimer = COUNT_MAX;//damit beim Programmstart schon mal gesucht wird
    private BooleanProperty foundNewList = new SimpleBooleanProperty(false);

    public SearchFilmlistUpdate() {
    }

    public boolean isFoundNewList() {
        return foundNewList.get();
    }

    public BooleanProperty foundNewListProperty() {
        return foundNewList;
    }

    public void setFoundNewList(boolean foundNewList) {
        this.foundNewList.set(foundNewList);
    }

    public boolean check(String id) {
        //auf jeden Fall suchen
        searchNewList(id);
        return foundNewList.getValue();
    }

    public void hasNewFilmlist(String id) {
        //nach Timer suchen, nur wenn noch keine neue gefunden
        if (foundNewList.getValue()) {
            //dann schon eine neue gefunden
            return;
        }

        ++countTimer;
        if (countTimer > COUNT_MAX) {
            countTimer = 0;
            //alle 15 Min.
            P2Log.sysLog("Gibt es eine neue Filmliste?");
            searchNewList(id);
        }
    }

    private void searchNewList(String id) {
        if (hasNewRemoteFilmlistID(id)) {
            P2Log.sysLog("Es gibt *eine* neue Filmliste");
            foundNewList.setValue(true);
        } else {
            P2Log.sysLog("Es gibt *keine* neue Filmliste");
            foundNewList.setValue(false);
        }
    }

    private boolean hasNewRemoteFilmlistID(String oldId) {
        boolean result = false;

        HttpUrl filmListUrl = HttpUrl.parse(P2LoadConst.FILMLIST_ID);
        final Request request = new Request.Builder().url(Objects.requireNonNull(filmListUrl)).build();

        try (Response response = MtHttpClientProxy.getInstance().getHttpClient().newCall(request).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {

                String res = body.string();
                if (!res.isEmpty() && !res.equalsIgnoreCase(oldId))
                    result = true; // we have an update...
            }
        } catch (final Exception ex) {
            P2Log.errorLog(895012478, ex);
        }

        return result;
    }
}
