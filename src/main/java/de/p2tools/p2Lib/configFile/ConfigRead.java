/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.configFile;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_pData;
import de.p2tools.p2Lib.configFile.config.Config_pDataList;
import de.p2tools.p2Lib.configFile.configList.ConfigList;
import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.log.PLog;

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
            PLog.errorLog(915263478, ex);
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
            for (PDataList pDataList : configFile.getpDataList()) {

                //if (pDataList.getTag().equals(xmlElem)) {
                if (checkTag(pDataList.getTag(), xmlElem)) {
                    getConf(parser, pDataList);
                    return true;
                }
            }
        }

        if (configFile.getpData() != null) {
            for (PData pData : configFile.getpData()) {

                //if (pData.getTag().equals(xmlElem)) {
                if (checkTag(pData.getTag(), xmlElem)) {
                    getConf(parser, pData);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConf(XMLStreamReader parser, Object o) {
        //Standard-Daten
        if (o instanceof PData) {
            return getPData(parser, (PData) o);

        } else if (o instanceof PDataList) {
            return getPDataList(parser, (PDataList) o);

            //spezielle configs
        } else if (o instanceof Config_pDataList) {
            PDataList<? extends PData> actValue = ((Config_pDataList) o).getActValue();
            return getPDataList(parser, actValue);

        } else if (o instanceof Config_pData) {
            PData cd = ((Config_pData) o).getActValue();
            return getPData(parser, cd);

            //sind jetzt dann die configs zum Einlesen der Daten
        } else if (o instanceof ConfigList) {
            return getConfigList(parser, (ConfigList) o);

        } else if (o instanceof Config) {
            return getConfig(parser, (Config) o);

        } else {
            PLog.sysLog("Fehler beim Lesen: " + o.getClass().toString());
            return false;
        }

    }

    private boolean getPDataList(XMLStreamReader parser, PDataList pDataList) {
        boolean ret = false;
        try {
            PData pData = pDataList.getNewItem();
            while (parser.hasNext()) {
                final int event = parser.next();

                //if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(pDataList.getTag())) {
                if (event == XMLStreamConstants.END_ELEMENT && checkTag(pDataList.getTag(), parser.getLocalName())) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                //if (!pData.getTag().equals(parser.getLocalName())) {
                if (!checkTag(pData.getTag(), parser.getLocalName())) {
                    continue;
                }

                if (getConf(parser, pData)) {
                    ret = true;
                    pDataList.addNewItem(pData);
                    pData = pDataList.getNewItem();
                }
            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(975102305, ex);
        }
        return ret;
    }

    private boolean getPData(XMLStreamReader parser, PData pData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();//von der "Umrandung"
        try {
            Config[] configs = pData.getConfigsArr();
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
            PLog.errorLog(102365494, ex);
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
            PLog.errorLog(302104587, ex);
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
        for (String s : checkTag.split(PData.TAGGER)) {
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
