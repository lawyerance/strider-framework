package pers.lyks.strider.client;

import pers.lyks.strider.annotation.Request;
import pers.lyks.strider.annotation.Client;

/**
 * @author lawyerance
 * @version 1.0 2019-09-03
 */
@Client(host = "http://127.0.0.1:10081", context = "/get")
public interface StriderGet {
    @Request(uri = "/search?keyword=import")
    String get();

    @Request(uri = "/search?keyword=import")
    byte[] getByteArray();

    @Request(uri = "/search?keyword=import")
    void discarding();
}
