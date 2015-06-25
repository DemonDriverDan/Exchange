package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public abstract class Event {

    private final int clientId;

    public Event(int clientId) {
        this.clientId = clientId;
    }

    public Event(ByteBuffer buffer) {
        clientId = buffer.getInt();
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "clientId=" + clientId +
                '}';
    }
}
