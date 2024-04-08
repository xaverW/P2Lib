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

package de.p2tools.p2lib.mtfilm.film;

import de.p2tools.p2lib.mtfilm.tools.LoadFactoryConst;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.function.Predicate;

public class Filmlist<T extends FilmData> extends SimpleListProperty<T> {

    public int nr = 1;
    public String[] metaData = new String[]{"", "", "", "", ""};
    public String[] sender = {""};
    int count = 0;
    int countDouble = 0;
    FilteredList<T> filteredList = null;
    SortedList<T> sortedList = null;

    public Filmlist() {
        super(FXCollections.observableArrayList());
    }

    public <T extends FilmData> FilmData getNewElement() {
        return new FilmData();
    }

    public static synchronized String genDate(String[] metaData) {
        // Tag, Zeit in lokaler Zeit wann die Filmliste erstellt wurde
        // in der Form "dd.MM.yyyy, HH:mm"
        return FilmlistFactory.genDate(metaData);
    }

    public void clearFilteredList() {
        initFilterdList();
        filteredList.clear();
        sortedList.clear();
    }

    public SortedList<? extends FilmData> getSortedList() {
        initFilterdList();
        return sortedList;
    }

    public FilteredList<? extends FilmData> getFilteredList() {
        initFilterdList();
        return filteredList;
    }

