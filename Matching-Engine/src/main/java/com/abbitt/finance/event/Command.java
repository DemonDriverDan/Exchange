package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public abstract class Command extends Event {

    public Command(int clientId) {
        super(clientId);
    }

    public Command(ByteBuffer buffer) {
        super(buffer);
    }

    public abstract void writeToBuffer(ByteBuffer buffer);
}
