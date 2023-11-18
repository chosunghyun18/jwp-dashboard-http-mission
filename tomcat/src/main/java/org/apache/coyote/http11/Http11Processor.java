package org.apache.coyote.http11;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.Socket;

import nextstep.jwp.DispatcherServlet.handler.RequestHandler;
import nextstep.jwp.exception.UncheckedServletException;

import org.apache.coyote.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final RequestHandler requestHandler;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
        this.requestHandler = new RequestHandler();
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
            String header = bf.readLine();
            final var response = requestHandler.getResponse(header);
            outputStream.write(response.getHeader());
            outputStream.write(response.getData());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
