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

package de.p2tools.p2lib.mediathek.audiolistload;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import de.p2tools.p2lib.mediathek.audio.P2AudioListFactory;
import de.p2tools.p2lib.mediathek.download.MtHttpClient;
import de.p2tools.p2lib.mediathek.film.FilmFactory;
import de.p2tools.p2lib.mediathek.filmdata.FilmData;
import de.p2tools.p2lib.mediathek.filmdata.Filmlist;
import de.p2tools.p2lib.mediathek.filmdata.FilmlistXml;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadFactory;
import de.p2tools.p2lib.mediathek.filmlistreadwrite.P2WriteFilmlistJson;
import de.p2tools.p2lib.mediathek.storedaudiolist.StoredAudioDataFactory;
import de.p2tools.p2lib.mediathek.tools.P2InputStreamProgressMonitor;
import de.p2tools.p2lib.mediathek.tools.P2ProgressMonitorInputStream;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Events;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SimpleTimeZone;

public class P2ReadWebAudioList {

    private final List<String> logList;
    private static int countDouble = 0;
    private HashSet<String> hashSet = new HashSet<>();
    public Filmlist audioListNew;

    public P2ReadWebAudioList(List<String> logList, Filmlist audioListNew) {
        this.logList = logList;
        this.audioListNew = audioListNew;
    }

    public boolean readWebList(String path) {
        boolean ret;
        P2Duration.counterStart("readWebList");

        try {
            // Hash füllen
            hashSet.addAll(((Filmlist<? extends FilmData>) P2LoadConst.audioListLocal)
                    .stream().map(FilmData::getUrlHistory).toList());
            hashSet.addAll(((Filmlist<? extends FilmData>) audioListNew).stream().map(FilmData::getUrlHistory).toList());

            P2LoadConst.audioListLocal.clear();
            audioListNew.clear();

            //dann aus dem Web mit der URL laden
            String url = StoredAudioDataFactory.getStoredAudioList();
            logList.add("## Audioliste aus URL laden: " + url);
            processFromWeb(new URL(url), audioListNew);

            if (audioListNew.isEmpty()) {
                // dann hats nicht geklappt
                ret = false;

            } else {
                setDateFromWeb();

                flattenDiacritic(logList, audioListNew);
                markNewFilms(logList, audioListNew);
                markDoubleAudios(logList, audioListNew);

                // und dann auch speichern
                logList.add("##");
                logList.add("## Audioliste schreiben (" + audioListNew.size() + " Audios) :");
                logList.add("##    --> Start Schreiben nach: " + path);
                new P2WriteFilmlistJson().write(path, audioListNew);
                logList.add("##    --> geschrieben!");
                logList.add("##");

                ret = true;
            }
        } catch (final Exception ex) {
            logList.add("##   Audioliste lesen hat nicht geklappt");
            P2Log.errorLog(645891204, ex);
            P2LoadConst.p2EventHandler.notifyListener(
                    new P2Event(P2Events.EVENT_AUDIO_LIST_LOAD_FINISHED, "Laden hat nicht geklappt", P2LoadAudioFactory.PROGRESS_INDETERMINATE));
            ret = false;
        }

        P2Duration.counterStop("readWebList");
        return ret;
    }

    private void setDateFromWeb() {
        // Datum setzen
        LocalDateTime date = P2AudioListFactory.getDate(audioListNew.metaData);
        String dateStr = P2LDateTimeFactory.toString(date, P2DateConst.DT_FORMATTER__FILMLIST);
        P2LoadConst.dateStoredAudiolist.set(dateStr);
    }

