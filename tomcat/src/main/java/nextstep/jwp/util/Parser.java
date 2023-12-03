package nextstep.jwp.util;

import nextstep.jwp.spring.presantation.MemberLoginRequest;
import nextstep.jwp.spring.presantation.MemberRegisterRequest;

public class Parser {
    public static MemberLoginRequest parserForLogin(HttpServletRequest httpServletRequest) {
        var requestMethod = httpServletRequest.getRequestMethod();
        if (requestMethod.equals("GET")) {
            return handleLoginRequest(httpServletRequest.getRequestSourcePath());
        }
        if (requestMethod.equals("POST")) {
            return handleLoginRequestByRequestBody(httpServletRequest.getRequestBody());
        }
        return null;
    }

    private static MemberLoginRequest handleLoginRequestByRequestBody(String requestBody) {
        String[] params = requestBody.split("&");
        return getLoginRequest(params);
    }

    private static  MemberLoginRequest handleLoginRequest(String requestSourcePath) {
        String[] params = requestSourcePath.substring("/login?".length()).split("&");
        return getLoginRequest(params);
    }

    private static MemberLoginRequest getLoginRequest(String[] params) {
        String account = null;
        String password = null;

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                if ("account".equals(key)) {
                    account = value;
                } else if ("password".equals(key)) {
                    password = value;
                }
            }
        }

        if (account != null && password != null) {
            return new MemberLoginRequest(account, password);
        }
        return null;
    }

    public static MemberRegisterRequest parserForRegister(HttpServletRequest httpServletRequest) {
        String requestBody = httpServletRequest.getRequestBody();
        String[] params = requestBody.split("&");
        String account = null;
        String email = null;
        String password = null;
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                if ("account".equals(key)) {
                    account = value;
                } else if ("email".equals(key)) {
                    email = value;
                } else if ("password".equals(key)) {
                    password = value;
                }
            }
        }

        if (account != null && email != null && password != null) {
            return new MemberRegisterRequest(account, email, password);
        }
        return null;
    }
}
