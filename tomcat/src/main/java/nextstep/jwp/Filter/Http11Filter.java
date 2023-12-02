package nextstep.jwp.Filter;

import jakarta.servlet.http.HttpSession;
import nextstep.jwp.DispatcherServlet.controller.RequestController;
import nextstep.jwp.DispatcherServlet.dto.HttpServletRequest;
import nextstep.jwp.DispatcherServlet.dto.HttpServletResponse;
import org.apache.coyote.Request;
import org.apache.coyote.Response;

// 1차 , req , res 의 대한 auto login 지원
// 2차 , header 최종 파싱

public class Http11Filter {
    private final RequestController requestController;

    public Http11Filter() {
        this.requestController = new RequestController();
    }
    public Response handleRequest(final Request request,final  Response response ,final HttpSession httpSession) {
        var httpServletRequest = new HttpServletRequest(httpSession,request);
        var httpServletResponse = new HttpServletResponse(httpSession,response);
        if(httpSession.isNew()) {
            var requestResponse = requestController.requestHandle(httpServletRequest,httpServletResponse);
            httpSession.setAttribute("user",requestResponse.getUserSessionInfo());
            return httpServletResponse;
        }
        if(isAutoLogin(httpSession,request)) {
            return httpServletResponse;
        }
        return requestController.requestHandle(httpServletRequest,httpServletResponse);
    }
    private Boolean isAutoLogin(HttpSession httpSession , Request request) {
        return !httpSession.isNew() & isServiceLoginRequest(request);
    }
    private Boolean isServiceLoginRequest(Request request) {
        String requestMethod = request.getRequestMethod();
        String requestSourcePath = request.getRequestSourcePath();
        return (requestSourcePath.contains("/login") & requestMethod.equals("POST") || (requestSourcePath.contains("/login?") & requestMethod.equals("GET")));
    }
}
