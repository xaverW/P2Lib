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


package de.p2tools.p2lib.mtfilm.film;

import de.p2tools.p2lib.mtdownload.DownloadFactory;
import de.p2tools.p2lib.tools.DiacriticFactory;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.SimpleListProperty;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FilmFactory {
    final static String regEx1 = "[\\n\\r]";
    final static String regEx2 = "[\\p{Cc}&&[^\\t\\n\\r]]";
    private static Map<Character, Integer> counterMap = new HashMap<>(25);
    public static final String THEME_LIVE = "Livestream";
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

    public FilmFactory() {
    }

    public static String getSizeFromWeb(FilmData film, String url) {
        if (url.equals(film.arr[FilmDataXml.FILM_URL])) {
            return film.arr[FilmDataXml.FILM_SIZE];
        } else {
            return DownloadFactory.getContentLengthMB(url);
        }
    }

    public static void cleanFaultyCharacterFilmlist(SimpleListProperty<? extends FilmData> filmlist) {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da einen Java-Bug
        // https://github.com/javafxports/openjdk-jfx/issues/287

        PDuration.counterStart("cleanFaultyCharacterFilmlist");

        filmlist.stream().forEach(film -> {

            film.arr[FilmData.FILM_TITLE] = clean_1(film.getTitle(), true);
            film.arr[FilmData.FILM_THEME] = clean_1(film.getTheme(), true);
            film.setDescription(clean_1(film.getDescription(), false));

            film.arr[FilmData.FILM_TITLE] = clean_2(film.getTitle());
            film.arr[FilmData.FILM_THEME] = clean_2(film.getTheme());
            film.setDescription(clean_2(film.getDescription()));

            // U+3000 (12288)	　	Trenn- (Leer-) Zeichen	Whitespace	IDEOGRAPHIC SPACE	Ideographisches Leerzeichen
            // das hat die Probleme gemacht, Film: Weltbilder
        });

        for (Map.Entry<Character, Integer> entry : counterMap.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            PLog.sysLog("Key: " + (int) key + "  Key: " + key + "  Anz: " + value);
        }

        PDuration.counterStop("cleanFaultyCharacterFilmlist");
    }

    public static void flattenDiacritic(FilmData filmData) {
        //dann setzen
        //5,6s, 693MB ~15-20MB mehr durch die zusätzlichen Felder,
        //6,2s 829MB wenn alle Felder gesetzt würden

//        String s = DiacriticFactory.flattenDiacritic(filmData.getTitle());
//        if (!s.equals(filmData.getTitle())) {
//            filmData.arr[FilmData.FILM_TITLE] = s;
//        }
//        s = DiacriticFactory.flattenDiacritic(filmData.getTheme());
//        if (!s.equals(filmData.getTheme())) {
//            filmData.arr[FilmData.FILM_THEME] = s;
//        }
//        s = DiacriticFactory.flattenDiacritic(filmData.getDescription());
//        if (!s.equals(filmData.getDescription())) {
//            filmData.setDescription(s);
//        }


        filmData.arr[FilmData.FILM_TITLE] = DiacriticFactory.flattenDiacritic(filmData.getTitle());
        filmData.arr[FilmData.FILM_THEME] = DiacriticFactory.flattenDiacritic(filmData.getTheme());
        filmData.setDescription(DiacriticFactory.flattenDiacritic(filmData.getDescription()));
    }

    public static void flattenDiacritic(SimpleListProperty<? extends FilmData> filmlist) {
        //dann sollen die Diacritic *nicht* angezeigt werden!!
        PDuration.counterStart("flattenDiacritic");
        filmlist.stream().forEach(film -> {
            FilmFactory.flattenDiacritic(film);
        });
        PDuration.counterStop("flattenDiacritic");
    }

    public static String cleanUnicode(String ret) {
        return clean_1(ret, true);
    }

    private static String clean_1(String ret, boolean alsoNewLine) {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da eine Java-Bug
        // https://github.com/javafxports/openjdk-jfx/issues/287

        if (alsoNewLine) {
            ret = ret.replaceAll(regEx1, " ").replaceAll(regEx2, "");
        } else {
            ret = ret.replaceAll(regEx2, "");
        }

        return ret;
    }

    private static String clean_2(String test) {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da eine Java-Bug, auch Probleme bei Linux mit fehlenden Zeichen in den code tablen
        // https://github.com/javafxports/openjdk-jfx/issues/287

        char[] c = test.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if ((int) c[i] > 11263) { // der Wert ist jetzt einfach mal geschätzt und kommt ~ 20x vor
                counterMap.merge(c[i], 1, Integer::sum);
                c[i] = ' ';
                test = String.valueOf(c);
            }
        }

        return test;
    }
}
