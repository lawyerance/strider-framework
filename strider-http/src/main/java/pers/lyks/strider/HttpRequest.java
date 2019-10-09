package pers.lyks.strider;

import pers.lyks.strider.annotation.Method;

/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
public interface HttpRequest extends HttpOutputMessage {

    String url();

    Method method();
}
