package com.abbitt.finance.event;

import com.abbitt.finance.Instrument;
import com.abbitt.finance.Side;
import com.abbitt.finance.command.TradeCommandHandler;
import com.abbitt.finance.matching.MatchingEngine;


public class TradeEventHandlerImpl implements TradeEventHandler {

    private TradeCommandHandler tradeCommandHandler;
    private MatchingEngine matchingEngine;

    public TradeEventHandlerImpl(TradeCommandHandler tradeCommandHandler, MatchingEngine matchingEngine) {
        this.tradeCommandHandler = tradeCommandHandler;
        this.matchingEngine = matchingEngine;
    }

    @Override
    public void tradeCreated(double price, long quantity, Side side, Instrument instrument, int clientId) {
        tradeCommandHandler.ackTradeCreated();
        matchingEngine.handleTradeCreated(new TradeEvent(price, quantity, clientId, side));
    }

    @Override
    public void tradeAmended() {

    }
}