    private void initFilterdList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<T>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
    }

    public synchronized void filteredListSetPred(Predicate<FilmData> predicate) {
        P2Log.debugLog("=================> Filter: " + ++count);
        P2Duration.counterStart("FilmList.filteredListSetPred");
        getFilteredList().setPredicate(predicate);
        P2Duration.counterStop("FilmList.filteredListSetPred");
    }

    public String getFilmlistId() {
        return metaData[FilmlistXml.FILMLIST_ID_NR];
    }

    public synchronized boolean importFilmOnlyWithNr(T film) {
        // hier nur beim Laden aus einer fertigen Filmliste mit der GUI
        // die Filme sind schon sortiert, nur die Nummer muss noch ergänzt werden
        film.no = nr++;
        return add(film);
    }

    private void addHash(FilmData f, HashSet<String> hash, boolean index) {
        if (f.arr[FilmDataXml.FILM_CHANNEL].equals(LoadFactoryConst.KIKA)) {
            // beim KIKA ändern sich die URLs laufend
            hash.add(f.arr[FilmDataXml.FILM_THEME] + f.arr[FilmDataXml.FILM_TITLE]);
        } else if (index) {
            hash.add(f.getIndex());
        } else {
            hash.add(f.getUrlForHash());
        }
    }

    public synchronized void updateList(Filmlist addList,
                                        boolean index /* Vergleich über Index, sonst nur URL */,
                                        boolean replace) {
        // in eine vorhandene Liste soll eine andere Filmliste einsortiert werden
        // es werden nur Filme, die noch nicht vorhanden sind, einsortiert
        // "ersetzen": true: dann werden gleiche (index/URL) in der Liste durch neue ersetzt
        FilmlistFactory.updateList(this, addList, index, replace);
    }

    public synchronized void markGeoBlocked() {
        // geblockte Filme markieren
        this.parallelStream().forEach((FilmData f) -> f.setGeoBlocked());
    }

    public synchronized int markFilms() {
        // läuft direkt nach dem Laden der Filmliste!
        // doppelte Filme (URL), Geo, InFuture markieren
        // viele Filme sind bei mehreren Sendern vorhanden
        return FilmlistFactory.markFilms(this);
    }

    @Override
    public synchronized void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized void sort() {
        Collections.sort(this);
        // und jetzt noch die Nummerierung in Ordnung bringen
        int i = 1;
        for (final FilmData film : this) {
            film.no = i++;
        }
    }

    public synchronized void setMeta(Filmlist filmlist) {
        System.arraycopy(filmlist.metaData, 0, metaData, 0, FilmlistXml.MAX_ELEM);
    }

    public synchronized T getFilmByUrl(final String url) {
        final Optional<T> opt =
                parallelStream().filter(f -> f.arr[FilmDataXml.FILM_URL].equalsIgnoreCase(url)).findAny();
        return opt.orElse(null);
    }

    public synchronized void getTheme(String sender, LinkedList<String> list) {
        stream().filter(film -> film.arr[FilmDataXml.FILM_CHANNEL].equals(sender))
                .filter(film -> !list.contains(film.arr[FilmDataXml.FILM_THEME]))
                .forEach(film -> list.add(film.arr[FilmDataXml.FILM_THEME]));
    }

    public synchronized FilmData getFilmByUrl_small_high_hd(String url) {
        // Problem wegen gleicher URLs
        // wird versucht, einen Film mit einer kleinen/Hoher/HD-URL zu finden
        return parallelStream().filter(f ->

                f.arr[FilmDataXml.FILM_URL].equals(url) ||
                        f.getUrlForResolution(FilmData.RESOLUTION_HD).equals(url) ||
                        f.getUrlForResolution(FilmData.RESOLUTION_SMALL).equals(url)

        ).findFirst().orElse(null);
    }

    public synchronized String genDate() {
        return genDate(metaData);
    }

    /**
     * Get the age of the film list.
     *
     * @return Age in seconds.
     */
    public int getAge() {
        return FilmlistFactory.getAge(metaData);
    }

    /**
     * Get the age of the film list.
     *
     * @return Age as a {@link java.util.Date} object.
     */
    public static Date getAgeAsDate(String[] metaData) {
        return FilmlistFactory.getAgeAsDate(metaData);
    }

    /**
     * Get the age of the film list.
     *
     * @return Age as a {@link java.util.Date} object.
     */
    public static String getAgeAsStringDate(String[] metaData) {
        return FilmlistFactory.getAgeAsStringDate(metaData);
    }

    /**
     * Check if available Filmlist is older than a specified value.
     *
     * @return true if too old or if the list is empty.
     */
    public synchronized boolean isTooOldOrEmpty() {
        return FilmlistFactory.isTooOldOrEmpty(this, metaData);
    }

    /**
     * Check if Filmlist is too old for using a diff list.
     *
     * @return true if empty or too old.
     */
    public synchronized boolean isTooOldForDiffOrEmpty() {
        return FilmlistFactory.isTooOldForDiffOrEmpty(this, metaData);
    }

    /**
     * Check if list is older than specified parameter.
     *
     * @param second The age in seconds.
     * @return true if older.
     */
    public boolean isOlderThan(int second) {
        return FilmlistFactory.isOlderThan(metaData, second);
    }

    public synchronized long countNewFilms() {
        return stream().filter(FilmData::isNewFilm).count();
    }

    /**
     * Erstellt ein StringArray der Themen eines Senders oder wenn "sender" leer, aller Sender. Ist
     * für die Filterfelder in GuiFilme.
     */
    public synchronized void loadSender() {
        P2Duration.counterStart("loadSender");

        final LinkedHashSet<String> senderSet = new LinkedHashSet<>(21);
        // der erste Sender ist ""
        senderSet.add("");

        stream().forEach((film) -> senderSet.add(film.getChannel()));
        sender = senderSet.toArray(new String[senderSet.size()]);

        P2Duration.counterStop("loadSender");
//        loadUrls();
    }

//    public synchronized void loadUrls() {
//        PDuration.counterStart("Filmlist.loadUrls");
//
//        HashMap<String, Integer> urls = new HashMap<String, Integer>();
//        stream().forEach((film) -> {
//            final String u = film.getUrl();
//            String s = u.substring(0, u.indexOf("/")) + "  -  " + u.substring(u.lastIndexOf("."));
//            Integer i = urls.get(s);
//            if (i == null) {
//                urls.put(s, 1);
//            } else {
//                urls.replace(s, Integer.valueOf(++i));
//            }
//        });
//
//        System.out.println("====================================================");
//        System.out.println("====================================================");
//        System.out.println("====================================================");
//        urls.entrySet()
//                .stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue())
//                .forEach(e -> {
//                    String va = e.getValue() + "";
//                    while (va.length() < 8) {
//                        va = " " + va;
//                    }
//
//                    System.out.println(" value: " + va + "   -    " + "key: " + e.getKey());
//                });
//        System.out.println("====================================================");
//        System.out.println("====================================================");
//        System.out.println("====================================================");
//        PDuration.counterStop("Filmlist.loadUrls");
//    }
}
