package com.abbitt.finance.command;


public interface TradeCommandHandler {

    void ackTradeCreated();

    void tradeExecuted();
}
