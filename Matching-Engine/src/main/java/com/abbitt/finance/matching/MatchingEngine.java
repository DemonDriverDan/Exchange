package com.abbitt.finance.matching;


import com.abbitt.finance.event.TradeEvent;

public interface MatchingEngine {

    void handleTradeCreated(TradeEvent tradeEvent);

    OrderBook getBidBook();

    OrderBook getAskBook();
}
