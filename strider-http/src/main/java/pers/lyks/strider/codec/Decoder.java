package pers.lyks.strider.codec;

import java.lang.reflect.Type;

/**
 * @author lawyerance
 * @version 1.0 2019-08-09
 */
public interface Decoder {
    <T> T decode(byte[] bytes, Type type);
}
