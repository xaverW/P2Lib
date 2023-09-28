/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.PLog;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RetrieveProgUpdateData {

    private static final int TIMEOUT = 10_000; // timeout ms

    private RetrieveProgUpdateData() {
    }


    /**
     * Load and parse the update information.
     *
     * @return parsed update info for further use when successful
     */
    public static boolean retrieveProgramInformation(ProgUpdateData progUpdateData, String searchUrl) {
        boolean ret;
        XMLStreamReader parser = null;

        final XMLInputFactory inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try (InputStreamReader inReader = new InputStreamReader(connectToServer(searchUrl), StandardCharsets.UTF_8)) {

            parser = inFactory.createXMLStreamReader(inReader);
            ret = getConfig(parser, progUpdateData);

        } catch (final Exception ex) {
            PLog.errorLog(951203214, ex);
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

    private static InputStream connectToServer(String searchUrl) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL(searchUrl).openConnection();
        conn.setRequestProperty("User-Agent", P2LibConst.userAgent);
        conn.setReadTimeout(TIMEOUT);
        conn.setConnectTimeout(TIMEOUT);

        return conn.getInputStream();
    }

    private static boolean getConfig(XMLStreamReader parser, ProgUpdateData progUpdateData) {
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
                    case ProgUpdateData.ParserTags.PROG_NAME:
                        progUpdateData.setProgName(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_URL:
                        progUpdateData.setProgUrl(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_DOWNLOAD_URL:
                        progUpdateData.setProgDownloadUrl(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_VERSION:
                        progUpdateData.setProgVersion(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_BUILD_NO:
                        progUpdateData.setProgBuildNo(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_BUILD_DATE:
                        progUpdateData.setProgBuildDate(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_RELEASE_NOTES:
                        progUpdateData.setProgReleaseNotes(parser.getElementText());
                        break;
                    case ProgUpdateData.ParserTags.PROG_INFOS:

                        final int count = parser.getAttributeCount();
                        String no = "";
                        for (int i = 0; i < count; ++i) {
                            if (parser.getAttributeName(i).toString().equals(ProgUpdateData.ParserTags.PROG_INFOS_NUMBER)) {
                                no = parser.getAttributeValue(i);
                            }
                        }
                        final String info = parser.getElementText();
                        if (!no.isEmpty() && !info.isEmpty()) {
                            progUpdateData.addProgInfo(new ProgUpdateInfoData(info, no));
                        }
                        break;
                    case ProgUpdateData.ParserTags.PROG_DOWNLOAD:
                        progUpdateData.addDownloads(parser.getElementText());
                        break;

                    default:
                        break;
                }
            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(645120302, ex);
        }
        return ret;
    }
}
