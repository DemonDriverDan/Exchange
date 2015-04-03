package com.abbitt.finance.command;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeCommandHandlerImpl implements TradeCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TradeCommandHandler.class);

    @Override
    public void ackTradeCreated() {
        LOG.info("Trade acked");
    }

    @Override
    public void tradeExecuted() {
        LOG.info("Trade executed");
    }
}
