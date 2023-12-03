package nextstep.jwp.Filter;

import jakarta.servlet.http.HttpSession;
import nextstep.jwp.DispatcherServlet.controller.RequestController;
import nextstep.jwp.util.HttpServletRequest;
import nextstep.jwp.util.HttpServletResponse;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.coyote.http11.HttpResponse;

public class Http11Filter {
    private final RequestController requestController;

    public Http11Filter() {
        this.requestController = new RequestController();
    }
    public HttpResponse handleRequest(final Request request, final  Response response , final HttpSession httpSession) {
        var httpServletRequest = new HttpServletRequest(httpSession,request);
        var httpServletResponse = new HttpServletResponse(httpSession,response,httpServletRequest.getRequestSourcePath());
        if(isAutoLogin(httpSession,request)) {
            httpServletResponse.setHeadersByLoginComplete();
            return httpServletResponse;
        }
        return requestController.requestHandle(httpServletRequest,httpServletResponse);
    }
    private Boolean isAutoLogin(HttpSession httpSession , Request request) {
        return isUserSession(httpSession) & isServiceLoginRequest(request) ;
    }
    private Boolean isServiceLoginRequest(Request request) {
        String requestMethod = request.getRequestMethod();
        String requestSourcePath = request.getRequestSourcePath();
        return (requestSourcePath.contains("/login") & requestMethod.equals("POST") || (requestSourcePath.contains("/login?") & requestMethod.equals("GET")));
    }
    private Boolean isUserSession(HttpSession httpSession) {
        return httpSession.getAttribute("user").equals("user");
    }

}
