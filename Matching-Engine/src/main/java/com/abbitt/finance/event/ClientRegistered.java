package com.abbitt.finance.event;


public class ClientRegistered extends Event {

    public ClientRegistered(int clientId) {
        super(clientId);
    }
}
