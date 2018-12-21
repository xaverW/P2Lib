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
import de.p2tools.p2Lib.configFile.config.ConfigPData;
import de.p2tools.p2Lib.configFile.config.ConfigPDataList;
import de.p2tools.p2Lib.configFile.configList.ConfigList;
import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.log.PLog;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class LoadConfigFile implements AutoCloseable {

    private XMLInputFactory inFactory;
    private final Path xmlFilePath;

    private ArrayList<PDataList> pDataListArr = null;
    private ArrayList<PData> pDataArr = null;

    /**
     * @param filePath
     * @param configsListArrayDataList
     * @param pDataArr
     */
    LoadConfigFile(Path filePath, ArrayList<PDataList> configsListArrayDataList, ArrayList<PData> pDataArr) {
        this.xmlFilePath = filePath;
        this.pDataListArr = configsListArrayDataList;
        this.pDataArr = pDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    /**
     * @param filePath
     * @param pDataArr
     */
    LoadConfigFile(Path filePath, ArrayList<PData> pDataArr) {
        this.xmlFilePath = filePath;
        this.pDataArr = pDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    /**
     * @return
     */
    boolean readConfiguration() {
        boolean ret = false;

        if (!Files.exists(xmlFilePath)) {
            return ret;
        }

        XMLStreamReader parser = null;
        try (InputStream is = Files.newInputStream(xmlFilePath);
             InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {

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
            PLog.errorLog(732160795, ex);
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
        if (pDataListArr != null) {
            for (PDataList pDataList : pDataListArr) {
                if (pDataList.getTag().equals(xmlElem)) {
                    getConf(parser, pDataList);
                    return true;
                }
            }
        }

        if (pDataArr != null) {
            for (PData pData : pDataArr) {
                if (pData.getTag().equals(xmlElem)) {
                    getConf(parser, pData);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConf(XMLStreamReader parser, Object o) {
//        System.out.println("getConf: " + o.getClass());
        if (o instanceof PData) {
            return getPData(parser, (PData) o);

        } else if (o instanceof PDataList) {
            return getPDataList(parser, (PDataList) o);

        } else if (o instanceof ConfigPDataList) {
            PDataList<? extends PData> actValue = ((ConfigPDataList) o).getActValue();
            return getPDataList(parser, actValue);

        } else if (o instanceof ConfigPData) {
            PData cd = ((ConfigPData) o).getActValue();
            return getPData(parser, cd);

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
        final String configsListTagName = pDataList.getTag();

        try {
            PData pData = pDataList.getNewItem();
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(configsListTagName)) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                String s = parser.getLocalName();
                if (!pData.getTag().equals(s)) {
                    continue;
                }

                if (getConf(parser, pData)) {
                    ret = true;

//                    PDuration.counterStart("getPdataList");
                    pDataList.addNewItem(pData);
//                    PDuration.counterStop("getPdataList");
                    pData = pDataList.getNewItem();

                }

            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(302104541, ex);
        }

        return ret;
    }

    private boolean getPData(XMLStreamReader parser, PData pData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                final String localName = parser.getLocalName();
                for (Config config : pData.getConfigsArr()) {

//                    String key = config.getKey();
                    if (config.getKey().equals(localName)) {
                        getConf(parser, config);
                        break;
                    }

                }

            }
            ret = true;

        } catch (final Exception ex) {
            PLog.errorLog(302104541, ex);
        }

        return ret;
    }

    private boolean getConfigList(XMLStreamReader parser, ConfigList config) {
        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(config.getKey())) {
                    break;
                }
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

//                final String localName = parser.getLocalName();
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

    @Override
    public void close() throws Exception {
    }

}
