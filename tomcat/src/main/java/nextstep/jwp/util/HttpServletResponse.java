package nextstep.jwp.util;

import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.apache.coyote.http11.HttpResponse;

public class HttpServletResponse extends HttpResponse {
    private String requestSourcePath;
    private final HttpSession httpSession;
    private final Response response;
    private Boolean isNew;
    private Http11Header header;
    private String responseBody;

    public HttpServletResponse(HttpSession httpSession, Response response,String  requestSourcePath) {
        this.isNew = httpSession.isNew();
        this.httpSession = httpSession;
        this.response = response;
        this.requestSourcePath = requestSourcePath;
        super.sessionId = httpSession.getId();
        super.setCookieKey = response.getSetCookieKey();
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public Response getResponse() {
        return response;
    }
    public void setHeadersByData(String data) {
        this.header = new Http11Header(super.sessionId,requestSourcePath,super.setCookieKey);
        header.checkData(data);
    }
    public void setHeadersByLoginComplete() {
        setHeadersByData("LoginComplete");
    }
    public void setResponseBody(String data) {
        if(data.contains("User")) this.isNew = false ;
        this.responseBody = data;
    }

    public String getUserSessionInfo() {
        return httpSession.getId() +"="+this.isNew;
    }
    @Override
    public byte[] getHeader() {
        return header.getHeader();
    }
    @Override
    public byte[] getBody() {
        return responseBody.getBytes();
    }
    @Override
    public HttpSession updateSessionToSessionDB() {
        httpSession.setAttribute("user",getUserSessionInfo());
        return httpSession;
    }


}
