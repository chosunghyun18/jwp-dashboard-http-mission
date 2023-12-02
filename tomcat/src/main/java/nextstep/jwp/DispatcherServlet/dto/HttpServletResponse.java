package nextstep.jwp.DispatcherServlet.dto;

import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;

public class HttpServletResponse extends Response {
    private final HttpSession httpSession;
    private final Response response;
    private Boolean isNew;
    private String header;
    private String data;

    public HttpServletResponse(HttpSession httpSession, Response response) {
        this.isNew = httpSession.isNew();
        this.httpSession = httpSession;
        this.response = response;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public Response getResponse() {
        return response;
    }

    public void setHeaders(String header) {
        this.header = header;
    }

    public void setData(String data) {
        // check httpSession , case if new login , make httpSession is new true into false
        this.data = data;
    }

    public String getUserSessionInfo() {
        return httpSession.getId() +"="+httpSession.isNew();
    }
}
