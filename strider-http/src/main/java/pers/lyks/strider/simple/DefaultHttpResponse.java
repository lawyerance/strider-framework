package pers.lyks.strider.simple;

import org.springframework.util.StreamUtils;
import pers.lyks.strider.StriderHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
public final class DefaultHttpResponse implements StriderHttpResponse {

    private final HttpURLConnection connection;
    private Map<String, Collection<String>> headers = new LinkedHashMap<>();
    private InputStream responseStream;

    public DefaultHttpResponse(HttpURLConnection connection) {
        this.connection = connection;
    }


    @Override
    public InputStream getBody() throws IOException {
        InputStream errorStream = this.connection.getErrorStream();
        this.responseStream = (errorStream != null ? errorStream : this.connection.getInputStream());
        return this.responseStream;
    }


    @Override
    public Map<String, Collection<String>> headers() {
        if (this.headers.isEmpty()) {
            for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
                // response message
                if (field.getKey() != null) {
                    headers.put(field.getKey(), field.getValue());
                }
            }
        }
        return this.headers;
    }

    @Override
    public int getStatusCode() throws IOException {
        return this.connection.getResponseCode();
    }

    @Override
    public String getStatusMessage() throws IOException {
        return this.connection.getResponseMessage();
    }


    @Override
    public void close() {
        try {
            if (this.responseStream == null) {
                getBody();
            }
            StreamUtils.drain(this.responseStream);
            this.responseStream.close();
        } catch (Exception ex) {
            // ignore
        }
    }
}
