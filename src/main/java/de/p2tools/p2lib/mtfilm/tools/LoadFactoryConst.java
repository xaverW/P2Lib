/*
 * P2Tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.mtfilm.tools;

import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.Filmlist;
import de.p2tools.p2lib.mtfilm.loadfilmlist.LoadFilmlist;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;

public class LoadFactoryConst {
    public static final String DREISAT = "3Sat";
    public static final String ARD = "ARD";

    public static final String ARTE_DE = "ARTE.DE";
    public static final String ARTE_EN = "ARTE.EN";
    public static final String ARTE_ES = "ARTE.ES";
    public static final String ARTE_FR = "ARTE.FR";
    public static final String ARTE_IT = "ARTE.IT";
    public static final String ARTE_PL = "ARTE.PL";

    public static final String BR = "BR";
    public static final String DW = "DW";
    public static final String FUNK_NET = "Funk.net";

    public static final String HR = "HR";
    public static final String KIKA = "KiKA";
    public static final String MDR = "MDR";
    public static final String NDR = "NDR";
    public static final String ORF = "ORF";
    public static final String PHOENIX = "PHOENIX";
    public static final String RADIO_BREMEN_TV = "Radio Bremen TV";
    public static final String RBB = "RBB";
    public static final String RBB_TV = "rbtv";

    public static final String SR = "SR";
    public static final String SRF = "SRF";
    public static final String SWR = "SWR";
    public static final String WDR = "WDR";
    public static final String ZDF = "ZDF";
    public static final String ZDF_TIVI = "ZDF-tivi";

    public static final String[] SENDER = {DREISAT, ARD,
            ARTE_DE, ARTE_EN, ARTE_ES, ARTE_FR, ARTE_IT, ARTE_PL,
            BR, DW, FUNK_NET, HR, KIKA, MDR, NDR, ORF, PHOENIX, RADIO_BREMEN_TV, RBB, RBB_TV,
            SR, SRF, SWR, WDR, ZDF, ZDF_TIVI};


    // beim Programmstart wird die Liste geladen wenn sie älter ist als ..
    public static final int ALTER_FILMLISTE_SEKUNDEN_FUER_AUTOUPDATE = 4 * 60 * 60;
    // Uhrzeit ab der die Diffliste alle Änderungen abdeckt, die Filmliste darf also nicht vor xx erstellt worden sein
    public static final String TIME_MAX_AGE_FOR_DIFF = "09:00:00";
    public static final String FORMAT_ZIP = ".zip";
    public static final String FORMAT_XZ = ".xz";
    // MediathekView URLs
    public static String FILMLIST_URL_AKT = "https://liste.mediathekview.de/Filmliste-akt.xz";
    public static String FILMLIST_URL_DIFF = "https://liste.mediathekview.de/Filmliste-diff.xz";
    public static String FILMLIST_ID = "https://liste.mediathekview.de/filmliste.id";
    public static String GEO_HOME_PLACE = "";
    public static boolean debug = false;
    public static String SYSTEM_LOAD_NOT_SENDER = "";
    public static String dateStoredFilmlist = "";
    public static IntegerProperty DOWNLOAD_MAX_BANDWIDTH_KBYTE = new SimpleIntegerProperty(0);//damit wird die Downloadbandbreite reduziert
    public static int downloadMaxBandwidth = 0;//ist der Wert beim DownloadStart
    public static boolean firstProgramStart = false;
    public static String localFilmListFile = "";
    public static boolean loadNewFilmlistOnProgramStart = true;

    public static int SYSTEM_LOAD_FILMLIST_MAX_DAYS = 0;
    public static int SYSTEM_LOAD_FILMLIST_MIN_DURATION = 0;
    public static boolean removeDiacritic = false;
    public static boolean filmInitNecessary = true;

    public static FilmChecker checker = null;//0,2s schneller als mit checker->true

    public static Filmlist filmlist;

    public static String userAgent = "";
    public static LoadFilmlist loadFilmlist;
    public static Stage primaryStage = null;
    public static String filmListUrl = "";

    public interface FilmChecker {
        boolean check(FilmData film);
    }
}
