package pers.lyks.strider.binding;


import pers.lyks.strider.Configuration;
import pers.lyks.strider.StriderHttpRequestFactory;
import pers.lyks.strider.annotation.Client;
import pers.lyks.strider.util.ClassScanner;

import java.lang.annotation.Annotation;
import java.util.*;

public class RequestRegistry {

    private final Map<Class<?>, RequestProxyFactory<?>> knownMappers = new HashMap<>();
    private Configuration configuration;

    public RequestRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    public <T> T getRequest(Class<T> type, StriderHttpRequestFactory requestFactory) {
        final RequestProxyFactory<T> requestProxyFactory = (RequestProxyFactory<T>) knownMappers.get(type);
        if (requestProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the RequestRegistry.");
        }
        try {
            return requestProxyFactory.newInstance(requestFactory);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> boolean hasRequest(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public <T> void addRequest(Class<T> type) {
        if (type.isInterface()) {
            if (hasRequest(type)) {
                throw new RuntimeException("Type " + type + " is already known to the RequestRegistry.");
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new RequestProxyFactory<>(type));
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

    public Collection<Class<?>> getRequest() {
        return Collections.unmodifiableCollection(knownMappers.keySet());
    }

    public void addRequests(Class<? extends Annotation> superType, String... packages) {

        Set<Class<?>> set = ClassScanner.getTypesAnnotatedWith(superType, packages);
        for (Class<?> c : set) {
            addRequest(c);
        }
    }

    public void addRequests(String... packages) {
        addRequests(Client.class, packages);
    }

}
