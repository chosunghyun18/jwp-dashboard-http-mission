package org.apache.coyote;

import java.io.BufferedReader;


public abstract class Request {

    public void setHook(AbstractProcessor abstractProcessor) {

    }

    public void setBufferReader(BufferedReader bf) {

    }
    public Response setResponse(Response response)  {
        return null;
    }

    public String getRequestMethod() {
        return null;
    }

    public String getRequestSourcePath() {
        return null;
    }

    public String getRequestBody() {
        return null;
    }

    public String getHeader() {
        return null;
    }
    public Boolean getIsCookie() {
        return null;
    }

    public String getSessionId() {
        return null;
    }

    public Response getResponse() {
        return null;
    }
}
