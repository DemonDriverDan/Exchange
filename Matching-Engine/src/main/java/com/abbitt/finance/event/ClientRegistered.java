package com.abbitt.finance.event;

import java.nio.ByteBuffer;

public class ClientRegistered extends Command {

    public static final int MESSAGE_ID = 2;

    public ClientRegistered(int clientId) {
        super(clientId);
    }

    public ClientRegistered(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public void writeToBuffer(ByteBuffer buffer) {
        buffer.putInt(MESSAGE_ID);
        buffer.putInt(getClientId());
    }

    @Override
    public String toString() {
        return "ClientRegistered{clientId=" + getClientId() + "}";
    }
}
