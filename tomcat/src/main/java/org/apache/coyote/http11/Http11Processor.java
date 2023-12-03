package org.apache.coyote.http11;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.Socket;

import org.apache.handler.RequestHandler;
import nextstep.jwp.exception.UncheckedServletException;

import org.apache.coyote.ActionCode;
import org.apache.coyote.ActionHook;
import org.apache.coyote.Adapter;
import org.apache.coyote.Processor;

import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.tomcat.util.threads.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor, ActionHook {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);
    protected ThreadPool threadPool;
    private final Socket connection;
    private final RequestHandler requestHandler;

    protected final Request request;
    protected Response response;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
        this.requestHandler = new RequestHandler();
        this.request = new HttpRequest();
        this.response = new HttpResponse();
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream(); final var outputStream = connection.getOutputStream()) {
            final InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            final InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            final BufferedReader bf = new BufferedReader(inputStreamReader);
            request.setBufferReader(bf);
            response = request.setResponse(response);
            final var httpResponse = requestHandler.getResponse(request,response);
            outputStream.write(httpResponse.getHeader());
            outputStream.write(httpResponse.getBody());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Adapter getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(Adapter adapter) {

    }

    @Override
    public void action(ActionCode actionCode, Object param) {

    }

    void setThreadPool(ThreadPool threadPool) {

    }

}
