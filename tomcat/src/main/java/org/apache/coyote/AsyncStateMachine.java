package org.apache.coyote;

public class AsyncStateMachine {
    private final AbstractProcessor processor;

    AsyncStateMachine(AbstractProcessor processor) {
        this.processor = processor;
    }
}
