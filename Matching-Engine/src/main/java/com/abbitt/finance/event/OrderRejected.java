package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public class OrderRejected extends Command {

    private static final int MESSAGE_ID = 6;

    private final String reason;

    public OrderRejected(int clientId, String reason) {
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
