package org.apache.catalina.connector;


import org.apache.catalina.util.StringManager;
import org.apache.catalina.util.URLEncoder;
import org.apache.coyote.Adapter;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.tomcat.util.buf.MessageBytes;

public class CoyoteAdapter implements Adapter {
    protected static final boolean ALLOW_BACKSLASH = true;

    public static final int ADAPTER_NOTES = 0;
    protected StringManager sm;
    protected static URLEncoder urlEncoder;

    public CoyoteAdapter(Connector connector) {

    }

    @Override
    public void service(Request req, Response res) throws Exception {

    }

    protected boolean postParseRequest(Request req, Request request, Response res, Response response) throws java.lang.Exception {
        return true;
    }

    protected void parseSessionId(Request req, Request request) {

    }

    protected void convertURI(MessageBytes uri, Request request)
            throws Exception {

    }

    public static boolean normalize(MessageBytes uriMB) {
        return false;
    }

    public static boolean checkNormalize(MessageBytes uriMB) {
        return false;
    }

    protected static void copyBytes(byte[] b, int dest, int src, int len) {

    }
}
