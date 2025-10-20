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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_comment;
import de.p2tools.p2lib.configfile.config.Config_pData;
import de.p2tools.p2lib.configfile.config.Config_pDataList;
import de.p2tools.p2lib.configfile.configlist.ConfigList;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2lib.tools.log.P2Log;
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
            P2Log.errorLog(912014085, ex);
            return false;
        }
    }

    private void xmlWriteStart(OutputStream outputStream) throws XMLStreamException {
        P2Log.sysLog("Start Schreiben nach: " + configFile.getFilePath());
        outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        xmlStreamWriter = outFactory.createXMLStreamWriter(outputStreamWriter);

        xmlStreamWriter.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
        xmlStreamWriter.writeStartElement(configFile.getXmlStart());
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
    }

    private void xmlDataWrite() throws XMLStreamException {
        for (P2Data p2Data : configFile.getpData()) {
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATORx2);
            xmlStreamWriter.writeComment(p2Data.getComment());
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
            write(p2Data, 0);
        }

        for (P2DataList cl : configFile.getpDataList()) {
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
        P2Log.sysLog("geschrieben!");
    }


    private void write(Object o, int tab) throws XMLStreamException {
        //Standard-Daten
        if (o instanceof P2Data) {
            writePData((P2Data) o, tab);

        } else if (o instanceof P2DataList) {
            writePDataList((P2DataList) o, tab);

            //spezielle Configs
        } else if (o instanceof Config_pDataList) {
            writePDataList(((Config_pDataList) o).getActValue(), tab);
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);

        } else if (o instanceof Config_pData) {
            writePData(((Config_pData) o).getActValue(), tab);
            //sind dann die configs zum Speichern der Daten
            //ab hier wird dann geschrieben
        } else if (o instanceof Config_comment) {
            writeComment((Config) o, tab);

        } else if (o instanceof ConfigList) {
            writeConfigList((ConfigList) o, tab);

        } else if (o instanceof Config) {
            writeConfig((Config) o, tab);
        } else {
            P2Log.sysLog("Fehler beim Schreiben von: " + o.getClass().toString());
        }
    }

    private void writePData(P2Data p2Data, int tab) throws XMLStreamException {
        //String xmlName = pdata.getTag();
        String xmlName = p2Data.getTag().split(P2Data.TAGGER)[0];

        writeTab(tab++);
        xmlStreamWriter.writeStartElement(xmlName);
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        for (Config config : p2Data.getConfigsArr()) {
            write(config, tab);
        }

        writeTab(--tab);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
    }


    private void writePDataList(P2DataList p2DataList, int tab) throws XMLStreamException {
        //String xmlName = pDataList.getTag();
        String xmlName = p2DataList.getTag().split(P2Data.TAGGER)[0];

        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        writeTab(tab++);
        xmlStreamWriter.writeStartElement(xmlName);
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

        for (Object configsData : p2DataList) {
            write(configsData, tab);
        }

        writeTab(--tab);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile
    }

//    private void writeConfigPDataList(Config_pDataList configPDataList, int tab) throws XMLStreamException {
//        P2DataList<? extends P2Data> list = configPDataList.getActValue();
//        writePDataList(configPDataList.getActValue(), tab);
//    }
//
//    private void writeConfigP2DataList(Config_p2DataList configP2DataList, int tab) throws XMLStreamException {
//        writePDataList(configP2DataList.getActValue(), tab);
//    }
//
//    private void writeConfigPData(Config_pData configPData, int tab) throws XMLStreamException {
//        P2Data p2Data = configPData.getActValue();
//        writePData(configPData.getActValue(), tab);
//    }

    private void writeConfigList(ConfigList config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValue().isEmpty()) {

            writeTab(tab++);
            //xmlStreamWriter.writeStartElement(config.getKey());
            xmlStreamWriter.writeStartElement(config.getKey().split(P2Data.TAGGER)[0]);
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR); //neue Zeile

            ObservableList<Object> actValue = config.getActValue();
            int i = 0;
            for (Object o : actValue) {
                ++i;
                writeTab(tab);
                //xmlStreamWriter.writeStartElement(config.getKey() + "-" + i);
                xmlStreamWriter.writeStartElement(config.getKey().split(P2Data.TAGGER)[0] + "-" + i);
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
        if (config.getActValue() == null) {
            return;
        }

        if (config.getActValueString().isEmpty()) {
            // dann nur eine Leerzeile
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);

        } else {
            // ein Kommentar
            writeTab(tab);
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
            xmlStreamWriter.writeComment("  " + config.getActValueString() + "  ");
            xmlStreamWriter.writeCharacters(P2LibConst.LINE_SEPARATOR);
        }
    }

    private void writeConfig(Config config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValueString().isEmpty()) {
            writeTab(tab);
            xmlStreamWriter.writeStartElement(config.getKey().split(P2Data.TAGGER)[0]);
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
