package pers.lyks.strider.exception;

import java.io.IOException;
import java.net.URI;

/**
 * Signals that a HTTP response returned a mime type that is not supported.
 *
 * @author lawyerance
 * @version 1.0 2019-09-01
 */
public class UnsupportedMimeTypeException extends IOException {
    private String mimeType;
    private URI uri;

    public UnsupportedMimeTypeException(String message, String mimeType, URI uri) {
        super(message);
        this.mimeType = mimeType;
        this.uri = uri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return super.toString() + ". Mimetype=" + mimeType + ", URI=" + uri;
    }
}

