package pers.lyks.strider.client;

import pers.lyks.strider.annotation.Request;
import pers.lyks.strider.annotation.ResponseBody;
import pers.lyks.strider.annotation.Client;

import java.util.Map;

/**
 * @author lawyerance
 * @version 1.0 2019-09-03
 */
@Client(host = "http://127.0.0.1:10082", context = "/get")
public interface StriderJsonResponseGet {
    @Request(uri = "/search?keyword=import")
    @ResponseBody
    Map<String, Object> get();

}
