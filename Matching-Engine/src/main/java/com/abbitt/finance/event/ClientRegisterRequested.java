package com.abbitt.finance.event;


public class ClientRegisterRequested extends Event {

    public ClientRegisterRequested(int clientId) {
        super(clientId);
    }
}
