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


package de.p2tools.p2lib.configfile;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDialogFileChooser;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;

class ConfigBackupFile {
    public static final int MAX_COPY_BACKUP_FILE = 5; // Maximum number of backup files to be stored.

    private static boolean alreadyMadeBackup = false;
    private static final String CONFIG_FILE_COPY_ADDON = "_copy_";
    private final String backupFileName;
    private final Path filePath;

    ConfigBackupFile(Path filePath) {
        this.filePath = filePath;
        this.backupFileName = filePath.getFileName().toString() + CONFIG_FILE_COPY_ADDON;
    }

    /**
     * Create backup copies of settings file.
     */
    void backupConfigFile() {
        if (alreadyMadeBackup) {
            return;
        }

        ArrayList<String> list = new ArrayList<>();
        list.add(PLog.LILNE3);
        list.add("BackupConfigFile sichern");

        try {
            long createTime = -1;
            Path confFileCopy = filePath.getParent().resolve(backupFileName + 1);
            if (Files.exists(confFileCopy)) {
                //schauen, obs das File schon gibt
                final BasicFileAttributes attrs = Files.readAttributes(confFileCopy, BasicFileAttributes.class);
                final FileTime d = attrs.lastModifiedTime();
                createTime = d.toMillis();
            }

            if (createTime == -1 || createTime < getToday_00_00()) {
                //und es schon von gestern ist
                for (int i = MAX_COPY_BACKUP_FILE; i > 1; --i) {
                    confFileCopy = filePath.getParent().resolve(backupFileName + (i - 1));//Start: xx_copy_4, xx_copy_3, ..
                    final Path confFileCopy_2 = filePath.getParent().resolve(backupFileName + i);//wird zu xx_copy_5, xx_copy_4, ..
                    if (Files.exists(confFileCopy)) {
                        Files.move(confFileCopy, confFileCopy_2, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                if (Files.exists(filePath)) {
                    //und jetzt das org-config kopieren zu xx_copy_1
                    Files.move(filePath, filePath.getParent().resolve(backupFileName + 1),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                list.add("BackupConfigFile wurde gesichert: " + backupFileName + 1);
            } else {
                list.add("BackupConfigFile wurde heute schon gesichert");
            }
        } catch (final IOException e) {
            list.add("Die Einstellungen konnten nicht komplett gesichert werden!");
            PLog.errorLog(795623147, e);
        }

        alreadyMadeBackup = true;
        list.add(PLog.LILNE3);
        PLog.sysLog(list);
    }

    ArrayList<Path> loadBackup(String header, String text) {
        final ArrayList<Path> pathList = new ArrayList<>();
        getXmlCopyFilePath(pathList);
        if (pathList.isEmpty()) {
            PLog.sysLog("Es gibt kein Backup");
            return null;
        }

        // dann gibts ein Backup
        PLog.sysLog("Es gibt ein Backup");
        //stage bzw. scene gibts noch nicht
        //ist ja meist der Programmstart!!!
        if (PAlert.BUTTON.YES != new PDialogFileChooser().showAlert_yes_no(null, "Gesicherte Einstellungen laden?",

                header.isEmpty() ? "Die Einstellungen sind beschädigt" + P2LibConst.LINE_SEPARATOR +
                        "und können nicht geladen werden." : header,

                text.isEmpty() ?
                        "Soll versucht werden, mit gesicherten" + P2LibConst.LINE_SEPARATOR
                                + "Einstellungen zu starten?" + P2LibConst.LINE_SEPARATORx2
                                + "(ansonsten startet das Programm mit" + P2LibConst.LINE_SEPARATOR
                                + "Standardeinstellungen)" : text)) {

            PLog.sysLog("User will kein Backup laden.");
            return null;
        }

        return pathList;
    }

    /**
     * Return the path to "p2tools.xml_copy_" first copy exists
     *
     * @param xmlFilePath Path to file.
     */
    private void getXmlCopyFilePath(ArrayList<Path> xmlFilePath) {
        for (int i = 1; i <= MAX_COPY_BACKUP_FILE; ++i) {
            final Path path = filePath.getParent().resolve(backupFileName + i);
            if (Files.exists(path)) {
                xmlFilePath.add(path);
            }
        }
    }

    /**
     * Return the number of milliseconds from today´s midnight.
     *
     * @return Number of milliseconds from today´s midnight.
     */
    private long getToday_00_00() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
}
