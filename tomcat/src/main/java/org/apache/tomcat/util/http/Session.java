package org.apache.tomcat.util.http;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.UUID;
import org.apache.catalina.util.SessionManager;

public class Session implements HttpSession {
    private final String sessionId;
    private final User user;

    public Session(String sessionId , User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public static String checkSessionIdExpire(String sessionId) {
        if(isExpired(sessionId)) {
            SessionManager.getInstance().remove(sessionId);
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }
    private static Boolean isExpired(String id) {
        // add Busniess Logic for seesionID rule
        return false;
    }

    public static String addGuestVisitor() {
        var generatedID = UUID.randomUUID().toString();
        SessionManager.getInstance().add(new Session(generatedID,new User()));
        return generatedID;
    }
    @Override
    public Object getAttribute(String name) {
        return null;
    }
    @Override
    public boolean isNew() {
        return user.isNew();
    }
    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {

    }
    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }
    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        if(value.equals(name)) {
            String[] sessionInfo =value.toString().split("=");
            String id = sessionInfo[0];
            String flag = sessionInfo[1];
            if(flag.equals("false")) SessionManager.getInstance().add(new Session(id,new User("user")));
        }
    }

    @Override
    public void putValue(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public void invalidate() {

    }
}
