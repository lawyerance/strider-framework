package pers.lyks.strider.simple;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import pers.lyks.strider.StriderHttpRequest;
import pers.lyks.strider.annotation.Method;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
public final class DefaultHttpRequest implements StriderHttpRequest {
    private HttpURLConnection connection;
    private OutputStream body;
    private int chunkSize = 1024 * 8 + 4;


    DefaultHttpRequest(HttpURLConnection connection) {
        this.connection = connection;
    }

    @Override
    public ClientHttpResponse execute() throws IOException {

        return null;
    }

    @Override
    public OutputStream getBody() throws IOException {
        return null;
    }

    @Override
    public Map<String, Collection<String>> headers() {
        return null;
    }

    @Override
    public String url() {
        return this.connection.getURL().toString();
    }

    @Override
    public Method method() {
        return null;
    }

    static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
        headers.forEach((headerName, headerValues) -> {
            if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
                String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
                connection.setRequestProperty(headerName, headerValue);
            } else {
                for (String headerValue : headerValues) {
                    String actualHeaderValue = headerValue != null ? headerValue : "";
                    connection.addRequestProperty(headerName, actualHeaderValue);
                }
            }
        });
    }
}
