package org.apache.coyote.http11;

import org.apache.coyote.AbstractProcessor;
import org.apache.coyote.Response;

public class HttpResponse extends Response {
    private String sessionId;
    private Boolean setCookieKey = false;

    @Override
    public void setHook(AbstractProcessor abstractProcessor) {
        super.setHook(abstractProcessor);
    }

    @Override
    public byte[] getHeader() {
        return super.getHeader();
    }

    @Override
    public byte[] getData() {
        return super.getData();
    }
    @Override
    public void  addSetCookieKey() {
        this.setCookieKey = true;
    }
    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
