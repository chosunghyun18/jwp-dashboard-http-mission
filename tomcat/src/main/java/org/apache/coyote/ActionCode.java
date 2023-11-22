package org.apache.coyote;
public enum ActionCode {
    ACTION_ACK(0),                            // Acknowledgement
    ACTION_CLIENT_FLUSH(1),                   // Client-initiated flush
    ACTION_CLOSE(2),                          // Close connection
    ACTION_COMMIT(3),                         // Commit
    ACTION_CUSTOM(4),                         // Custom action
    ACTION_NEW_REQUEST(5),                    // New request creation
    ACTION_POST_REQUEST(6),                   // Post-request hook
    ACTION_REQ_HOST_ADDR_ATTRIBUTE(7),        // Fetch remote host address
    ACTION_REQ_HOST_ATTRIBUTE(8),             // Fetch remote host
    ACTION_REQ_LOCAL_ADDR_ATTRIBUTE(9),       // Fetch local address
    ACTION_REQ_LOCAL_NAME_ATTRIBUTE(10),      // Fetch local name
    ACTION_REQ_LOCALPORT_ATTRIBUTE(11),       // Fetch socket local port
    ACTION_REQ_REMOTEPORT_ATTRIBUTE(12),      // Fetch socket remote port
    ACTION_REQ_SET_BODY_REPLAY(13),           // Set body replay for FORM auth
    ACTION_REQ_SSL_ATTRIBUTE(14),             // Fetch SSL attributes
    ACTION_REQ_SSL_CERTIFICATE(15),           // Fetch SSL certificate
    ACTION_RESET(16),                         // Reset
    ACTION_START(17),                         // Start
    ACTION_STOP(18),                          // Stop
    ACTION_WEBAPP(19);                        // Web application related action

    private final int code;

    ActionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
