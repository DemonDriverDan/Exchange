package com.abbitt.finance.event;


public abstract class Event {

    private final int clientId;

    public Event(int clientId) {
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Client Id: " + clientId;
    }
}