    private void processFromWeb(URL source, Filmlist audioList) {
        final Request.Builder builder = new Request.Builder().url(source);
        builder.addHeader("User-Agent", P2LoadConst.userAgent);

        // our progress monitor callback
        final P2InputStreamProgressMonitor monitor = new P2InputStreamProgressMonitor() {
            private int oldProgress = 0;

            @Override
            public void progress(long bytesRead, long size) {
                final int iProgress = (int) (bytesRead * 100/* zum Runden */ / size);
                if (iProgress != oldProgress) {
                    oldProgress = iProgress;
                }
            }
        };

        try (Response response = MtHttpClient.getInstance().getHttpClient().newCall(builder.build()).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {

                try (InputStream input = new P2ProgressMonitorInputStream(body.byteStream(), body.contentLength(), monitor)) {
                    try (InputStream is = P2LoadFactory.selectDecompressor(source.toString(), input);
                         JsonParser jp = new JsonFactory().createParser(is)) {
                        new P2ReadWebAudioToFilmListJson().readAudioData(jp, audioList);
                    }
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(820147395, ex, "FilmListe: " + source);
            audioList.clear();
        }

        // meta korrigieren
        changeMeta(audioList.metaData);
    }

    private static void changeMeta(String[] metaData) {
        // audiothek: "AudioList" : [ "22.05.2025 06:59:17", "22.05.2025 08:59:17" ]
        // audiolist: metaData = new String[]{"GMT", "LocalDate"}; // AudioDataXml.AUDIO_LIST_META_MAX_ELEM

        // mediathek: "Filmliste" : [ "22.05.2025, 10:32", "22.05.2025, 08:32", "3", "MSearch [Vers.: 3.1.255]", "a34202f7a2c9077f7b654e878c11a784" ],
        // FILMLIST_DATE_NR = 0;
        // FILMLIST_DATE_GMT_NR = 1;

        final String DATE_TIME_FORMAT_MEDIATHEK = "dd.MM.yyyy, HH:mm";
        final String DATE_TIME_FORMAT_AUDIOTHEK = "dd.MM.yyyy HH:mm:ss";
        try {
            final SimpleDateFormat sdf_audiothek = new SimpleDateFormat(DATE_TIME_FORMAT_AUDIOTHEK);

            String date = metaData[1];
            String dateGmt = metaData[0];
            Date filmDate = sdf_audiothek.parse(date);
            Date filmDateGmt = sdf_audiothek.parse(dateGmt);

            metaData[FilmlistXml.FILMLIST_DATE_GMT_NR] = FastDateFormat.getInstance(DATE_TIME_FORMAT_MEDIATHEK).format(filmDateGmt);
            metaData[FilmlistXml.FILMLIST_DATE_NR] = FastDateFormat.getInstance(DATE_TIME_FORMAT_MEDIATHEK).format(filmDate);
        } catch (Exception ex) {
            P2Log.errorLog(965874548, ex, "GenDateTime: Audiolist");

            SimpleDateFormat sdf_mediathek = new SimpleDateFormat(DATE_TIME_FORMAT_MEDIATHEK);
            SimpleDateFormat sdfUtf_mediathek = new SimpleDateFormat(DATE_TIME_FORMAT_MEDIATHEK);
            sdfUtf_mediathek.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            String d = sdf_mediathek.format(new Date());
            String dUtf = sdfUtf_mediathek.format(new Date());
            metaData[FilmlistXml.FILMLIST_DATE_NR] = d;
            metaData[FilmlistXml.FILMLIST_DATE_GMT_NR] = dUtf;
        }
    }

    private void flattenDiacritic(List<String> logList, Filmlist<? extends FilmData> audioList) {
        logList.add("## Diakritika setzen/ändern, Diakritika suchen");
        if (P2LoadConst.removeDiacritic) {
            FilmFactory.flattenDiacritic(audioList);
        } else {
            logList.add("## Diakritika: nicht gewollt");
        }
    }

    private void markNewFilms(List<String> logList, Filmlist<? extends FilmData> audioList) {
        logList.add("## neue Audios markieren");
        audioList.stream() //genauso schnell wie "parallel": ~90ms
                .peek(film -> film.setNewFilm(false))
                .filter(film -> !hashSet.contains(film.getUrl()))
                .forEach(film -> {
                    film.setNewFilm(true);
                });

        hashSet.clear();
    }

    public void markDoubleAudios(List<String> logList, Filmlist<? extends FilmData> audioList) {
        // läuft direkt nach dem Laden der Filmliste!
        // doppelte Filme (URL)
        P2Duration.counterStart("markDoubleAudios");
        logList.add("## doppelte Audios markieren");
        final HashSet<String> urlHashSet = new HashSet<>(audioList.size(), 0.75F);
        countDouble = 0;
        audioList.forEach((FilmData f) -> {
            if (!urlHashSet.add(f.getUrl())) {
                ++countDouble;
                f.setDoubleUrl(true);
            }
        });
        urlHashSet.clear();

        logList.add("## Anzahl doppelte: " + countDouble);
        if (P2LoadConst.SYSTEM_FILMLIST_REMOVE_DOUBLE) {
            // dann auch entfernen
            logList.add("## und entfernen");
            logList.add("## Anzahl: " + audioList.size());
            audioList.removeIf(FilmData::isDoubleUrl);
            logList.add("## Anzahl jetzt: " + audioList.size());
        }

        P2LoadConst.SYSTEM_AUDIOLIST_COUNT_DOUBLE.setValue(countDouble);
        P2Duration.counterStop("markDoubleAudios");
    }


}
