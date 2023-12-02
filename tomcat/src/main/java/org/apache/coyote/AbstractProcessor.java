package org.apache.coyote;

public abstract class AbstractProcessor {
     protected final AsyncStateMachine asyncStateMachine;
    private volatile long asyncTimeout = -1;
    protected Request request;
    protected Response response;
    protected AbstractProcessor() {
        asyncStateMachine = new AsyncStateMachine(this);
        response.setHook(this);
//        request.setResponse(response);
        request.setHook(this);
    }
}
