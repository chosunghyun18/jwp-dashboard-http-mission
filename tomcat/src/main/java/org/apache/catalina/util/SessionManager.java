package org.apache.catalina.util;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.catalina.manager.Manager;

public final class SessionManager implements Manager {

    private static final SessionManager instance;
    private final Map<String, HttpSession> sessionDB;

    static {
        instance = new SessionManager();
    }

    private SessionManager() {
        sessionDB = new ConcurrentHashMap<>();
    }
    public static SessionManager getInstance() {
        return instance;
    }

    @Override
    public void add(HttpSession session) {
        sessionDB.put(session.getId(), session);
    }

    @Override
    public HttpSession findSession(String id) {
        return sessionDB.get(id);
    }

    @Override
    public void remove(HttpSession session) {
        sessionDB.remove(session.getId());
    }
    public void remove(String sessionId) {
        sessionDB.remove(sessionId);
    }
}
