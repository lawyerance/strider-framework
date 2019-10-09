package pers.lyks.strider;

import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
public interface StriderHttpRequest extends HttpOutputMessage, HttpRequest {
    ClientHttpResponse execute() throws IOException;
}
