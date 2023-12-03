package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.coyote.AbstractProcessor;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.Session;

public class HttpRequest extends Request {
    private String requestMethod;
    private String requestSourcePath;
    private String requestBody;
    private String header;
    private Boolean isCookie;
    private String sessionId;
    private Response response;

    @Override
    public void setHook(AbstractProcessor abstractProcessor) {
        super.setHook(abstractProcessor);
    }
    @Override
    public Response setResponse(Response response) {
        if(isRequestHaveCookie(header)) {
            this.sessionId = extractSessionId(header);
            this.sessionId = Session.checkSessionIdExpire(sessionId);
        }else{
            response.setSetCookieKey(true);
            this.sessionId = Session.addGuestVisitor();
        }
        response.setSessionId(sessionId);
        this.response = response;
        return this.response;
    }
    @Override
    public void setBufferReader(BufferedReader bf) {
        String[] httpStartLine = getStartLine(bf);
        this.requestMethod = httpStartLine[0];
        this.requestSourcePath = httpStartLine[1];
        this.header = readHeaders(bf);
        readRequestBody(bf, requestMethod, header);
    }
    private String[] getStartLine(BufferedReader bf) {
        String startLine = readLine(bf);
        return startLine.split(" ");
    }
    private String readHeaders(BufferedReader bf) {
        StringBuilder headerBuilder = new StringBuilder();
        String line;
        while (!(line = readLine(bf)).isEmpty()) {
            headerBuilder.append(line).append("\n");
        }
        return headerBuilder.toString();
    }
    private String readLine(BufferedReader bf){
        try{
            return bf.readLine();
        }catch (IOException e) {
            return null;
        }
    }

    private void readRequestBody(BufferedReader bf, String requestMethod, String headers){
        if (requestMethod.startsWith("POST")) {
            int contentLength = getContentLength(headers);
            char[] body = new char[contentLength];
            try {
                bf.read(body, 0, contentLength);
                this.requestBody =  new String(body);
            }catch (IOException e) {
                throw new IllegalArgumentException("[ERROR]");
            }
        }
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
    @Override
    public String getRequestMethod() {
        return requestMethod;
    }
    @Override
    public String getRequestSourcePath() {
        return requestSourcePath;
    }
    @Override
    public String getRequestBody() {
        return requestBody;
    }
    @Override
    public String getHeader() {
        return header;
    }
    @Override
    public Boolean getIsCookie() {
        return isCookie;
    }
    @Override
    public String getSessionId() {
        return sessionId;
    }
    @Override
    public Response getResponse() {
        return response;
    }

    private boolean isRequestHaveCookie(String headers) {
        this.isCookie = headers.contains("Cookie");
        return this.isCookie;
    }
    private String extractSessionId(String headers) {
        String sessionId = "";
        int sessionIdStart = headers.indexOf("JSESSIONID=");
        if (sessionIdStart != -1) {
            sessionIdStart += "JSESSIONID=".length();
            int sessionIdEnd = headers.indexOf('\n', sessionIdStart);
            if (sessionIdEnd == -1) {
                sessionId = headers.substring(sessionIdStart);
            } else {
                sessionId = headers.substring(sessionIdStart, sessionIdEnd);
            }
        }
        return sessionId;
    }

}
