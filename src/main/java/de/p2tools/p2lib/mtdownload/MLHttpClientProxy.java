/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.p2lib.mtdownload;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public class MLHttpClientProxy {
    private final static MLHttpClientProxy ourInstance = new MLHttpClientProxy();
    private static final String HTTP_PROXY_AUTHORIZATION = "Proxy-Authorization";
    private OkHttpClient httpClient;
    private OkHttpClient copyClient;

    private MLHttpClientProxy() {
        String proxyHost = P2LibConst.proxyHost.getValueSafe();
        String proxyPort = P2LibConst.proxyPort.getValueSafe();

        try {
            if (!P2LibConst.useProxy.getValue()) {
                // dann wills der User nicht
                setupNonProxyClients();
                P2Log.debugLog("Keinen Proxy setzen");

            } else if (!proxyHost.isEmpty() && !proxyPort.isEmpty()) {
                final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
                setupProxyClients(proxy);
                P2Log.debugLog("Proxy verwenden: :\n" + proxyHost + " - " + proxyPort);

            } else {
                // dann halt keinen
                setupNonProxyClients();
                P2Log.debugLog("Keinen Proxy setzen, Vorgaben nicht vollständig:\n" + proxyHost + " - " + proxyPort);
            }
        } catch (NumberFormatException ex) {
            setupNonProxyClients();
            P2Log.debugLog("Keinen Proxy setzen, Fehler:\n" + proxyHost + " - " + proxyPort);
        }
    }

    private void setupProxyClients(Proxy proxy) {
        final Authenticator proxyAuthenticator = setupProxyAuthenticator();

        OkHttpClient.Builder tmpBuilder;
        tmpBuilder = getDefaultClientBuilder().proxy(proxy);

        if (proxyAuthenticator != null) {
            tmpBuilder.proxyAuthenticator(proxyAuthenticator);
        }
        httpClient = tmpBuilder.build();

        tmpBuilder = getDefaultClientBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .proxy(proxy);

        if (proxyAuthenticator != null) {
            tmpBuilder.proxyAuthenticator(proxyAuthenticator);
        }
        copyClient = tmpBuilder.build();
    }

    private void setupNonProxyClients() {
        httpClient = getDefaultClientBuilder().build();
        copyClient = getDefaultClientBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
    }

    private Authenticator setupProxyAuthenticator() {
        String prxUser = P2LibConst.proxyUser.getValueSafe();
        String prxPassword = P2LibConst.proxyPwd.getValueSafe();
        Authenticator proxyAuthenticator = null;

        if (!prxUser.isEmpty() && !prxPassword.isEmpty()) {
            proxyAuthenticator = createAuthenticator(prxUser, prxPassword);
            P2Log.debugLog("Authenticator setzen");
        }
        return proxyAuthenticator;
    }

    private Authenticator createAuthenticator(String prxUser, String prxPassword) {
        return (route, response) -> {
            if (response.request().header(HTTP_PROXY_AUTHORIZATION) != null) {
                return null; // Give up, we've already attempted to authenticate.
            }
            final String credential = Credentials.basic(prxUser, prxPassword);
            return response.request().newBuilder()
                    .header(HTTP_PROXY_AUTHORIZATION, credential)
                    .build();
        };
    }

    private OkHttpClient.Builder getDefaultClientBuilder() {
        var builder = new OkHttpClient.Builder();

        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        return builder;
    }

    public static MLHttpClientProxy getInstance() {
        return ourInstance;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public OkHttpClient getReducedTimeOutClient() {
        return copyClient;
    }
}
