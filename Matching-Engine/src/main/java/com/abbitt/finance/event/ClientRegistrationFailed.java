package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public class ClientRegistrationFailed extends Command {

    private static final int MESSAGE_ID = 3;

    private final String reason;

    public ClientRegistrationFailed(int clientId, String reason) {
        super(clientId);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void writeToBuffer(ByteBuffer buffer) {

    }
}
