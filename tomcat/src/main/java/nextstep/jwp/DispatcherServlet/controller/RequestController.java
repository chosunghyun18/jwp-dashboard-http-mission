package nextstep.jwp.DispatcherServlet.controller;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import nextstep.jwp.DispatcherServlet.dto.ResponseDto;
import nextstep.jwp.spring.presantation.MemberController;
import nextstep.jwp.spring.presantation.MemberLoginRequest;
import nextstep.jwp.spring.presantation.MemberRegisterRequest;

public class RequestController {
    public final MemberController memberController ;

    public RequestController() {
        memberController = new MemberController();
    }

    public ResponseDto handleMember(String requestMethod, String requestSourcePath) {
        String data = "";
        if(requestSourcePath.contains("/login?") & requestMethod.equals("GET"))
        {   var request = handleLoginRequest(requestSourcePath);
            if(request != null) data = memberController.getMemberLoginInfo(request);
        }
        if(requestSourcePath.contains("/login?") & requestMethod.equals("POST"))
        {   var request = handleLoginRequest(requestSourcePath);
            if(request != null) data = memberController.getMemberLoginInfo(request);
        }
        if(requestSourcePath.equals("/register?") & requestMethod.equals("POST"))
        {   var request = handleRegisterRequest(requestSourcePath);
            if(request != null) data =  memberController.saveMemberInfo(request);
        }

        String header = buildHeader(data);
        return new ResponseDto(header,data);
    }
    private MemberLoginRequest handleLoginRequest(String requestSourcePath) {
        String[] params = requestSourcePath.substring("/login?".length()).split("&");
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

    private MemberRegisterRequest handleRegisterRequest(String requestSourcePath) {
        String[] params = requestSourcePath.substring("/register?".length()).split("&");
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
            return new MemberRegisterRequest(account,email,password) ;
        }
        return null;
    }

    private String buildHeader(String data) {
        String header = String.join("\r\n",
                "HTTP/1.1 " + HTTP_OK + " " + "OK",
                "Content-Type: text/html",
                "Content-Length: " + data.length(),
                "",
                "");
        if(data.contains("[ERROR]")) {
            String.join("\r\n",
                "HTTP/1.1 " + 401 + " " + "BADR_REQUEST",
                "Content-Type: text/html",
                "Content-Length: " + data.length(),
                "",
                "");
        }
        if(data.isEmpty()) {
            header =String.join("\r\n",
                    "HTTP/1.1 " + HTTP_NO_CONTENT + " " + "BAD_REQUEST",
                    "Content-Type: text/html",
                    "Content-Length: " + 0,
                    "",
                    "");
        }
        return header;
    }
}
