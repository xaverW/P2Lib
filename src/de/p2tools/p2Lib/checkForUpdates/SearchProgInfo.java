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

import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchProgInfo {

    private final String UPDATE_SEARCH_TITLE = "Software-Aktualisierung";
    private final String UPDATE_ERROR_MESSAGE =
            "Es ist ein Fehler bei der Softwareaktualisierung aufgetreten.\n"
                    + "Die aktuelle Version konnte nicht ermittelt werden.";

    private static final int TIMEOUT = 10_000; // timeout ms
    private ProgInfo progInfo = new ProgInfo();
    private String searchUrl = "";
    private int lastInfoNr;
    ArrayList<Infos> newInfosList = new ArrayList<>(5);
    boolean newVersion = false;
    boolean newInfo = false;

    public ProgInfo checkUpdate(String searchUrl, int progVersion, IntegerProperty infoNr, boolean showProgInfo, boolean showError) {
        // prüft auf neue Version, aneigen: wenn true
        // showProgInfo-> dann wird die Info immer angezeigt
        this.searchUrl = searchUrl;
        this.lastInfoNr = infoNr.get();

        // Todo!!!!!!!!!!!!!!!!!!!!!!!!
        this.searchUrl = "http://p2.localhost:8080/extra/filerunner-info.xml";

        if (!retrieveProgramInformation(progInfo)) {
            progInfo = null;
        }


        if (progInfo == null && showError) {
            Platform.runLater(() ->
                    new PAlert().showErrorAlert("Fehler", UPDATE_SEARCH_TITLE, UPDATE_ERROR_MESSAGE));
            return null;
        }

        if (progInfo.getProgVersion() < 0 && showError) {
            // dann konnte die "Version" im xml nicht geparst werden
            Platform.runLater(() -> new PAlert().showErrorAlert("Fehler", UPDATE_SEARCH_TITLE, UPDATE_ERROR_MESSAGE));
            return null;
        }

        newVersion = progInfo.getProgVersion() > progVersion;

        for (Infos i : progInfo.getInfos()) {
            if (i.getInfoNr() > lastInfoNr) {
                newInfo = true;
                infoNr.setValue(i.getInfoNr());
                newInfosList.add(i);
            }
        }

        if (newVersion || newInfo || showProgInfo) {
            displayNotification();
        }

        return progInfo;
    }

    private void displayNotification() {
        Platform.runLater(() -> new InfoAlert(progInfo, newInfosList, newVersion).showInfoAlert("Programminfos",
                (newVersion ? "Neue Version verfügbar" : "Infos")));
    }

    /**
     * Load and parse the update information.
     *
     * @return parsed update info for further use when successful
     */
    private boolean retrieveProgramInformation(ProgInfo progInfo) {
        boolean ret;
        XMLStreamReader parser = null;

        final XMLInputFactory inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try (InputStreamReader inReader = new InputStreamReader(connectToServer(), StandardCharsets.UTF_8)) {

            parser = inFactory.createXMLStreamReader(inReader);
            ret = getConfig(parser, progInfo);

        } catch (final Exception ex) {
            Log.errorLog(951203214, ex);
            ret = false;
        } finally {
            try {
                if (parser != null) {
                    parser.close();
                }
            } catch (final Exception ignored) {
            }
        }

        return ret;
    }

    private InputStream connectToServer() throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL(searchUrl).openConnection();
        conn.setRequestProperty("User-Agent", PConst.userAgent);
        conn.setReadTimeout(TIMEOUT);
        conn.setConnectTimeout(TIMEOUT);

        return conn.getInputStream();
    }

    private boolean getConfig(XMLStreamReader parser, ProgInfo progInfo) {
        boolean ret = true;
        try {
            while (parser.hasNext()) {
                int event = parser.next();
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }
                if (event == XMLStreamConstants.END_ELEMENT) {
                    break;
                }
                switch (parser.getLocalName()) {
                    case ProgInfo.ParserTags.PROG_NAME:
                        progInfo.setProgName(parser.getElementText());
                        break;
                    case ProgInfo.ParserTags.PROG_URL:
                        progInfo.setProgUrl(parser.getElementText());
                        break;
                    case ProgInfo.ParserTags.PROG_DOWNLOAD_URL:
                        progInfo.setProgDownloadUrl(parser.getElementText());
                        break;
                    case ProgInfo.ParserTags.PROG_VERSION:
                        progInfo.setProgVersion(parser.getElementText());
                        break;
                    case ProgInfo.ParserTags.PROG_RELEASE_NOTES:
                        progInfo.setProgReleaseNotes(parser.getElementText());
                        break;
                    case ProgInfo.ParserTags.PROG_INFOS:

                        final int count = parser.getAttributeCount();
                        String nr = "";
                        for (int i = 0; i < count; ++i) {
                            if (parser.getAttributeName(i).toString().equals(ProgInfo.ParserTags.PROG_INFOS_NUMBER)) {
                                nr = parser.getAttributeValue(i);
                            }
                        }
                        final String info = parser.getElementText();
                        if (!nr.isEmpty() && !info.isEmpty()) {
                            progInfo.addProgInfo(new Infos(info, nr));
                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(645120302, ex);
        }
        return ret;
    }
}
