package org.apache.coyote;

public interface Adapter {
    // Call the service method, and notify all listeners

    void service(Request req, Response res) throws java.lang.Exception;
}
