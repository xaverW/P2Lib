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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2lib.mediathek.download.MLHttpClientProxy;
import de.p2tools.p2lib.mediathek.filmdata.FilmData;
import de.p2tools.p2lib.mediathek.filmdata.FilmDataXml;
import de.p2tools.p2lib.mediathek.filmdata.Filmlist;
import de.p2tools.p2lib.mediathek.filmdata.FilmlistXml;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadFactory;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadFilmlist;
import de.p2tools.p2lib.mediathek.tools.InputStreamProgressMonitor;
import de.p2tools.p2lib.mediathek.tools.ProgressMonitorInputStream;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2EventHandler;
import de.p2tools.p2lib.p2event.P2Events;
import de.p2tools.p2lib.tools.P2StringUtils;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class P2ReadFilmlist {

    int sumFilms = 0;
    String channel = "", theme = "";
    private double progress = 0;
    private int countAll = 0;
    private boolean loadFromWeb = false;//nur dann müssen die Filme gefiltert werden

    private final P2EventHandler p2EventHandler;
    private final int eventProcess;
    private final int eventLoaded;

    private final Map<String, Integer> filmsPerChannelFoundCompleteList = new TreeMap<>();
    private final Map<String, Integer> filmsPerChannelUsed = new TreeMap<>();
    private final Map<String, Integer> filmsPerChannelBlocked = new TreeMap<>();
    private final Map<String, Integer> filmsPerDaysBlocked = new TreeMap<>();
    private final Map<String, Integer> filmsPerDurationBlocked = new TreeMap<>();

    public P2ReadFilmlist() {
        this.p2EventHandler = P2LoadConst.p2EventHandler;
        this.eventProcess = P2Events.EVENT_FILMLIST_LOAD_PROGRESS;
        this.eventLoaded = P2Events.EVENT_FILMLIST_LOAD_LOADED;
    }

    public P2ReadFilmlist(boolean film) {
        this.p2EventHandler = P2LoadConst.p2EventHandler;
        if (film) {
            this.eventProcess = P2Events.EVENT_FILMLIST_LOAD_PROGRESS;
            this.eventLoaded = P2Events.EVENT_FILMLIST_LOAD_LOADED;

        } else {
            this.eventProcess = P2Events.LOAD_AUDIO_LIST_PROGRESS;
            this.eventLoaded = P2Events.LOAD_AUDIO_LIST_LOADED;
        }
    }

    //Hier wird die Filmliste tatsächlich geladen: lokal von Datei, oder aus dem Web mit URL
    public void readFilmlistWebOrLocal(List<String> logList, final Filmlist filmlist, String sourceFileOrUrl) {
        P2Duration.counterStart("readFilmlistWebOrLocal");

        countAll = 0;
        filmsPerChannelFoundCompleteList.clear();
        filmsPerChannelUsed.clear();
        filmsPerChannelBlocked.clear();
        filmsPerDaysBlocked.clear();
        filmsPerDurationBlocked.clear();

        logList.add("## " + P2Log.LILNE2);
        try {
            progress = 0; // für die Progressanzeige
            filmlist.clear();
            if (sourceFileOrUrl.startsWith("http")) {
                //dann aus dem Web mit der URL laden
                logList.add("## Filmliste aus URL laden: " + sourceFileOrUrl);
                logList.add("## FilmInit wird gemacht: " + P2LoadConst.filmInitNecessary);
                loadFromWeb = true;
                processFromWeb(new URL(sourceFileOrUrl), filmlist);

            } else {
                //dann lokale Datei laden
                logList.add("## Filmliste aus Datei laden: " + sourceFileOrUrl);
                logList.add("## FilmInit wird gemacht: " + P2LoadConst.filmInitNecessary);
                loadFromWeb = false;
                processFromFile(sourceFileOrUrl, filmlist);
            }

            if (P2LoadConst.stop.get()) {
                logList.add("## -> Filmliste laden abgebrochen");
                filmlist.clear();

            } else {
                logList.add("##   Erstellt am:        " + filmlist.genDate());
                logList.add("##   Anzahl Gesamtliste: " + countAll);
                logList.add("##   Anzahl verwendet:   " + filmlist.size());
                countFoundChannel(logList, filmlist);
            }
        } catch (final MalformedURLException ex) {
            P2Log.errorLog(945120201, ex);
        } catch (final Exception ex) {
            P2Log.errorLog(965412378, ex);
        }
        logList.add("## " + P2Log.LILNE2);
        notifyLoaded();

        P2Duration.counterStop("readFilmlistWebOrLocal");
    }

    /**
     * Download a process a filmliste from the web.
     *
     * @param source   source url as string
     * @param filmlist the list to read to
     */
    private void processFromWeb(URL source, Filmlist filmlist) {
        final Request.Builder builder = new Request.Builder().url(source);
        builder.addHeader("User-Agent", P2LoadConst.userAgent);

        // our progress monitor callback
        final InputStreamProgressMonitor monitor = new InputStreamProgressMonitor() {
            private int oldProgress = 0;

            @Override
            public void progress(long bytesRead, long size) {
                final int iProgress = (int) (bytesRead * 100/* zum Runden */ / size);
                if (iProgress != oldProgress) {
                    oldProgress = iProgress;
                    notifyProgress(1.0 * iProgress / 100);
                }
            }
        };

        try (Response response = MLHttpClientProxy.getInstance().getHttpClient().newCall(builder.build()).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {

                try (InputStream input = new ProgressMonitorInputStream(body.byteStream(), body.contentLength(), monitor)) {
                    try (InputStream is = P2LoadFactory.selectDecompressor(source.toString(), input);
                         JsonParser jp = new JsonFactory().createParser(is)) {
                        readData(jp, filmlist);
                    }
                }

            }
        } catch (final Exception ex) {
            P2Log.errorLog(820147395, ex, "FilmListe: " + source);
            filmlist.clear();
        }
    }

    /**
     * Read a locally available filmlist.
     *
     * @param source   file path as string
     * @param filmlist the list to read to
     */
    private void processFromFile(String source, Filmlist filmlist) {
        notifyProgress(P2LoadFilmlist.PROGRESS_INDETERMINATE);
        try (InputStream in = P2LoadFactory.selectDecompressor(source, new FileInputStream(source));
             JsonParser jp = new JsonFactory().createParser(in)) {
            readData(jp, filmlist);
        } catch (final FileNotFoundException ex) {
            P2Log.errorLog(894512369, "FilmListe existiert nicht: " + source);
            filmlist.clear();
        } catch (final Exception ex) {
            P2Log.errorLog(945123641, ex, "FilmListe: " + source);
            filmlist.clear();
        }
    }

    private void readData(JsonParser jp, Filmlist filmlist) throws IOException {
        JsonToken jsonToken;
        ArrayList<String> listChannel = P2LoadFactory.getSenderListNotToLoad();
        final long loadFilmsMaxMilliSeconds = getDaysLoadingFilms();
        final int loadFilmsMinDuration = P2LoadConst.SYSTEM_LOAD_FILMLIST_MIN_DURATION;
        final P2LoadConst.FilmChecker checker = P2LoadConst.checker;


        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected data to start with an Object");
        }

        while ((jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }
            if (jp.isExpectedStartArrayToken()) {
                for (int k = 0; k < FilmlistXml.MAX_ELEM; ++k) {
                    filmlist.metaData[k] = jp.nextTextValue();
                }
                break;
            }
        }
        while ((jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }
            if (jp.isExpectedStartArrayToken()) {
                // sind nur die Feldbeschreibungen, brauch mer nicht
                jp.nextToken();
                break;
            }
        }

        final boolean listChannelIsEmpty = listChannel.isEmpty();
        while (!P2LoadConst.stop.get() && (jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }

            if (jp.isExpectedStartArrayToken()) {
                final FilmData film = filmlist.getNewElement();
                addValue(film, jp);

                if (P2LoadConst.filmInitNecessary) {
                    //sonst muss eh die ganze Liste geladen werden und es wird dann nur die URL für den Hash gebraucht
                    ++countAll;
                    countFilm(filmsPerChannelFoundCompleteList, film);
                    film.init(); // damit wird auch das Datum! gesetzt
                }

                //=========================
                //Filter
                if (loadFromWeb) {
                    //und jetzt wird gefiltert, wenn aus dem Web, die lokale ist ja bereits gefiltert
                    //bringt aber nur ~5% Einsparung :(
                    if (film.arr[FilmDataXml.FILM_CHANNEL].equals("rbtv") ||
                            film.arr[FilmDataXml.FILM_CHANNEL].equals("Radio Bremen TV")) {
                        // die sind unterschiedlich geschrieben und alles RadioBremen
                        film.arr[FilmDataXml.FILM_CHANNEL] = "RBTV";
                    }

                    if (!listChannelIsEmpty && listChannel.contains(film.arr[FilmDataXml.FILM_CHANNEL])) {
                        //diesen Sender nicht laden
                        countFilm(filmsPerChannelBlocked, film);
                        continue;
                    }
                    if (loadFilmsMaxMilliSeconds > 0 && !checkDays(film, loadFilmsMaxMilliSeconds)) {
                        //wenn er zu alt ist, nicht laden
                        countFilm(filmsPerDaysBlocked, film);
                        continue;
                    }
                    if (loadFilmsMinDuration > 0 && !checkDuration(film, loadFilmsMinDuration)) {
                        //wenn er zu kurz ist, nicht laden
                        countFilm(filmsPerDurationBlocked, film);
                        continue;
                    }

                    //und jetzt noch evtl. gegen eine Blacklist prüfen
                    if (checker != null) {
                        if (checker.check(film)) {
                            continue;
                        }
                    }
                }

                countFilm(filmsPerChannelUsed, film);
                filmlist.importFilmOnlyWithNr(film);
            }
        }
    }

    private void countFilm(Map<String, Integer> map, FilmData film) {
        if (map.containsKey(film.arr[FilmData.FILM_CHANNEL])) {
            map.put(film.arr[FilmData.FILM_CHANNEL], 1 + map.get(film.arr[FilmData.FILM_CHANNEL]));
        } else {
            map.put(film.arr[FilmData.FILM_CHANNEL], 1);
        }
    }

    private void countFoundChannel(List<String> logList, Filmlist filmlist) {
        final int KEYSIZE = 12;

        P2Duration.counterStart("countFoundChannel");
        if (!filmsPerChannelFoundCompleteList.isEmpty()) {
            logList.add("## " + P2Log.LILNE3);
            logList.add("##");
            logList.add("## == Filme pro Sender in der Gesamtliste ==");

            sumFilms = 0;
            filmsPerChannelFoundCompleteList.keySet().forEach(key -> {
                int found = filmsPerChannelFoundCompleteList.get(key);
                sumFilms += found;
                logList.add("## " + P2StringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + P2StringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("##");
        }

        if (sumFilms == filmlist.size()) {
            // dann werden alle gefunden Filme auch genommen
            // -> gibt keine "blocked"
            return;
        }

        if (!filmsPerChannelUsed.isEmpty()) {
            logList.add("## " + P2Log.LILNE3);
            logList.add("##  ");
            logList.add("## == Filme pro Sender verwendet ==");

            sumFilms = 0;
            filmsPerChannelUsed.keySet().forEach(key -> {
                int found = filmsPerChannelUsed.get(key);
                sumFilms += found;
                logList.add("## " + P2StringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + P2StringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerChannelBlocked.isEmpty()) {
            logList.add("## " + P2Log.LILNE3);
            logList.add("## ");
            logList.add("## == nach Sender geblockte Filme ==");

            sumFilms = 0;
            filmsPerChannelBlocked.keySet().forEach(key -> {
                int found = filmsPerChannelBlocked.get(key);
                sumFilms += found;
                logList.add("## " + P2StringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + P2StringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerDaysBlocked.isEmpty()) {
            logList.add("## " + P2Log.LILNE3);
            logList.add("## ");
            final int maxDays = P2LoadConst.SYSTEM_LOAD_FILMLIST_MAX_DAYS;
            logList.add("## == nach max. Tage geblockte Filme (max. " + maxDays + " Tage) ==");

            sumFilms = 0;
            filmsPerDaysBlocked.keySet().forEach(key -> {
                int found = filmsPerDaysBlocked.get(key);
                sumFilms += found;
                logList.add("## " + P2StringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + P2StringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerDurationBlocked.isEmpty()) {
            logList.add("## " + P2Log.LILNE3);
            logList.add("## ");
            final int dur = P2LoadConst.SYSTEM_LOAD_FILMLIST_MIN_DURATION;
            logList.add("## == nach Filmlänge geblockte Filme (mind. " + dur + " min.) ==");

            sumFilms = 0;
            filmsPerDurationBlocked.keySet().forEach(key -> {
                int found = filmsPerDurationBlocked.get(key);
                sumFilms += found;
                logList.add("## " + P2StringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + P2StringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }
        P2Duration.counterStop("countFoundChannel");
    }

    private void addValue(FilmData film, JsonParser jp) throws IOException {
        for (int i = 0; i < P2ReadWriteFactory.MAX_JSON_NAMES; ++i) {
            String str = jp.nextTextValue();

            switch (i) {
                case P2ReadWriteFactory.JSON_NAMES_CHANNEL:
                    if (!str.isEmpty()) {
                        channel = str.intern();
                    }
                    film.arr[FilmDataXml.FILM_CHANNEL] = channel;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_THEME:
                    if (!str.isEmpty()) {
                        theme = str.intern();
                    }
                    film.arr[FilmDataXml.FILM_THEME] = theme;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_TITLE:
                    film.arr[FilmDataXml.FILM_TITLE] = str;
                    break;

                case P2ReadWriteFactory.JSON_NAMES_DATE:
                    film.arr[FilmDataXml.FILM_DATE] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_TIME:
                    film.arr[FilmDataXml.FILM_TIME] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_DURATION:
                    film.arr[FilmDataXml.FILM_DURATION] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_SIZE:
                    film.arr[FilmDataXml.FILM_SIZE] = str;
                    break;

                case P2ReadWriteFactory.JSON_NAMES_DESCRIPTION:
                    film.arr[FilmDataXml.FILM_DESCRIPTION] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_URL:
                    film.arr[FilmDataXml.FILM_URL] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_WEBSITE:
                    film.arr[FilmDataXml.FILM_WEBSITE] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_URL_SUBTITLE:
                    film.arr[FilmDataXml.FILM_URL_SUBTITLE] = str;
                    break;

                case P2ReadWriteFactory.JSON_NAMES_URL_SMALL:
                    film.arr[FilmDataXml.FILM_URL_SMALL] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_URL_HD:
                    film.arr[FilmDataXml.FILM_URL_HD] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_DATE_LONG:
                    film.arr[FilmDataXml.FILM_DATE_LONG] = str;
                    break;

                case P2ReadWriteFactory.JSON_NAMES_GEO:
                    film.arr[FilmDataXml.FILM_GEO] = str;
                    break;
                case P2ReadWriteFactory.JSON_NAMES_NEW:
                    film.arr[FilmDataXml.FILM_NEW] = str;
                    film.setNewFilm(Boolean.parseBoolean(str));
                    break;

                case P2ReadWriteFactory.JSON_NAMES_URL_RTMP:
                case P2ReadWriteFactory.JSON_NAMES_URL_RTMP_SMALL:
                case P2ReadWriteFactory.JSON_NAMES_URL_RTMP_HD:
                case P2ReadWriteFactory.JSON_NAMES_URL_HISTORY:
                    break;
            }
        }
    }

    private long getDaysLoadingFilms() {
        final long days = P2LoadConst.SYSTEM_LOAD_FILMLIST_MAX_DAYS;
        if (days > 0) {
            return System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
        } else {
            return 0;
        }
    }

    private boolean checkDays(FilmData film, long loadFilmsLastMilliSeconds) {
        // true, wenn der Film angezeigt werden kann!
        try {
            if (film.filmDate.getTime() != 0) {
                if (film.filmDate.getTime() < loadFilmsLastMilliSeconds) {
                    //dann ist er zu alt
                    return false;
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(495623014, ex);
        }
        return true;
    }

    private boolean checkDuration(FilmData film, int loadFilmsMinDuration) {
        //true, wenn der Film angezeigt werden kann!
        try {
            if (film.getDurationMinute() != 0) {
                if (film.getDurationMinute() < loadFilmsMinDuration) {
                    //dann ist er zu kurz
                    return false;
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(495623014, ex);
        }
        return true;
    }

    private void notifyProgress(double iProgress) {
        progress = iProgress;
        if (progress > P2LoadFilmlist.PROGRESS_MAX) {
            progress = P2LoadFilmlist.PROGRESS_MAX;
        }
        p2EventHandler.notifyListener(new P2Event(eventProcess, "Filmliste laden", progress));
    }

    private void notifyLoaded() {
        // Laden ist durch
        p2EventHandler.notifyListener(new P2Event(eventLoaded, "Filme verarbeiten",
                P2LoadFilmlist.PROGRESS_INDETERMINATE));
    }
}
