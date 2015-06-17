package com.abbitt.finance.event;


public class ClientRegistrationFailed extends Event {

    private final String reason;

    public ClientRegistrationFailed(int clientId, String reason) {
        super(clientId);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
