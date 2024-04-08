/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class P2Lock {

    public static boolean getLockInstance(final String lockFile) {
        boolean exit = false;

        try {
            final File file = new File(lockFile);
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            final FileLock fileLock = randomAccessFile.getChannel().tryLock();

            if (fileLock == null) {
                //dann läuft das Programm bereits
                P2Log.sysLog("PLock: Das Programm läuft bereits");
                exit = ask(file);

            } else {
                //das Programm läuft noch nicht
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                        file.delete();
                    } catch (Exception e) {
                        P2Log.errorLog(956230478, e, "Unable to remove lock file: " + lockFile);
                    }
                }));
            }
        } catch (Exception e) {
            P2Log.errorLog(784512965, e, "Unable to create and/or lock file: " + lockFile);
        }

        if (exit) {
            exitProg();
            return false;
        }
        return true;
    }

    private static boolean ask(File file) {
        boolean exit;
        P2Alert.BUTTON btn = P2Alert.showAlert_yes_no(null, "Programm starten",
                "Es läuft schon eine Instanz",
                "Das Programm läuft bereits, soll es nochmal gestartet werden?");
        exit = btn == P2Alert.BUTTON.NO;
        if (exit) {
            P2Log.sysLog("PLock: Das Programm läuft bereits - nicht nochmal starten");
        } else {
            //dann versuchen, das Lock auch vorsichtshalber zu löschen
            P2Log.sysLog("PLock: Das Programm läuft bereits - nochmal starten");
            try {
                file.delete();
            } catch (Exception e) {
                P2Log.errorLog(963569896, e, "Unable to delete the lock file: " + file);
            }
        }
        return exit;
    }

    private static void exitProg() {
        // dann jetzt beenden -> Thüss
        Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
