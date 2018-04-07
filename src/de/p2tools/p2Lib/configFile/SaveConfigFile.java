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
import de.p2tools.p2Lib.tools.log.SysMsg;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class SaveConfigFile implements AutoCloseable {

    private XMLStreamWriter writer = null;
    private OutputStreamWriter out = null;
    private Path xmlFilePath;
    private OutputStream os = null;

    private final String xmlStart;
    private final ArrayList<PDataList> pDataList;
    private final ArrayList<PData> pData;

    SaveConfigFile(String xmlStart, Path filePath, ArrayList<PDataList> pDataList, ArrayList<PData> pData) {
        xmlFilePath = filePath;
        this.xmlStart = xmlStart;
        this.pDataList = pDataList;
        this.pData = pData;
    }

    synchronized void write() {
        SysMsg.sysMsg("ProgData Schreiben nach: " + xmlFilePath.toString());
        xmlDatenSchreiben();
    }


    private void xmlDatenSchreiben() {
        try {
            xmlSchreibenStart();

            for (PData pData : this.pData) {

                writer.writeCharacters("\n\n");
                writer.writeComment(pData.getComment());
                writer.writeCharacters("\n");
                write(pData, 0);
            }

            for (PDataList cl : pDataList) {
                writer.writeCharacters("\n");
                writer.writeComment(cl.getComment());
                write(cl, 0);
            }

            writer.writeCharacters("\n\n");
            xmlSchreibenEnde();
        } catch (final Exception ex) {
            PLog.errorLog(656328109, ex);
        }
    }

    private void xmlSchreibenStart() throws IOException, XMLStreamException {
        SysMsg.sysMsg("Start Schreiben nach: " + xmlFilePath.toAbsolutePath());
        os = Files.newOutputStream(xmlFilePath);
        out = new OutputStreamWriter(os, StandardCharsets.UTF_8);

        final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        writer = outFactory.createXMLStreamWriter(out);
        writer.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement(xmlStart);
        writer.writeCharacters("\n");
    }

    private void xmlSchreibenEnde() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        SysMsg.sysMsg("geschrieben!");
    }

    @Override
    public void close() throws IOException, XMLStreamException {
        writer.close();
        out.close();
        os.close();
    }


    private void write(Object o, int tab) throws XMLStreamException {
        if (o instanceof PData) {
            writePData((PData) o, tab);

        } else if (o instanceof PDataList) {
            writePDataList((PDataList) o, tab);

        } else if (o instanceof ConfigPDataList) {
            writeConfigPDataList((ConfigPDataList) o, tab);

        } else if (o instanceof ConfigPData) {
            writeConfigPData((ConfigPData) o, tab);

        } else if (o instanceof ConfigList) {
            writeConfigList((ConfigList) o, tab);

        } else if (o instanceof Config) {
            writeConfig((Config) o, tab);
        } else {
            SysMsg.sysMsg("Fehler beim Schreiben von: " + o.getClass().toString());
        }
    }

    private void writePData(PData pData, int tab) throws XMLStreamException {

        String xmlName = pData.getTag();

        writeTab(tab++);
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        for (Config config : pData.getConfigsArr()) {
            write(config, tab);
        }

        writeTab(--tab);
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }


    private void writePDataList(PDataList pDataList, int tab) throws XMLStreamException {

        String xmlName = pDataList.getTag();
        writer.writeCharacters("\n"); // neue Zeile

        writeTab(tab++);
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        for (Object configsData : pDataList) {
            write(configsData, tab);
        }

        writeTab(--tab);
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }

    private void writeConfigPDataList(ConfigPDataList configPDataList, int tab) throws XMLStreamException {
        PDataList<? extends PData> list = configPDataList.getActValue();
        writePDataList(list, tab);
    }

    private void writeConfigPData(ConfigPData configPData, int tab) throws XMLStreamException {
        PData pData = configPData.getActValue();
        writePData(pData, tab);
    }

    private void writeConfigList(ConfigList config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValue().isEmpty()) {

            writeTab(tab++);
            writer.writeStartElement(config.getKey());
            writer.writeCharacters("\n"); // neue Zeile

            ObservableList<Object> actValue = config.getActValue();
            int i = 0;
            for (Object o : actValue) {
                ++i;
                writeTab(tab);
                writer.writeStartElement(config.getKey() + "-" + i);
                writer.writeCharacters(o.toString());
                writer.writeEndElement();
                writer.writeCharacters("\n"); // neue Zeile
            }

            writeTab(--tab);
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        }
    }

    private void writeConfig(Config config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValueString().isEmpty()) {
            writeTab(tab);
            writer.writeStartElement(config.getKey());
            writer.writeCharacters(config.getActValueString());
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        }
    }

    private void writeTab(int tab) throws XMLStreamException {
        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
    }
}
