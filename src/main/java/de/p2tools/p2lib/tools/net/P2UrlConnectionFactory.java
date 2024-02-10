package de.p2tools.p2lib.tools.net;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class P2UrlConnectionFactory {
    private P2UrlConnectionFactory() {
    }

    public static HttpURLConnection getUrlConnection(String url) throws IOException {
        return getUrlConnection(new URL(url));
    }

    public static HttpURLConnection getUrlConnection(URL url) throws IOException {
        HttpURLConnection httpURLConn;

        String host = P2LibConst.proxyHost.getValueSafe();
        String port = P2LibConst.proxyPort.getValueSafe();

        if (!P2LibConst.useProxy.getValue() ||
                host.isEmpty() || port.isEmpty()) {
            // dann ohne Proxy
            httpURLConn = (HttpURLConnection) url.openConnection();

        } else {
            try {
                int iPort = Integer.parseInt(port);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, iPort));
                httpURLConn = (HttpURLConnection) url.openConnection(proxy);
            } catch (Exception ex) {
                // dann eben ohne proxy
                PLog.errorLog(701208613, "Kann die Proxy-Verbindung nicht aufbauen:\n" + host + " - " + port);
                httpURLConn = (HttpURLConnection) url.openConnection();
            }
        }
        return httpURLConn;
    }
}
