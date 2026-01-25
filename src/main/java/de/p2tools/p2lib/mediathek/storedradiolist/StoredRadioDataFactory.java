package de.p2tools.p2lib.mediathek.storedradiolist;

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.tools.log.P2Log;

import java.util.Random;

public class StoredRadioDataFactory {
    private StoredRadioDataFactory() {
    }

    public static String getStoredRadioList() {
        String url;
        StoredRadioList storedRadioList = loadStoredRadioList();
        if (!storedRadioList.isEmpty()) {
            int rInt = new Random().nextInt(storedRadioList.getSize());
            url = storedRadioList.get(rInt).getUrl();
            P2Log.sysLog("URL der StoredRadioList: " + url);
            return url;
        }

        // nur wenn die stored.. leer ist
        int rand = new Random().nextInt(3); // 0...2
        url = switch (rand) {
            case 0 -> P2LoadConst.RADIO_LIST_URL_1;
            case 1 -> P2LoadConst.RADIO_LIST_URL_2;
            case 2 -> P2LoadConst.RADIO_LIST_URL_3;
            default -> P2LoadConst.RADIO_LIST_URL_1;
        };

        P2Log.sysLog("URL der StoredRadioList[STORED]: " + url);
        return url;
    }

    private static StoredRadioList loadStoredRadioList() {
        StoredRadioList storedAudioList = new StoredRadioList();
        String url = P2LoadConst.STORED_RADIO_LIST;
        try {
            ConfigFile configFile = new ConfigFile(url, false);
            configFile.addConfigs(storedAudioList);
            if (!ConfigReadFile.readConfig(configFile)) {
                P2Log.errorLog(951753963, "StoredRadioList konnten nicht geladen werden");
            }
        } catch (final Exception ignore) {
        }

        return storedAudioList;
    }
}
