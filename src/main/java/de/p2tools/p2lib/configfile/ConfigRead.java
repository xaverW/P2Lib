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

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_pData;
import de.p2tools.p2lib.configfile.config.Config_pDataList;
import de.p2tools.p2lib.configfile.configlist.ConfigList;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2lib.tools.log.P2Log;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStreamReader;

class ConfigRead implements AutoCloseable {

    private final ConfigFile configFile;
    private final XMLInputFactory inFactory;

    /**
     * @param configFile
     */
    ConfigRead(ConfigFile configFile) {
        this.configFile = configFile;
        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    boolean read(InputStreamReader in) {
        boolean ret;
        XMLStreamReader parser = null;
        try {
            parser = inFactory.createXMLStreamReader(in);

            nextTag:
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue nextTag;
                }

                String xmlElem = parser.getLocalName();
                if (get(parser, xmlElem)) {
                    continue nextTag;
                }
            }
            ret = true;

        } catch (final Exception ex) {
            ret = false;
            P2Log.errorLog(915263478, ex);
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

    private boolean get(XMLStreamReader parser, String xmlElem) {
        if (configFile.getpDataList() != null) {
            for (P2DataList p2DataList : configFile.getpDataList()) {

                //if (pDataList.getTag().equals(xmlElem)) {
                if (checkTag(p2DataList.getTag(), xmlElem)) {
                    getConf(parser, p2DataList);
                    return true;
                }
            }
        }

        if (configFile.getpData() != null) {
            for (P2Data p2Data : configFile.getpData()) {

                //if (pdata.getTag().equals(xmlElem)) {
                if (checkTag(p2Data.getTag(), xmlElem)) {
                    getConf(parser, p2Data);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConf(XMLStreamReader parser, Object o) {
        //Standard-Daten
        if (o instanceof P2Data) {
            return getPData(parser, (P2Data) o);

        } else if (o instanceof P2DataList) {
            return getPDataList(parser, (P2DataList) o);

            //spezielle configs
        } else if (o instanceof Config_pDataList) {
            P2DataList<? extends P2Data> actValue = ((Config_pDataList) o).getActValue();
            return getPDataList(parser, actValue);

        } else if (o instanceof Config_pData) {
            P2Data cd = ((Config_pData) o).getActValue();
            return getPData(parser, cd);

            //sind jetzt dann die configs zum Einlesen der Daten
        } else if (o instanceof ConfigList) {
            return getConfigList(parser, (ConfigList) o);

        } else if (o instanceof Config) {
            return getConfig(parser, (Config) o);

        } else {
            P2Log.sysLog("Fehler beim Lesen: " + o.getClass().toString());
            return false;
        }

    }

    private boolean getPDataList(XMLStreamReader parser, P2DataList p2DataList) {
        boolean ret = false;
        try {
            P2Data p2Data = p2DataList.getNewItem();
            while (parser.hasNext()) {
                final int event = parser.next();

                //if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(pDataList.getTag())) {
                if (event == XMLStreamConstants.END_ELEMENT && checkTag(p2DataList.getTag(), parser.getLocalName())) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                //if (!pdata.getTag().equals(parser.getLocalName())) {
                if (!checkTag(p2Data.getTag(), parser.getLocalName())) {
                    continue;
                }

                if (getConf(parser, p2Data)) {
                    ret = true;
                    p2DataList.addNewItem(p2Data);
                    p2Data = p2DataList.getNewItem();
                }
            }
        } catch (final Exception ex) {
            ret = false;
            P2Log.errorLog(975102305, ex);
        }
        return ret;
    }

    private boolean getPData(XMLStreamReader parser, P2Data p2Data) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();//von der "Umrandung"
        try {
            Config[] configs = p2Data.getConfigsArr();
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    //dann ist die "Umrandung" beendet
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    //Inhalt geht wieder mit einem Startelement los
                    continue;
                }

                //final String localName = parser.getLocalName();
                for (Config config : configs) {
                    //if (config.getKey().equals(localName)) {
                    if (checkTag(config.getKey(), parser.getLocalName())) {
                        getConf(parser, config);
                        break;
                    }
                }
            }
            ret = true;

        } catch (final Exception ex) {
            P2Log.errorLog(102365494, ex);
        }

        return ret;
    }

    private boolean getConfigList(XMLStreamReader parser, ConfigList config) {
        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                //if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(config.getKey())) {
                if (event == XMLStreamConstants.END_ELEMENT && checkTag(config.getKey(), parser.getLocalName())) {
                    //dann ist das Endelement der Liste
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    //kein Startelement des Inhalts der Liste
                    continue;
                }

                final String n = parser.getElementText();
                config.setActValue(n);
            }
        } catch (final Exception ex) {
            P2Log.errorLog(302104587, ex);
            return false;
        }

        return true;
    }

    private boolean getConfig(XMLStreamReader parser, Config config) {
        try {
            final String n = parser.getElementText();
            config.setActValue(n);
        } catch (XMLStreamException ex) {
            return false;
        }

        return true;
    }

    private boolean checkTag(String checkTag, String xmlElem) {
        for (String s : checkTag.split(P2Data.TAGGER)) {
            if (s.equals(xmlElem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() throws Exception {
    }
}
