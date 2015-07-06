package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public class OrderRejected extends Command {

    public static final int MESSAGE_ID = 6;

    private final String reason;

    public OrderRejected(int clientId, String reason) {
        super(clientId);
        this.reason = reason;
    }

    public OrderRejected(ByteBuffer buffer) {
        super(buffer);
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        reason = new String(bytes);
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void writeToBuffer(ByteBuffer buffer) {
        buffer.putInt(MESSAGE_ID);
        buffer.putInt(getClientId());
        buffer.putInt(reason.length());
        buffer.put(reason.getBytes());
    }

    @Override
    public String toString() {
        return "OrderRejected{" +
                "clientId=" + getClientId() +
                ", reason='" + reason + '\'' +
                '}';
    }
}
