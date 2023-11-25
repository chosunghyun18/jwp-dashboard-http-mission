package nextstep.jwp.DispatcherServlet.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
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
import nextstep.jwp.DispatcherServlet.dto.ResponseDto;
import nextstep.jwp.DispatcherServlet.controller.RequestController;
import org.apache.catalina.util.SessionManager;
import org.apache.coyote.http11.Http11Processor;
import org.apache.tomcat.util.http.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    private final RequestController requestController;
    private final SessionManager sessionManager;

    public RequestHandler() {
        requestController = new RequestController();
        sessionManager = new SessionManager();
    }

    public ResponseDto getResponse(BufferedReader bf) throws IOException {
        String[] httpStartLine = getStartLine(bf);
        final String requestMethod = httpStartLine[0];
        final String requestSourcePath = httpStartLine[1];

        String headers = readHeaders(bf);
        final boolean checkCookie = isRequestHaveCookie(headers);
        String requestBody = readRequestBody(bf, requestMethod, headers);

        if (isMemberRequest(requestMethod,requestSourcePath)) {
            String sessionId = headers.substring(headers.lastIndexOf("JSESSIONID="));
            if(checkCookie) {
                HttpSession session  = sessionManager.findSession(sessionId);
                if(session.isNew()) {
                    ResponseDto response = requestController.handleMember(requestMethod, requestSourcePath, requestBody);
                    sessionManager.add(new Session(response.getData()));
                    return responseBuilderStaticPage(response,checkCookie);
                }
                else {
                    var user = session.getAttribute("user");
                    ResponseDto response = responseBuilderStaticPage(new ResponseDto(buildSessionFindHeader(session),user.toString()),checkCookie);
                    return responseBuilderStaticPage(response,checkCookie);
                }
            }
            ResponseDto response = requestController.handleMember(requestMethod, requestSourcePath, requestBody);
            return responseBuilderStaticPage(response,checkCookie);
        }

        if (isImageRequest(requestMethod, requestSourcePath)) {
            return handleImageRequest(requestSourcePath);
        }

        return responseBuilderStaticPage(requestSourcePath,checkCookie);
    }

    private boolean isRequestHaveCookie(String headers) {
        return headers.contains("Cookie") ;
    }

    private String readHeaders(BufferedReader bf) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        String line;
        while (!(line = bf.readLine()).isEmpty()) {
            headerBuilder.append(line).append("\n");
        }
        return headerBuilder.toString();
    }

    private String readRequestBody(BufferedReader bf, String requestMethod, String headers) throws IOException {
        if (requestMethod.startsWith("POST")) {
            int contentLength = getContentLength(headers);
            char[] body = new char[contentLength];
            bf.read(body, 0, contentLength);
            return new String(body);
        }
        return "";
    }

    private boolean isMemberRequest(String requestMethod, String requestSourcePath) {
        return (requestSourcePath.contains("/login")& requestMethod.equals("POST")
                || (requestSourcePath.contains("/login")&requestMethod.equals("get")
                || (requestSourcePath.contains("/register")&requestMethod.equals("POST")
        )));
    }

    private boolean isImageRequest(String requestMethod, String requestSourcePath) {
        return requestMethod.equals("GET") && requestSourcePath.contains("/assets/img");
    }

    private String[] getStartLine(BufferedReader bf) throws IOException {
        String startLine = bf.readLine();
        return startLine.split(" ");
    }

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
    private String buildSessionFindHeader(HttpSession session) throws IOException{
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
                "Cookie: " + "JSESSIONID="+session.getId(),
                "",
                "");
        return responseHeader;
    }
    public ResponseDto responseBuilderStaticPage(String requestSourcePath,Boolean isCookie) throws IOException {
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
        String responseHeader="";
        if(isCookie) {
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
                "Set-Cookie: " + "JSESSIONID="+UUID.randomUUID(),
                "",
                "");
        return new ResponseDto(responseHeader, filesIO);
    }

    public ResponseDto responseBuilderStaticPage(ResponseDto responseDto,Boolean isCookie) throws IOException {
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
        if(isCookie){
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
                "Set-Cookie: " + "JSESSIONID="+UUID.randomUUID(),
                "",
                "");

        return new ResponseDto(responseHeader, filesIO);
    }

    private int getContentLength(String header) {
        String[] lines = header.split("\n");
        for (String line : lines) {
            if (line.startsWith("Content-Length:")) {
                return Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return 0;
    }
}
