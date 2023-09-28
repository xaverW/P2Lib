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


package de.p2tools.p2lib.tools.net;

import de.p2tools.p2lib.tools.log.PLog;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Proxy {
    private static final String HTTP_PROXY_USER = "http.proxyUser";
    private static final String HTTP_PROXY_PW = "http.proxyPassword";
    private static final String LOG_TEXT_PROXY_AUTHENTICATION_SUCCESSFUL = "Proxy Authentication: (%s)";
    private static final String LOG_TEXT_PROXY_AUTHENTICATION_NOT_CONFIGURED = "Proxy Authentication: not configured";
    private static final String LOG_TEXT_PROXY_PASSWORD_NOT_SET = "Proxy Authentication: Password is not set";
    private static final String LOG_TEXT_PROXY_AUTHENTICATION_CANNOT_ACCESS_PROXY_USER_PROXY_PW = "Proxy Authentication: cannot access proxyUser / proxyPassword";

    public static void proxyAuthentication() {
        try {
            final String prxUser = System.getProperty(HTTP_PROXY_USER, null);
            final String prxPassword = System.getProperty(HTTP_PROXY_PW, null);
            if (prxUser != null && prxPassword != null) {
                final PasswordAuthentication authenticator = new PasswordAuthentication(prxUser, prxPassword.toCharArray());
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return authenticator;
                    }
                });
                PLog.sysLog(String.format(LOG_TEXT_PROXY_AUTHENTICATION_SUCCESSFUL, prxUser));
            } else if (prxUser != null && prxPassword == null) {
                PLog.sysLog(LOG_TEXT_PROXY_PASSWORD_NOT_SET);
            } else {
                PLog.sysLog(LOG_TEXT_PROXY_AUTHENTICATION_NOT_CONFIGURED);
            }

        } catch (final SecurityException se) {
            PLog.sysLog(LOG_TEXT_PROXY_AUTHENTICATION_CANNOT_ACCESS_PROXY_USER_PROXY_PW + se.toString());
        }
    }

}
