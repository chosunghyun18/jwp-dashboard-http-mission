package org.apache.catalina.util;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.catalina.manager.Manager;


public class SessionManager implements Manager {
    private final static Map<String,HttpSession> sessionDB = new HashMap<>();

    @Override
    public void add(HttpSession session) {
        sessionDB.put(session.getId(),session);
    }

    @Override
    public HttpSession findSession(String id) throws IOException {
        return sessionDB.get(id);
    }

    @Override
    public void remove(HttpSession session) {
        sessionDB.remove(session.getId());
    }
}
