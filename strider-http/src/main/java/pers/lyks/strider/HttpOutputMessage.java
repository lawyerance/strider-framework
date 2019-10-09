package pers.lyks.strider;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
public interface HttpOutputMessage extends BaseHttp {
    OutputStream getBody() throws IOException;
}
