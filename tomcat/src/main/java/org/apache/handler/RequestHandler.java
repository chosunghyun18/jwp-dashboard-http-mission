package org.apache.handler;

import jakarta.servlet.http.HttpSession;

import nextstep.jwp.Filter.Http11Filter;
import org.apache.catalina.util.SessionManager;
import org.apache.coyote.Request;
import org.apache.coyote.Response;

public class RequestHandler {
    private final Http11Filter Http11Filter;
    private final SessionManager sessionManager;

    public RequestHandler() {
        Http11Filter = new Http11Filter();
        sessionManager = SessionManager.getInstance();
    }
    public Response getResponse(Request request , Response response) {
        HttpSession session  = sessionManager.findSession(request.getSessionId());
        return Http11Filter.handleRequest(request,response,session);
    }
}
