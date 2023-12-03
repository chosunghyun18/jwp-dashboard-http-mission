package org.apache.coyote.http11;

import jakarta.servlet.http.HttpSession;
import org.apache.coyote.AbstractProcessor;
import org.apache.coyote.Response;

public class HttpResponse extends Response {
    protected String sessionId;
    protected Boolean setCookieKey = false;

    @Override
    public void setHook(AbstractProcessor abstractProcessor) {
        super.setHook(abstractProcessor);
    }

    public byte[] getHeader() {
        return null;
    }

    public byte[] getBody() {
        return null;
    }
    @Override
    public void  setSetCookieKey(Boolean value) {
        this.setCookieKey = value;
    }
    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public HttpSession updateSessionToSessionDB() {
        return null;
    }
    @Override
    public Boolean getSetCookieKey() {
        return this.setCookieKey;
    }
}
