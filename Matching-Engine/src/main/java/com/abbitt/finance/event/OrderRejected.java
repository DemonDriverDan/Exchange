package com.abbitt.finance.event;


public class OrderRejected extends Event {

    private final String reason;

    public OrderRejected(int clientId, String reason) {
        super(clientId);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
