package pers.lyks.strider;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
public interface StriderHttpResponse extends HttpInputMessage, Closeable {

    int getStatusCode() throws IOException;

    /**
     * Return the HTTP status text of the response.
     *
     * @return the HTTP status text
     * @throws IOException in case of I/O errors
     */
    String getStatusMessage() throws IOException;

}
