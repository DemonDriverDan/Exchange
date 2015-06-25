package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public class ClientRegisterRequested extends Event {

    public static final int MESSAGE_ID = 1;

    public ClientRegisterRequested(int clientId) {
        super(clientId);
    }

    public ClientRegisterRequested(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public String toString() {
        return "ClientRegisterRequested{clientId=" + getClientId() + "}";
    }
}
