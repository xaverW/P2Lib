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

import de.p2tools.p2lib.tools.log.P2Log;

public class P2Exception extends IllegalStateException {

    public P2Exception() {
        super();
    }

    public P2Exception(String message) {
        super(message);
    }

    public P2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public P2Exception(Throwable cause) {
        super(cause);
    }

    public String getMsg() {
        return super.getMessage();
    }


    public static void throwPException(String message) {
        throwPException(000000000, message);
    }

    public static void throwPException(int no, String message) {
        P2Log.errorLog(no, message);
        throw new P2Exception(message);
    }

    public static void throwPException(String message, Exception cause) {
        P2Log.errorLog(000000000, message);
        throw new P2Exception(message, cause);
    }

}