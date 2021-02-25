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
import de.p2tools.p2Lib.configFile.pData.PDataMap;
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

class LoadConfig implements AutoCloseable {

    private XMLInputFactory inFactory;
    private final Path xmlFilePath;

    private ArrayList<PDataList> pDataListArr = null;
    private ArrayList<PData> pDataArr = null;

    /**
     * @param filePath
     * @param configsListArrayDataList
     * @param pDataArr
     */
    LoadConfig(Path filePath, ArrayList<PDataList> configsListArrayDataList, ArrayList<PData> pDataArr) {
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
    LoadConfig(Path filePath, ArrayList<PData> pDataArr) {
        this.xmlFilePath = filePath;
        this.pDataArr = pDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    /**
     * @return
     */
    boolean readConfiguration(InputStream inputStream) {
        boolean ret;

        try {
            InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            ret = read(in);

        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(963258967, ex);
        }

        return ret;
    }

    /**
     * @return
     */
    boolean readConfiguration() {
        if (!Files.exists(xmlFilePath)) {
            return false;
        }

        boolean ret = false;
        try (InputStream is = Files.newInputStream(xmlFilePath);
             InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            ret = read(in);

        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(454102598, ex);
        }

        return ret;
    }

    /**
     * @return
     */
    boolean read(InputStreamReader in) {
        boolean ret = false;

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

        } else if (o instanceof PDataMap) {
            return getPDataMap(parser, (PDataMap) o);

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
//        System.out.println("getPDataList - Tag: " + configsListTagName);

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

    private boolean getPDataMap(XMLStreamReader parser, PDataMap pDataMap) {
        boolean ret = false;
        final String configsListTagName = pDataMap.getTag();

        try {
            PData pData = pDataMap.getNewItem();
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
                    pDataMap.addNewItem(pData);
                    pData = pDataMap.getNewItem();

                }

            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(701045289, ex);
        }

        return ret;
    }

    private boolean getPData(XMLStreamReader parser, PData pData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        try {
            Config[] configs = pData.getConfigsArr();
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                final String localName = parser.getLocalName();
                for (Config config : configs) {

//                    String key = config.getKey();
                    if (config.getKey().equals(localName)) {
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
