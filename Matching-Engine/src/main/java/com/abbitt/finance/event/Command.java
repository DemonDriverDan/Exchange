package com.abbitt.finance.event;


import java.nio.ByteBuffer;

public abstract class Command extends Event {

    public Command(int clientId) {
        super(clientId);
    }

    public abstract void writeToBuffer(ByteBuffer buffer);
}
