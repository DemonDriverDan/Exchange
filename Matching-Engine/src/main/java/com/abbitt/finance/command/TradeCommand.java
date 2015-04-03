package com.abbitt.finance.command;


import com.abbitt.finance.event.TradeEvent;

public class TradeCommand extends TradeEvent {

    private String outcome;

    public TradeCommand(TradeEvent event, String outcome) {
        super(event.getPrice(), event.getQuantity(), event.getClientId(), event.getSide());
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }
}
