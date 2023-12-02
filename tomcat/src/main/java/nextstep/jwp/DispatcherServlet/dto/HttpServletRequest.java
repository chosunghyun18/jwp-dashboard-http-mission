package nextstep.jwp.DispatcherServlet.dto;

import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Request;

public class HttpServletRequest {
    private final HttpSession httpSession;
    private final Request request;

    public HttpServletRequest(HttpSession session, Request request) {
        this.httpSession = session;
        this.request = request;
    }

    public String getRequestSourcePath() {
        return request.getRequestSourcePath();
    }

    public String getRequestBody() {
        return request.getRequestBody();
    }

    public String getRequestMethod() {
        return request.getRequestMethod();
    }
}
