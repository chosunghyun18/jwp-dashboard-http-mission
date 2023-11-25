package org.apache.tomcat.util.http;

import java.util.ArrayList;
import java.util.List;

public class Cookies {
    private List<Cookie> cookies;

    public Cookies(List<Cookie> cookies) {
        this.cookies = new ArrayList<>(cookies);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public String toString() {
        return "Cookies{" +
                "cookies=" + cookies +
                '}';
    }
}
