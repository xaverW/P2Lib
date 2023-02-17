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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_comment;
import de.p2tools.p2Lib.configFile.config.Config_pData;
import de.p2tools.p2Lib.configFile.config.Config_pDataList;
import de.p2tools.p2Lib.configFile.configList.ConfigList;
import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

class ConfigWrite {

    private final ConfigFile configFile;
    private OutputStreamWriter outputStreamWriter = null;
    private XMLStreamWriter xmlStreamWriter = null;

    ConfigWrite(ConfigFile configFile) {
        this.configFile = configFile;
    }

    synchronized boolean write(OutputStream outputStream) {
        try {
            xmlWriteStart(outputStream);
            xmlDataWrite();
            xmlWriteEnd();
            return true;
        } catch (final Exception ex) {
            PLog.errorLog(912014085, ex);
            return false;
        }
    }

    private void xmlWriteStart(OutputStream outputStream) throws XMLStreamException {
        PLog.sysLog("Start Schreiben nach: " + configFile.getFilePath());
        outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        xmlStreamWriter = outFactory.createXMLStreamWriter(outputStreamWriter);

        xmlStreamWriter.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
        xmlStreamWriter.writeStartElement(configFile.getXmlStart());
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
    }

    private void xmlDataWrite() throws XMLStreamException {
        for (PData pData : configFile.getpData()) {
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATORx2);
            xmlStreamWriter.writeComment(pData.getComment());
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
            write(pData, 0);
        }

        for (PDataList cl : configFile.getpDataList()) {
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
            xmlStreamWriter.writeComment(cl.getComment());
            write(cl, 0);
        }

        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATORx2);
    }

    private void xmlWriteEnd() throws XMLStreamException {
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        PLog.sysLog("geschrieben!");
    }


    private void write(Object o, int tab) throws XMLStreamException {
        //Standard-Daten
        if (o instanceof PData) {
            writePData((PData) o, tab);

        } else if (o instanceof PDataList) {
            writePDataList((PDataList) o, tab);

            //spezielle Configs
        } else if (o instanceof Config_pDataList) {
            writeConfigPDataList((Config_pDataList) o, tab);

        } else if (o instanceof Config_pData) {
            writeConfigPData((Config_pData) o, tab);

            //sind dann die configs zum Speichern der Daten
            //ab hier wird dann geschrieben
        } else if (o instanceof Config_comment) {
            writeComment((Config) o, tab);

        } else if (o instanceof ConfigList) {
            writeConfigList((ConfigList) o, tab);

        } else if (o instanceof Config) {
            writeConfig((Config) o, tab);
        } else {
            PLog.sysLog("Fehler beim Schreiben von: " + o.getClass().toString());
        }
    }

    private void writePData(PData pData, int tab) throws XMLStreamException {
        //String xmlName = pData.getTag();
        String xmlName = pData.getTag().split(PData.TAGGER)[0];

        writeTab(tab++);
        xmlStreamWriter.writeStartElement(xmlName);
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        for (Config config : pData.getConfigsArr()) {
            write(config, tab);
        }

        writeTab(--tab);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
    }


    private void writePDataList(PDataList pDataList, int tab) throws XMLStreamException {
        //String xmlName = pDataList.getTag();
        String xmlName = pDataList.getTag().split(PData.TAGGER)[0];

        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        writeTab(tab++);
        xmlStreamWriter.writeStartElement(xmlName);
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        for (Object configsData : pDataList) {
            write(configsData, tab);
        }

        writeTab(--tab);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
    }

    private void writeConfigPDataList(Config_pDataList configPDataList, int tab) throws XMLStreamException {
        PDataList<? extends PData> list = configPDataList.getActValue();
        writePDataList(list, tab);
    }

    private void writeConfigPData(Config_pData configPData, int tab) throws XMLStreamException {
        PData pData = configPData.getActValue();
        writePData(pData, tab);
    }

    private void writeConfigList(ConfigList config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValue().isEmpty()) {

            writeTab(tab++);
            //xmlStreamWriter.writeStartElement(config.getKey());
            xmlStreamWriter.writeStartElement(config.getKey().split(PData.TAGGER)[0]);
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

            ObservableList<Object> actValue = config.getActValue();
            int i = 0;
            for (Object o : actValue) {
                ++i;
                writeTab(tab);
                //xmlStreamWriter.writeStartElement(config.getKey() + "-" + i);
                xmlStreamWriter.writeStartElement(config.getKey().split(PData.TAGGER)[0] + "-" + i);
                xmlStreamWriter.writeCharacters(o.toString());
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
            }

            writeTab(--tab);
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
        }
    }

    private void writeComment(Config config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValueString().isEmpty()) {
            writeTab(tab);
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATORx2);
            xmlStreamWriter.writeComment("  " + config.getActValueString() + "  ");
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
        }
    }

    private void writeConfig(Config config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValueString().isEmpty()) {
            writeTab(tab);
            xmlStreamWriter.writeStartElement(config.getKey().split(PData.TAGGER)[0]);
            xmlStreamWriter.writeCharacters(config.getActValueString());
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
        }
    }

    private void writeTab(int tab) throws XMLStreamException {
        for (int t = 0; t < tab; ++t) {
            xmlStreamWriter.writeCharacters("\t"); //Tab
        }
    }
}
