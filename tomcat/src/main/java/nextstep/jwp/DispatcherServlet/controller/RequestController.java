package nextstep.jwp.DispatcherServlet.controller;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import nextstep.jwp.DispatcherServlet.dto.HttpServletRequest;
import nextstep.jwp.DispatcherServlet.dto.HttpServletResponse;
import nextstep.jwp.DispatcherServlet.dto.ResponseDto;
import nextstep.jwp.spring.presantation.MemberController;
import nextstep.jwp.spring.presantation.MemberLoginRequest;
import nextstep.jwp.spring.presantation.MemberRegisterRequest;
import org.apache.coyote.Request;
import org.apache.coyote.Response;

public class RequestController {
    public final MemberController memberController;

    public RequestController() {
        memberController = new MemberController();
    }

    public HttpServletResponse requestHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String requestSourcePath = httpServletRequest.getRequestSourcePath();
        String data = "";
        if (requestSourcePath.contains("/login")) {
            var request = parserForLogin(httpServletRequest);
            if (request != null) {
                data = memberController.getMemberLoginInfo(request);
            }
        }
        if (requestSourcePath.contains("/register")) {
            var request = parserForRegister(httpServletRequest);
            if (request != null) {
                data = memberController.saveMemberInfo(request);
            }
        }
        String header = buildHeader(data);
        httpServletResponse.setHeaders(header);
        httpServletResponse.setData(data);
        return httpServletResponse;
    }

    private MemberLoginRequest parserForLogin(HttpServletRequest httpServletRequest) {
        var requestMethod = httpServletRequest.getRequestMethod();
        if (requestMethod.equals("GET")) {
            return handleLoginRequest(httpServletRequest.getRequestSourcePath());
        }
        if (requestMethod.equals("POST")) {
            return handleLoginRequestByRequestBody(httpServletRequest.getRequestBody());
        }
        return null;
    }

    private MemberLoginRequest handleLoginRequestByRequestBody(String requestBody) {
        String[] params = requestBody.split("&");
        return getLoginRequest(params);
    }

    private MemberLoginRequest handleLoginRequest(String requestSourcePath) {
        String[] params = requestSourcePath.substring("/login?".length()).split("&");
        return getLoginRequest(params);
    }

    private MemberLoginRequest getLoginRequest(String[] params) {
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

    private MemberRegisterRequest parserForRegister(HttpServletRequest httpServletRequest) {
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

    /*
    header Parser
     */
    private String buildHeader(String data) {
        String header = String.join("\r\n",
                "HTTP/1.1 " + HTTP_OK + " " + "OK",
                "Content-Type: text/html",
                "Content-Length: " + data.length(),
                "",
                "");
        if (data.contains("[ERROR]")) {
            String.join("\r\n",
                    "HTTP/1.1 " + 401 + " " + "BADR_REQUEST",
                    "Content-Type: text/html",
                    "Content-Length: " + data.length(),
                    "",
                    "");
        }
        if (data.isEmpty()) {
            header = String.join("\r\n",
                    "HTTP/1.1 " + HTTP_NO_CONTENT + " " + "BAD_REQUEST",
                    "Content-Type: text/html",
                    "Content-Length: " + 0,
                    "",
                    "");
        }
        return header;
    }


    //
    private ResponseDto handleImageRequest(String requestSourceHeader) throws IOException {
        String[] info = requestSourceHeader.split(" ");
        URL resource = getClass().getClassLoader().getResource("static" + info[1]);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
            gzip.write(filesIO);
            gzip.flush();
        }
        byte[] compressedData = obj.toByteArray();
        int contentLength = compressedData.length;
        String header = String.join("\r\n",
                "HTTP/1.1 " + HTTP_OK + " " + "OK",
                "Content-Type: image/svg+xml",
                "Content-Encoding: gzip",
                "Content-Length: " + contentLength,
                "",
                "");
        return new ResponseDto(header, compressedData);
    }

    private String buildSessionFindHeader(HttpSession session) throws IOException {
        String contentTypeFile = "text/html";
        Integer responseHeaderCode = 302;
        String responseHeaderMessage = "OK";
        String requestSourcePath = "/index.html";
        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        var contentLength = filesIO.length;
        String responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Cookie: " + "JSESSIONID=" + session.getId(),
                "",
                "");
        return responseHeader;
    }

    public ResponseDto responseBuilderStaticPage(ResponseDto responseDto, Boolean isCookie, String userSessionID)
            throws IOException {
        String requestSourcePath = "/index.html";
        Integer responseHeaderCode = 302;
        String responseHeaderMessage = "Found";
        String contentTypeFile = "text/html";
        final InputStream inputStream = new ByteArrayInputStream(responseDto.getData());
        final String actual = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (actual.contains("ERROR")) {
            requestSourcePath = "/401.html";
            responseHeaderCode = 401;
            responseHeaderMessage = "Unauthorized";
        }
        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        var contentLength = filesIO.length;
        String responseHeader = "";
        if (isCookie) {
            responseHeader = String.join("\r\n",
                    "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                    "Content-Type: " + contentTypeFile + ";charset=utf-8",
                    "Content-Length: " + contentLength,
                    "Location: " + requestSourcePath,
                    "Cookie: " + "JSESSIONID=" + userSessionID,
                    "",
                    "");
        }
        responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Set-Cookie: " + "JSESSIONID=" + userSessionID,
                "",
                "");

        return new ResponseDto(responseHeader, filesIO);
    }


    public ResponseDto responseBuilderStaticPage(Request request) throws IOException {
        String contentTypeFile = "text/html";
        Integer responseHeaderCode = 200;
        String responseHeaderMessage = "OK";
        if (requestSourcePath.equals("/")) {
            requestSourcePath = "/Home.txt";
        } else if (requestSourcePath.contains(".html") || requestSourcePath.contains("/js/scripts.js")
                || requestSourcePath.contains("/assets") || requestSourcePath.contains(".css")) {
            String contentTypeFileForm = requestSourcePath.substring(requestSourcePath.lastIndexOf(".") + 1);
            contentTypeFile = "text/" + contentTypeFileForm;
        } else if (requestSourcePath.equals("/register") || requestSourcePath.equals("/login")) {
            requestSourcePath = requestSourcePath + ".html";
        } else {
            requestSourcePath = "/404.html";
            responseHeaderCode = 404;
            responseHeaderMessage = "NOT FOUND";
        }
        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        var contentLength = filesIO.length;
        String responseHeader = "";
        if (isCookie) {
            responseHeader = String.join("\r\n",
                    "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage + " ",
                    "Content-Type: " + contentTypeFile + ";charset=utf-8 ",
                    "Content-Length: " + contentLength + " ",
                    "",
                    "");
        }
        responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Set-Cookie: " + "JSESSIONID=" + UUID.randomUUID(),
                "",
                "");
        return new ResponseDto(responseHeader, filesIO);
    }

    public ResponseDto responseBuilderStaticPage(Response response) throws IOException {
        String requestSourcePath = "/index.html";
        Integer responseHeaderCode = 302;
        String responseHeaderMessage = "Found";
        String contentTypeFile = "text/html";

        final InputStream inputStream = new ByteArrayInputStream(responseDto.getData());
        final String actual = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        if (actual.contains("ERROR")) {
            requestSourcePath = "/401.html";
            responseHeaderCode = 401;
            responseHeaderMessage = "Unauthorized";
        }

        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = Files.readAllBytes(path);
        var contentLength = filesIO.length;
        if (isCookie) {
            String responseHeader = String.join("\r\n",
                    "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                    "Content-Type: " + contentTypeFile + ";charset=utf-8",
                    "Content-Length: " + contentLength,
                    "Location: " + requestSourcePath,
                    "",
                    "");
        }
        String responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Set-Cookie: " + "JSESSIONID=" + UUID.randomUUID(),
                "",
                "");

        return new ResponseDto(responseHeader, filesIO);
    }

}
