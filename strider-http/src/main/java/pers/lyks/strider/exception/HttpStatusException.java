package pers.lyks.strider.exception;

import java.io.IOException;
import java.net.URI;

/**
 * Signals that a HTTP request resulted in a not OK HTTP response.
 *
 * @author lawyerance
 * @version 1.0 2019-09-01
 */
public class HttpStatusException extends IOException {
    private int statusCode;
    private URI uri;

    public HttpStatusException(String message, int statusCode, URI uri) {
        super(message);
        this.statusCode = statusCode;
        this.uri = uri;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return super.toString() + ". Status=" + statusCode + ", URI=" + uri;
    }
}
