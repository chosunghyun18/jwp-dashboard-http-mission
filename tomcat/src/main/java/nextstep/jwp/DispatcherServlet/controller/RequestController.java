package nextstep.jwp.DispatcherServlet.controller;

import nextstep.jwp.util.HttpServletRequest;
import nextstep.jwp.util.HttpServletResponse;

import nextstep.jwp.spring.presantation.MemberController;
import nextstep.jwp.util.Parser;

public class RequestController {
    public final MemberController memberController;

    public RequestController() {
        memberController = new MemberController();
    }

    public HttpServletResponse requestHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String requestSourcePath = httpServletRequest.getRequestSourcePath();
        String data = "";
        if (requestSourcePath.contains("/login")) {
            var request = Parser.parserForLogin(httpServletRequest);
            if (request != null) {
                data = memberController.getMemberLoginInfo(request).toString();
            }
        }
        if (requestSourcePath.contains("/register")) {
            var request = Parser.parserForRegister(httpServletRequest);
            if (request != null) {
                data = memberController.saveMemberInfo(request).toString();
            }
        }
        httpServletResponse.setResponseBody(data);
        httpServletResponse.setHeadersByData(data);
        return httpServletResponse;
    }
}
