package pers.lyks.strider;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
public interface HttpInputMessage extends BaseHttp {
    InputStream getBody() throws IOException;
}
