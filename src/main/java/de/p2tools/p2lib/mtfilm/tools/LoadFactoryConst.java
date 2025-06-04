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


package de.p2tools.p2lib.mtfilm.tools;

import de.p2tools.p2lib.mtfilm.film.FilmData;
import de.p2tools.p2lib.mtfilm.film.Filmlist;
import de.p2tools.p2lib.mtfilm.loadfilmlist.P2LoadFilmlist;
import javafx.stage.Stage;

public class LoadFactoryConst {
    public static final String DREI_SAT = "3Sat";
    public static final String DREI_SAT_ = "3sat.Online";
    public static final String ARD = "ARD";
    public static final String ARD_ = "Arbeitsgemeinschaft der öffentlich-rechtlichen Rundfunkanstalten der Bundesrepublik Deutschland";

    private static final String ARTE_DE = "ARTE.DE";
    private static final String ARTE_DE_ = "Association Relative à la Télévision Européenne";
    private static final String ARTE_EN = "ARTE.EN";
    private static final String ARTE_EN_ = "Association Relative à la Télévision Européenne";
    private static final String ARTE_ES = "ARTE.ES";
    private static final String ARTE_ES_ = "Association Relative à la Télévision Européenne";
    private static final String ARTE_FR = "ARTE.FR";
    private static final String ARTE_FR_ = "Association Relative à la Télévision Européenne";
    private static final String ARTE_IT = "ARTE.IT";
    private static final String ARTE_IT_ = "Association Relative à la Télévision Européenne";
    private static final String ARTE_PL = "ARTE.PL";
    private static final String ARTE_PL_ = "Association Relative à la Télévision Européenne";

    private static final String BR = "BR";
    private static final String BR_ = "Bayerischer Rundfunk";
    private static final String DW = "DW";
    private static final String DW_ = "Deutsche Welle";
    private static final String FUNK_NET = "Funk.net";
    private static final String FUNK_NET_ = "Online-Content-Netzwerk der ARD und des ZDF";

    private static final String HR = "HR";
    private static final String HR_ = "Hessischer Rundfunk";
    public static final String KIKA = "KiKA"; // wird bein Einsortieren eine updateFilmListe gebraucht
    public static final String KIKA_ = "Kinderkanal von ARD und ZDF"; // wird bein Einsortieren eine updateFilmListe gebraucht
    private static final String MDR = "MDR";
    private static final String MDR_ = "Mitteldeutscher Rundfunk";
    private static final String NDR = "NDR";
    private static final String NDR_ = "Norddeutscher Rundfunk";
    public static final String ORF = "ORF"; // wird bein Einsortieren eine updateFilmListe gebraucht
    public static final String ORF_ = "Österreichischer Rundfunk"; // wird bein Einsortieren eine updateFilmListe gebraucht
    private static final String PHOENIX = "PHOENIX";
    private static final String PHOENIX_ = "Arbeitsgemeinschaft der Rundfunkanstalten der Bundesrepublik Deutschland (ARD) und des Zweiten Deutschen Fernsehens (ZDF)";
    private static final String RBB = "RBB";
    private static final String RBB_ = "Rundfunk Berlin-Brandenburg";
    private static final String RBB_TV = "RBTV"; // "rbtv" und "Radio Bremen TV" werden darauf abgebildet
    private static final String RBB_TV_ = "Radio Bremen TV"; // "rbtv" und "Radio Bremen TV" werden darauf abgebildet

    private static final String SR = "SR";
    private static final String SR_ = "Saarländischer Rundfunk";
    private static final String SRF = "SRF";
    private static final String SRF_ = "Schweizer Radio und Fernsehen";
    private static final String SWR = "SWR";
    private static final String SWR_ = "Südwestrundfunk";
    private static final String WDR = "WDR";
    private static final String WDR_ = "Westdeutscher Rundfunk Köln";
    private static final String ZDF = "ZDF";
    private static final String ZDF_ = "Zweites Deutsches Fernsehen";
    private static final String ZDF_TIVI = "ZDF-tivi";
    private static final String ZDF_TIVI_ = "Kinder- und Jugend-Programmfenster des ZDF";

    // das wird in den Einstellungen "Sender nicht laden" angezeigt
    public static final String[] SENDER = {DREI_SAT, ARD,
            ARTE_DE, ARTE_EN, ARTE_ES, ARTE_FR, ARTE_IT, ARTE_PL,
            BR, DW, FUNK_NET, HR, KIKA, MDR, NDR, ORF, PHOENIX, RBB, RBB_TV, SR,
            SRF, SWR, WDR, ZDF, ZDF_TIVI};

    public static final String[] SENDER_ = {DREI_SAT_, ARD_,
            ARTE_DE_, ARTE_EN_, ARTE_ES_, ARTE_FR_, ARTE_IT_, ARTE_PL_,
            BR_, DW_, FUNK_NET_, HR_, KIKA_, MDR_, NDR_, ORF_, PHOENIX_, RBB_, RBB_TV_, SR_,
            SRF_, SWR_, WDR_, ZDF_, ZDF_TIVI_};


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
    public static P2LoadFilmlist p2LoadFilmlist;
    public static Stage primaryStage = null;
    public static String filmListUrl = "";

    public interface FilmChecker {
        boolean check(FilmData film);
    }
}
