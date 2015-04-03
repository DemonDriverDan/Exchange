package com.abbitt.finance.matching;


import com.abbitt.finance.Side;
import com.abbitt.finance.command.TradeCommandHandler;
import com.abbitt.finance.event.TradeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchingEngineImpl implements MatchingEngine {

    private static final Logger LOG = LoggerFactory.getLogger(MatchingEngine.class);
    private OrderBook bidBook;
    private OrderBook askBook;
    private TradeCommandHandler tradeCommandHandler;

    public MatchingEngineImpl(TradeCommandHandler tradeCommandHandler) {
        bidBook = OrderBook.createBidBook(tradeCommandHandler);
        askBook = OrderBook.createAskBook(tradeCommandHandler);
        this.tradeCommandHandler = tradeCommandHandler;
    }

    @Override
    public OrderBook getBidBook() {
        return bidBook;
    }

    @Override
    public OrderBook getAskBook() {
        return askBook;
    }

    @Override
    public void handleTradeCreated(TradeEvent tradeEvent) {
        LOG.info("New trade: {}", tradeEvent.toString());
        if (tradeEvent.getSide() == Side.BUY) {
            if (tradeEvent.getPrice() >= askBook.getInsidePrice() && askBook.hasQuantityAtPrice(tradeEvent.getPrice())) {
                askBook.takeQuantity(tradeEvent);
            } else { // Post
                bidBook.addTrade(tradeEvent);
            }
        } else {
            if (tradeEvent.getPrice() <= bidBook.getInsidePrice() && bidBook.hasQuantityAtPrice(tradeEvent.getPrice())) {
                bidBook.takeQuantity(tradeEvent);
            } else {
                askBook.addTrade(tradeEvent);
            }
        }
    }
}
