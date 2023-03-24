/*
 * MTViewer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2lib.mtfilm.readwritefilmlist;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2lib.mtdownload.MLHttpClient;
import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.FilmDataXml;
import de.p2tools.p2lib.mtfilm.film.Filmlist;
import de.p2tools.p2lib.mtfilm.film.FilmlistXml;
import de.p2tools.p2lib.mtfilm.loadfilmlist.ListenerFilmlistLoadEvent;
import de.p2tools.p2lib.mtfilm.loadfilmlist.ListenerLoadFilmlist;
import de.p2tools.p2lib.mtfilm.loadfilmlist.LoadFactory;
import de.p2tools.p2lib.mtfilm.tools.InputStreamProgressMonitor;
import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.mtfilm.tools.ProgressMonitorInputStream;
import de.p2tools.p2lib.tools.PStringUtils;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.application.Platform;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.tukaani.xz.XZInputStream;

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
import java.util.zip.ZipInputStream;

public class ReadFilmlist {

    private final int REDUCED_BANDWIDTH = 55;//ist ein Wert, der nicht eingestellt werden kann
    int sumFilms = 0;
    String channel = "", theme = "";
    private double progress = 0;
    private int countAll = 0;
    private boolean loadFromWeb = false;//nur dann müssen die Filme gefiltert werden

    private Map<String, Integer> filmsPerChannelFoundCompleteList = new TreeMap<>();
    private Map<String, Integer> filmsPerChannelUsed = new TreeMap<>();
    private Map<String, Integer> filmsPerChannelBlocked = new TreeMap<>();
    private Map<String, Integer> filmsPerDaysBlocked = new TreeMap<>();
    private Map<String, Integer> filmsPerDurationBlocked = new TreeMap<>();

    //Hier wird die Filmliste tatsächlich geladen: lokal von Datei, oder aus dem Web mit URL
    public void readFilmlistWebOrLocal(List<String> logList, final Filmlist filmlist, String sourceFileOrUrl) {
        PDuration.counterStart("readFilmlistWebOrLocal");

        countAll = 0;
        filmsPerChannelFoundCompleteList.clear();
        filmsPerChannelUsed.clear();
        filmsPerChannelBlocked.clear();
        filmsPerDaysBlocked.clear();
        filmsPerDurationBlocked.clear();

        logList.add("## " + PLog.LILNE2);
        try {
            notifyStart(); // für die Progressanzeige

            filmlist.clear();
            if (sourceFileOrUrl.startsWith("http")) {
                //dann aus dem Web mit der URL laden
                logList.add("## Filmliste aus URL laden: " + sourceFileOrUrl);
                logList.add("## FilmInit wird gemacht: " + LoadFactoryConst.filmInitNecessary);
                loadFromWeb = true;
                processFromWeb(new URL(sourceFileOrUrl), filmlist);

            } else {
                //dann lokale Datei laden
                logList.add("## Filmliste aus Datei laden: " + sourceFileOrUrl);
                logList.add("## FilmInit wird gemacht: " + LoadFactoryConst.filmInitNecessary);
                loadFromWeb = false;
                processFromFile(sourceFileOrUrl, filmlist);
            }

            if (LoadFactoryConst.loadFilmlist.isStop()) {
                logList.add("## -> Filmliste laden abgebrochen");
                filmlist.clear();

            } else {
                logList.add("##   Erstellt am:        " + filmlist.genDate());
                logList.add("##   Anzahl Gesamtliste: " + countAll);
                logList.add("##   Anzahl verwendet:   " + filmlist.size());
                countFoundChannel(logList, filmlist);
            }
        } catch (final MalformedURLException ex) {
            PLog.errorLog(945120201, ex);
        } catch (final Exception ex) {
            PLog.errorLog(965412378, ex);
        }
        logList.add("## " + PLog.LILNE2);
        notifyFinished();

        PDuration.counterStop("readFilmlistWebOrLocal");
    }

    /**
     * Download a process a filmliste from the web.
     *
     * @param source   source url as string
     * @param filmlist the list to read to
     */
    private void processFromWeb(URL source, Filmlist filmlist) {
        final Request.Builder builder = new Request.Builder().url(source);
        builder.addHeader("User-Agent", LoadFactoryConst.userAgent);

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

        try (Response response = MLHttpClient.getInstance().getHttpClient().newCall(builder.build()).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {

                try (InputStream input = new ProgressMonitorInputStream(body.byteStream(), body.contentLength(), monitor)) {
                    try (InputStream is = selectDecompressor(source.toString(), input);
                         JsonParser jp = new JsonFactory().createParser(is)) {
                        readData(jp, filmlist);
                    }
                }

            }
        } catch (final Exception ex) {
            PLog.errorLog(820147395, ex, "FilmListe: " + source);
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
        notifyProgress(ListenerLoadFilmlist.PROGRESS_INDETERMINATE);
        try (InputStream in = selectDecompressor(source, new FileInputStream(source));
             JsonParser jp = new JsonFactory().createParser(in)) {
            readData(jp, filmlist);
        } catch (final FileNotFoundException ex) {
            PLog.errorLog(894512369, "FilmListe existiert nicht: " + source);
            filmlist.clear();
        } catch (final Exception ex) {
            PLog.errorLog(945123641, ex, "FilmListe: " + source);
            filmlist.clear();
        }
    }

    private void readData(JsonParser jp, Filmlist filmlist) throws IOException {
        JsonToken jsonToken;
        ArrayList listChannel = LoadFactory.getSenderListNotToLoad();
        final long loadFilmsMaxMilliSeconds = getDaysLoadingFilms();
        final int loadFilmsMinDuration = LoadFactoryConst.SYSTEM_LOAD_FILMLIST_MIN_DURATION;
        final LoadFactoryConst.FilmChecker checker = LoadFactoryConst.checker;


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
        while (!LoadFactoryConst.loadFilmlist.isStop() && (jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }

            if (jp.isExpectedStartArrayToken()) {
                final FilmData film = filmlist.getNewElement();
                addValue(film, jp);
                if (LoadFactoryConst.filmInitNecessary) {
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

                    //und jetzt noch evt. gegen eine Blacklist prüfen
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

        PDuration.counterStart("countFoundChannel");
        if (!filmsPerChannelFoundCompleteList.isEmpty()) {
            logList.add("## " + PLog.LILNE3);
            logList.add("##");
            logList.add("## == Filme pro Sender in der Gesamtliste ==");

            sumFilms = 0;
            filmsPerChannelFoundCompleteList.keySet().stream().forEach(key -> {
                int found = filmsPerChannelFoundCompleteList.get(key);
                sumFilms += found;
                logList.add("## " + PStringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + PStringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("##");
        }

        if (sumFilms == filmlist.size()) {
            // dann werden alle gefunden Filme auch genommen
            // -> gibt keine "blocked"
            return;
        }

        if (!filmsPerChannelUsed.isEmpty()) {
            logList.add("## " + PLog.LILNE3);
            logList.add("##  ");
            logList.add("## == Filme pro Sender verwendet ==");

            sumFilms = 0;
            filmsPerChannelUsed.keySet().stream().forEach(key -> {
                int found = filmsPerChannelUsed.get(key);
                sumFilms += found;
                logList.add("## " + PStringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + PStringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerChannelBlocked.isEmpty()) {
            logList.add("## " + PLog.LILNE3);
            logList.add("## ");
            logList.add("## == nach Sender geblockte Filme ==");

            sumFilms = 0;
            filmsPerChannelBlocked.keySet().stream().forEach(key -> {
                int found = filmsPerChannelBlocked.get(key);
                sumFilms += found;
                logList.add("## " + PStringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + PStringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerDaysBlocked.isEmpty()) {
            logList.add("## " + PLog.LILNE3);
            logList.add("## ");
            final int maxDays = LoadFactoryConst.SYSTEM_LOAD_FILMLIST_MAX_DAYS;
            logList.add("## == nach max. Tage geblockte Filme (max. " + maxDays + " Tage) ==");

            sumFilms = 0;
            filmsPerDaysBlocked.keySet().stream().forEach(key -> {
                int found = filmsPerDaysBlocked.get(key);
                sumFilms += found;
                logList.add("## " + PStringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + PStringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }

        if (!filmsPerDurationBlocked.isEmpty()) {
            logList.add("## " + PLog.LILNE3);
            logList.add("## ");
            final int dur = LoadFactoryConst.SYSTEM_LOAD_FILMLIST_MIN_DURATION;
            logList.add("## == nach Filmlänge geblockte Filme (mind. " + dur + " min.) ==");

            sumFilms = 0;
            filmsPerDurationBlocked.keySet().stream().forEach(key -> {
                int found = filmsPerDurationBlocked.get(key);
                sumFilms += found;
                logList.add("## " + PStringUtils.increaseString(KEYSIZE, key) + ": " + found);
            });
            logList.add("## --");
            logList.add("## " + PStringUtils.increaseString(KEYSIZE, "=> Summe") + ": " + sumFilms);
            logList.add("## ");
        }
        PDuration.counterStop("countFoundChannel");
    }

    private InputStream selectDecompressor(String source, InputStream in) throws Exception {
        if (source.endsWith(LoadFactoryConst.FORMAT_XZ)) {
            in = new XZInputStream(in);
        } else if (source.endsWith(LoadFactoryConst.FORMAT_ZIP)) {
            final ZipInputStream zipInputStream = new ZipInputStream(in);
            zipInputStream.getNextEntry();
            in = zipInputStream;
        }
        return in;
    }

    private void addValue(FilmData film, JsonParser jp) throws IOException {
        for (int i = 0; i < ReadWriteFactory.MAX_JSON_NAMES; ++i) {
            String str = jp.nextTextValue();

            switch (i) {
                case ReadWriteFactory.JSON_NAMES_CHANNEL:
                    if (!str.isEmpty()) {
                        channel = str.intern();
                    }
                    film.arr[FilmDataXml.FILM_CHANNEL] = channel;
                    break;
                case ReadWriteFactory.JSON_NAMES_THEME:
                    if (!str.isEmpty()) {
                        theme = str.intern();
                    }
                    film.arr[FilmDataXml.FILM_THEME] = theme;
                    break;
                case ReadWriteFactory.JSON_NAMES_TITLE:
                    film.arr[FilmDataXml.FILM_TITLE] = str;
                    break;

                case ReadWriteFactory.JSON_NAMES_DATE:
                    film.arr[FilmDataXml.FILM_DATE] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_TIME:
                    film.arr[FilmDataXml.FILM_TIME] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_DURATION:
                    film.arr[FilmDataXml.FILM_DURATION] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_SIZE:
                    film.arr[FilmDataXml.FILM_SIZE] = str;
                    break;

                case ReadWriteFactory.JSON_NAMES_DESCRIPTION:
                    film.arr[FilmDataXml.FILM_DESCRIPTION] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_URL:
                    film.arr[FilmDataXml.FILM_URL] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_WEBSITE:
                    film.arr[FilmDataXml.FILM_WEBSITE] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_URL_SUBTITLE:
                    film.arr[FilmDataXml.FILM_URL_SUBTITLE] = str;
                    break;

                case ReadWriteFactory.JSON_NAMES_URL_SMALL:
                    film.arr[FilmDataXml.FILM_URL_SMALL] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_URL_HD:
                    film.arr[FilmDataXml.FILM_URL_HD] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_DATE_LONG:
                    film.arr[FilmDataXml.FILM_DATE_LONG] = str;
                    break;

                case ReadWriteFactory.JSON_NAMES_GEO:
                    film.arr[FilmDataXml.FILM_GEO] = str;
                    break;
                case ReadWriteFactory.JSON_NAMES_NEW:
                    film.arr[FilmDataXml.FILM_NEW] = str;
                    film.setNewFilm(Boolean.parseBoolean(str));
                    break;

                case ReadWriteFactory.JSON_NAMES_URL_RTMP:
                case ReadWriteFactory.JSON_NAMES_URL_RTMP_SMALL:
                case ReadWriteFactory.JSON_NAMES_URL_RTMP_HD:
                case ReadWriteFactory.JSON_NAMES_URL_HISTORY:
                    break;
            }
        }
    }

    private long getDaysLoadingFilms() {
        final long days = LoadFactoryConst.SYSTEM_LOAD_FILMLIST_MAX_DAYS;
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
            PLog.errorLog(495623014, ex);
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
            PLog.errorLog(495623014, ex);
        }
        return true;
    }

    private void notifyStart() {
        progress = 0;
        PLog.sysLog("Bandbreite zurücksetzen für das Laden der Filmliste von: " +
                LoadFactoryConst.downloadMaxBandwidth + " auf " + REDUCED_BANDWIDTH);
        //wird im GUI angezeigt!!
        Platform.runLater(() -> LoadFactoryConst.DOWNLOAD_MAX_BANDWIDTH_KBYTE.setValue(REDUCED_BANDWIDTH));

        LoadFactoryConst.loadFilmlist.setStart(
                new ListenerFilmlistLoadEvent("Filmliste laden", 0, 0, false));
    }

    private void notifyProgress(double iProgress) {
        progress = iProgress;
        if (progress > ListenerLoadFilmlist.PROGRESS_MAX) {
            progress = ListenerLoadFilmlist.PROGRESS_MAX;
        }
        LoadFactoryConst.loadFilmlist.setProgress(
                new ListenerFilmlistLoadEvent("Filmliste laden", progress, 0, false));
    }

    private void notifyFinished() {
        // reset download bandwidth
        PLog.sysLog("Bandbreite wieder herstellen: " + LoadFactoryConst.downloadMaxBandwidth);
        //dann die Bandbreite wieder herstellen, wird im GUI angezeigt!!
        Platform.runLater(() -> LoadFactoryConst.DOWNLOAD_MAX_BANDWIDTH_KBYTE.setValue(LoadFactoryConst.downloadMaxBandwidth));

        // Laden ist durch
        LoadFactoryConst.loadFilmlist.setLoaded(
                new ListenerFilmlistLoadEvent("Filme verarbeiten",
                        ListenerLoadFilmlist.PROGRESS_INDETERMINATE, 0, false/* Fehler */));
    }
}
