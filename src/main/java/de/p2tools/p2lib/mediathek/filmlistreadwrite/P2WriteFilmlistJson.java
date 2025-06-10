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

package de.p2tools.p2lib.mediathek.filmlistreadwrite;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.mediathek.filmdata.FilmData;
import de.p2tools.p2lib.mediathek.filmdata.FilmDataXml;
import de.p2tools.p2lib.mediathek.filmdata.Filmlist;
import de.p2tools.p2lib.mediathek.filmdata.FilmlistXml;
import de.p2tools.p2lib.tools.log.P2Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class P2WriteFilmlistJson {

    public void write(String file, Filmlist<? extends FilmData> filmlist) {
        String sender = "", theme = "";

        try (FileOutputStream fos = new FileOutputStream(file);
             JsonGenerator jg = getJsonGenerator(fos)) {

            jg.writeStartObject();

            //=======================================
            // Infos zur Filmliste
            jg.writeArrayFieldStart(FilmlistXml.FILMLIST);
            for (int i = 0; i < FilmlistXml.MAX_ELEM; ++i) {
                jg.writeString(filmlist.metaData[i]);
            }
            jg.writeEndArray();

            //=======================================
            // Infos der Felder in der Filmliste
            jg.writeArrayFieldStart(FilmlistXml.FILMLIST);
            for (int i = 0; i < P2ReadWriteFactory.JSON_NAMES.length; ++i) {
                jg.writeString(P2ReadWriteFactory.JSON_NAMES[i]);
            }
            jg.writeEndArray();

            //=======================================
            //Filme schreiben
            for (FilmData film : filmlist) {
                film.arr[FilmDataXml.FILM_NEW] = Boolean.toString(film.isNewFilm()); // damit wirs beim nächsten Programmstart noch wissen

                jg.writeArrayFieldStart(P2ReadWriteFactory.TAG_JSON_LIST);
                for (int i = 0; i < P2ReadWriteFactory.MAX_JSON_NAMES; ++i) {
                    switch (i) {
                        case P2ReadWriteFactory.JSON_NAMES_CHANNEL:
                            if (film.arr[FilmDataXml.FILM_CHANNEL].equals(sender)) {
                                jg.writeString("");
                            } else {
                                sender = film.arr[FilmDataXml.FILM_CHANNEL];
                                jg.writeString(film.arr[FilmDataXml.FILM_CHANNEL]);
                            }
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_THEME:
                            if (film.arr[FilmDataXml.FILM_THEME].equals(theme)) {
                                jg.writeString("");
                            } else {
                                theme = film.arr[FilmDataXml.FILM_THEME];
                                jg.writeString(film.arr[FilmDataXml.FILM_THEME]);
                            }
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_TITLE:
                            jg.writeString(film.arr[FilmDataXml.FILM_TITLE]);
                            break;


                        case P2ReadWriteFactory.JSON_NAMES_DATE:
                            jg.writeString(film.arr[FilmDataXml.FILM_DATE]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_TIME:
                            jg.writeString(film.arr[FilmDataXml.FILM_TIME]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_DURATION:
                            jg.writeString(film.arr[FilmDataXml.FILM_DURATION]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_SIZE:
                            jg.writeString(film.arr[FilmDataXml.FILM_SIZE]);
                            break;


                        case P2ReadWriteFactory.JSON_NAMES_DESCRIPTION:
                            jg.writeString(film.arr[FilmDataXml.FILM_DESCRIPTION]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_URL:
                            jg.writeString(film.arr[FilmDataXml.FILM_URL]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_WEBSITE:
                            jg.writeString(film.arr[FilmDataXml.FILM_WEBSITE]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_URL_SUBTITLE:
                            jg.writeString(film.arr[FilmDataXml.FILM_URL_SUBTITLE]);
                            break;


                        case P2ReadWriteFactory.JSON_NAMES_URL_SMALL:
                            jg.writeString(film.arr[FilmDataXml.FILM_URL_SMALL]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_URL_HD:
                            jg.writeString(film.arr[FilmDataXml.FILM_URL_HD]);
                            break;


                        case P2ReadWriteFactory.JSON_NAMES_DATE_LONG:
                            jg.writeString(film.arr[FilmDataXml.FILM_DATE_LONG]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_GEO:
                            jg.writeString(film.arr[FilmDataXml.FILM_GEO]);
                            break;
                        case P2ReadWriteFactory.JSON_NAMES_NEW:
                            jg.writeString(film.arr[FilmDataXml.FILM_NEW]);
                            break;


                        case P2ReadWriteFactory.JSON_NAMES_URL_RTMP_SMALL:
                        case P2ReadWriteFactory.JSON_NAMES_URL_RTMP:
                        case P2ReadWriteFactory.JSON_NAMES_URL_RTMP_HD:
                        case P2ReadWriteFactory.JSON_NAMES_URL_HISTORY:
                            jg.writeString("");
                            break;
                    }
                }
                jg.writeEndArray();
            }
            jg.writeEndObject();
        } catch (Exception ex) {
            P2Log.errorLog(846930145, ex, "nach: " + file);
        }
    }

    private JsonGenerator getJsonGenerator(OutputStream os) throws IOException {
        JsonFactory jsonF = new JsonFactory();
        JsonGenerator jg = jsonF.createGenerator(os, JsonEncoding.UTF8);
        if (P2LibConst.debug) {
            jg.useDefaultPrettyPrinter(); // enable indentation just to make debug/testing easier
        }

        return jg;
    }
}