package nextstep.jwp.util;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class Http11Header {
    private String data;
    private final String sessionId;
    private final String requestSourcePath;
    private final Boolean addSetCookie;
    private String headerMessage;

    public Http11Header(String sessionId, String requestSourcePath,Boolean addSetCookie) {
        this.sessionId = sessionId;
        this.requestSourcePath = requestSourcePath;
        this.addSetCookie = addSetCookie;
    }

    public ResponseDto checkData(String data) {
        ResponseDto res = null;
        if(data.equals("")) {
            res = responseBuilderStaticPage(data);
            this.headerMessage = res.getHeader();
        }
        if(data.contains("User") || data.equals("LoginComplete")) {
            res = responseBuilderStaticPageCompleteLogin();
            this.headerMessage = res.getHeader();
        }
        return res;
    }

    public ResponseDto responseBuilderStaticPage(String data) {
        String requestSourcePath = this.requestSourcePath;
        String contentTypeFile = "text/html";
        Integer responseHeaderCode = 200;
        String responseHeaderMessage = "OK";
        if (requestSourcePath.equals("/")) {
            requestSourcePath = "/Home.txt";
        } else if (requestSourcePath.contains(".html") || requestSourcePath.contains("/js/scripts.js") || requestSourcePath.contains("/assets") || requestSourcePath.contains(".css")) {
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
        byte[] filesIO = readAllBytesFromFile(path);
        var contentLength = filesIO.length;
        String responseHeader = "";

        if (!addSetCookie) {
            responseHeader = String.join("\r\n",
                    "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage + " ",
                    "Content-Type: " + contentTypeFile + ";charset=utf-8 ",
                    "Content-Length: " + contentLength + " ",
                    "",
                    "");
            return new ResponseDto(responseHeader, filesIO);
        }
        responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Set-Cookie: " + "JSESSIONID=" + sessionId,
                "",
                "");
        return new ResponseDto(responseHeader, filesIO);
    }
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
    private ResponseDto handleImageRequest() throws IOException {

        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO =  readAllBytesFromFile(path);

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

    public ResponseDto responseBuilderStaticPageCompleteLogin()  {
        String requestSourcePath = "/index.html";
        Integer responseHeaderCode = 302;
        String responseHeaderMessage = "Found";
        String contentTypeFile = "text/html";

        final InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final String actual = buildActual(inputStream);
        if (actual.contains("ERROR")) {
            requestSourcePath = "/401.html";
            responseHeaderCode = 401;
            responseHeaderMessage = "Unauthorized";
        }

        URL resource = getClass().getClassLoader().getResource("static" + requestSourcePath);
        final Path path = new File(resource.getFile()).toPath();
        byte[] filesIO = readAllBytesFromFile(path);

        var contentLength = filesIO.length;
        if (!addSetCookie) {
            String responseHeader = String.join("\r\n",
                    "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                    "Content-Type: " + contentTypeFile + ";charset=utf-8",
                    "Content-Length: " + contentLength,
                    "Location: " + requestSourcePath,
                    "",
                    "");
            return new ResponseDto(responseHeader, filesIO);
        }
        String responseHeader = String.join("\r\n",
                "HTTP/1.1 " + responseHeaderCode + " " + responseHeaderMessage,
                "Content-Type: " + contentTypeFile + ";charset=utf-8",
                "Content-Length: " + contentLength,
                "Location: " + requestSourcePath,
                "Set-Cookie: " + "JSESSIONID=" + sessionId,
                "",
                "");

        return new ResponseDto(responseHeader, filesIO);
    }

    public byte[] getHeader() {
        return headerMessage.getBytes();
    }
    private String buildActual(InputStream inputStream) {
        try{
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }catch (IOException e) {
            return "ERROR";
        }
    }
    private byte[] readAllBytesFromFile(final Path path) {
        try{
        return Files.readAllBytes(path);
        }catch (IOException e) {
            return new byte[0];
        }
    }
}
