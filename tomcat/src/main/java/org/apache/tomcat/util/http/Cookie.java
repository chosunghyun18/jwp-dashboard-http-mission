package org.apache.tomcat.util.http;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private Map<String,Object> cookie;

    public Cookie(Map<String, Object> cookie) {
        this.cookie = new HashMap<>(cookie);
    }
}
