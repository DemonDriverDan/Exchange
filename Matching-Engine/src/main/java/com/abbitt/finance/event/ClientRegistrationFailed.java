package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public class ClientRegistrationFailed extends Command {

    public static final int MESSAGE_ID = 3;

    private final String reason;

    public ClientRegistrationFailed(int clientId, String reason) {
        super(clientId);
        this.reason = reason;
    }

    public ClientRegistrationFailed(ByteBuffer buffer) {
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
        // To read, do buffer.getInt() for length then
        // byte[] bytes = new byte[length]
        // buffer.get(bytes)
        // String reason = new String(bytes)
    }

    @Override
    public String toString() {
        return "ClientRegistrationFailed{" +
                "clientId=" + getClientId() +
                ", reason='" + reason + '\'' +
                '}';
    }
}
