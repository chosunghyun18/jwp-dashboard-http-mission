package org.apache.coyote;

public abstract class Response {
    public void setHook(AbstractProcessor abstractProcessor) {

    }
    public byte[] getHeader() {
        return null;
    }

    public byte[] getData() {
        return null;
    }

    public void addSetCookieKey() {

    }
    public void setSessionId(String sessionId) {

    }
}
