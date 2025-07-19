package de.p2tools.p2lib.mediathek.storedaudiolist;

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.tools.log.P2Log;

import java.util.Random;

public class StoredAudioDataFactory {
    private StoredAudioDataFactory() {
    }

    public static String getStoredAudioList() {
        String url;
        StoredAudioList storedAudioList = loadStoredAudioList();
        if (!storedAudioList.isEmpty()) {
            int rInt = new Random().nextInt(storedAudioList.getSize());
            url = storedAudioList.get(rInt).getUrl();
            P2Log.sysLog("URL der AudioList: " + url);
            return url;
        }

        Random random = new Random();
        if (random.nextBoolean()) {
            url = P2LoadConst.AUDIOLIST_URL_1;
        } else {
            url = P2LoadConst.AUDIOLIST_URL_2;
        }
        P2Log.sysLog("URL der AudioList[STORED]: " + url);
        return url;
    }

    private static StoredAudioList loadStoredAudioList() {
        StoredAudioList storedAudioList = new StoredAudioList();
        String url = P2LoadConst.STORED_AUDIO_LIST;
        try {
            ConfigFile configFile = new ConfigFile(url, false);
            configFile.addConfigs(storedAudioList);
            if (!ConfigReadFile.readConfig(configFile)) {
                P2Log.errorLog(989845214, "StoredAudioData konnten nicht geladen werden");
            }
        } catch (final Exception ignore) {
        }

        return storedAudioList;
    }
}
