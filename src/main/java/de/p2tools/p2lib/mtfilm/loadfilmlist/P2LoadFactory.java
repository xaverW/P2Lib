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


package de.p2tools.p2lib.mtfilm.loadfilmlist;

import de.p2tools.p2lib.alert.P2Alert;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.tukaani.xz.XZInputStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.ZipInputStream;

public class P2LoadFactory {

    private P2LoadFactory() {
    }

    /**
     * liefert die String-Liste der Sender die _NICHT_ geladen werden sollen
     *
     * @return
     */
    public static ArrayList<String> getSenderListNotToLoad() {
        return new ArrayList(Arrays.asList(P2LoadConst.SYSTEM_LOAD_NOT_SENDER.split(",")));
    }

    /**
     * die Einstellung _alle Sender nicht laden_ ist sinnlos, ist ein Fehler des Nutzers
     * und das ist nur ein Hinweis daruf!
     *
     * @return
     */
    public static boolean checkAllSenderSelectedNotToLoad(Stage stage) {
        // die Einstellung _alle Sender nicht laden_,  wird ja sonst nix geladen, ist ein Fehler des Nutzers
        // und das ist nur ein Hinweis darauf!
        ArrayList<String> aListSenderNotToLoad = getSenderListNotToLoad();
        boolean allSender = true;
        for (String sender : P2LoadConst.SENDER) {
            Optional<String> optional = aListSenderNotToLoad.stream().filter(aktSender -> aktSender.equals(sender)).findAny();
            if (optional.isEmpty()) {
                // mindestens einer fehlt :)
                allSender = false;
                break;
            }
        }

        if (allSender) {
            Platform.runLater(() -> P2Alert.showErrorAlert(stage,
                    "Sender laden",
                    "Es werden keine Filme geladen. Alle Sender " +
                            "sind vom Laden ausgenommen!" +
                            "\n\n" +
                            "Einstellungen -> Filmliste laden"));
        }

        return allSender;
    }

    public static InputStream selectDecompressor(String source, InputStream in) throws Exception {
        if (source.endsWith(P2LoadConst.FORMAT_XZ)) {
            in = new XZInputStream(in);
        } else if (source.endsWith(P2LoadConst.FORMAT_ZIP)) {
            final ZipInputStream zipInputStream = new ZipInputStream(in);
            zipInputStream.getNextEntry();
            in = zipInputStream;
        }
        return in;
    }
}
