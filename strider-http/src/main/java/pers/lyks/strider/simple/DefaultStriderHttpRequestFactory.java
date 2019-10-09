package pers.lyks.strider.simple;

import pers.lyks.strider.StriderHttpRequest;
import pers.lyks.strider.StriderHttpRequestFactory;
import pers.lyks.strider.annotation.Method;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
public class DefaultStriderHttpRequestFactory implements StriderHttpRequestFactory {
    private Proxy proxy;
    private int connectTimeout;
    private int readTimeout;
    private boolean followRedirect = true;

    @Override
    public StriderHttpRequest createRequest(String url, Method method) throws IOException {
        HttpURLConnection connection = openConnection(url, this.proxy);
        prepareConnection(connection, method);
        return new DefaultHttpRequest(connection);
    }

    private HttpURLConnection openConnection(String url, Proxy proxy) throws IOException {
        return openConnection(new URL(url), proxy);
    }


    protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
        URLConnection urlConnection = (proxy != null ? url.openConnection(proxy) : url.openConnection());
        if (!HttpURLConnection.class.isInstance(urlConnection)) {
            throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + urlConnection);
        }
        return (HttpURLConnection) urlConnection;
    }

    protected void prepareConnection(HttpURLConnection connection, Method httpMethod) throws IOException {
        if (this.connectTimeout >= 0) {
            connection.setConnectTimeout(this.connectTimeout);
        }
        if (this.readTimeout >= 0) {
            connection.setReadTimeout(this.readTimeout);
        }

        connection.setDoInput(true);

        if (httpMethod == Method.GET && followRedirect) {
            connection.setInstanceFollowRedirects(true);
        } else {
            connection.setInstanceFollowRedirects(false);
        }

        if (httpMethod.hasBody()) {
            connection.setDoOutput(true);
        } else {
            connection.setDoOutput(false);
        }

        connection.setRequestMethod(httpMethod.name());
    }

}
