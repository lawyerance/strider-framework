package pers.lyks.strider.pasring;

import java.util.Properties;

/**
 * @author lawyerance
 * @version 1.0 2019-09-11
 */
@FunctionalInterface
public interface PropertyParser {
    String parse(String content, Properties properties);
}
