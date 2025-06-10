package de.p2tools.p2lib.mediathek.audiolistload;

import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class P2LoadAudioFactory {
    public static final double PROGRESS_MIN = 0.0;
    public static final double PROGRESS_MAX = 1.0;
    public static final double PROGRESS_INDETERMINATE = -1.0;

    public static boolean isNotFromToday(String strDate) {
        // in den Einstellungen gespeichertes Datum prüfen
        LocalDateTime listDate = P2LDateTimeFactory.fromString(strDate, P2DateConst.DT_FORMATTER__FILMLIST); // "dd.MM.yyyy, HH:mm"
        LocalDate act = listDate.toLocalDate();
        LocalDate today = LocalDate.now();
        return !act.equals(today);
    }
}
