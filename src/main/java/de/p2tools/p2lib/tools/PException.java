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


package de.p2tools.p2lib.tools;

import de.p2tools.p2lib.tools.log.PLog;

public class PException extends IllegalStateException {

    public PException() {
        super();
    }

    public PException(String message) {
        super(message);
    }

    public PException(String message, Throwable cause) {
        super(message, cause);
    }

    public PException(Throwable cause) {
        super(cause);
    }

    public String getMsg() {
        return super.getMessage();
    }


    public static void throwPException(String message) {
        throwPException(000000000, message);
    }

    public static void throwPException(int no, String message) {
        PLog.errorLog(no, message);
        throw new PException(message);
    }

    public static void throwPException(String message, Exception cause) {
        PLog.errorLog(000000000, message);
        throw new PException(message, cause);
    }

}