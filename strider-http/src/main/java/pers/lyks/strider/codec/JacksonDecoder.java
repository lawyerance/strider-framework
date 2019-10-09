package pers.lyks.strider.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;

/**
 * @author lawyerance
 * @version 1.0 2019-08-09
 */
public class JacksonDecoder implements Decoder {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T decode(byte[] bytes, Type type) {
        JavaType javaType = mapper.constructType(type);
        try {
            return mapper.readValue(bytes, javaType);
        } catch (IOException e) {
            return null;
        }
    }
}
