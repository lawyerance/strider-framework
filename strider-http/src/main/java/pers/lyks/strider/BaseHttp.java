package pers.lyks.strider;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
public interface BaseHttp extends Serializable {
    Map<String, Collection<String>> headers();
}
