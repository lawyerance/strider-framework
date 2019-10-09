package pers.lyks.strider;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import pers.lyks.strider.annotation.Client;
import pers.lyks.strider.annotation.Header;
import pers.lyks.strider.annotation.Request;
import pers.lyks.strider.annotation.ResponseBody;
import pers.lyks.strider.codec.Decoder;
import pers.lyks.strider.convert.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodMetadata {

    private Method method;
    private final Class<?> returnType;
    private final Class<?> mapperInterface;
    private StriderHttpRequestFactory requestFactory;
    private String url;
    private pers.lyks.strider.annotation.Method httpMethod;
    private Map<String, Collection<String>> headers;
    Optional<Decoder> decoder;

    public MethodMetadata(Class<?> mapperInterface, Method method, StriderHttpRequestFactory configuration) {
        this.mapperInterface = mapperInterface;
        this.method = method;
        this.requestFactory = configuration;
        this.returnType = this.method.getReturnType();
    }

    public void resolver() {
        Request request = this.method.getDeclaredAnnotation(Request.class);
        this.httpMethod = request.method();
        this.url = resolveUrl(this.mapperInterface.getAnnotation(Client.class));
        this.headers = Stream.of(request.headers()).collect(Collectors.toMap(Header::name, item -> Arrays.asList(item.value())));

        ResponseBody responseBody = this.method.getDeclaredAnnotation(ResponseBody.class);
        this.decoder = Optional.ofNullable(responseBody).map(item -> {
            try {
                return item.value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        });
    }

    public Object execute(Object[] args) throws IOException {

        StriderHttpRequest httpRequest = this.requestFactory.createRequest(url, this.httpMethod);
        ClientHttpResponse httpResponse = httpRequest.execute();
        InputStream body = httpResponse.getBody();

        return decoder.map(decode -> {
            try {
                byte[] bytes = ByteStreams.toByteArray(body);
                return decode.decode(bytes, this.returnType);
            } catch (IOException e) {
                return null;
            }
        }).orElse(other(returnType, body));
    }


    private static String resolveUrl(Client client) {
        return client.host() + client.context();
    }

    private Object other(Type returnType, final InputStream is) {
        return DirectType.getType(returnType).map(item -> item.convert(is)).orElse(null);
    }

    private enum DirectType implements Converter<InputStream, Object> {
        STREAM(InputStream.class) {
            @Override
            public Object convert(InputStream is) {
                return is;
            }
        },
        BYTE_ARRAY(byte[].class) {
            @Override
            public Object convert(InputStream is) {
                try {
                    return ByteStreams.toByteArray(is);
                } catch (IOException e) {
                    return new byte[0];
                }
            }
        },
        STRING(String.class) {
            @Override
            public Object convert(InputStream is) {
                try {
                    byte[] bytes = ByteStreams.toByteArray(is);
                    return new String(bytes);
                } catch (IOException e) {
                    return (String) null;
                }
            }
        },
        VOID(Void.class) {
            @Override
            public Object convert(InputStream is) {
                return (Void) null;
            }
        };

        private final Type type;

        DirectType(Type type) {
            this.type = type;
        }

        private static final Map<Type, DirectType> caches = new HashMap<>();

        static {
            for (DirectType value : DirectType.values()) {
                caches.put(value.type, value);
            }
        }

        public static Optional<DirectType> getType(Type t) {
            return Optional.ofNullable(caches.get(t));
        }
    }
}
