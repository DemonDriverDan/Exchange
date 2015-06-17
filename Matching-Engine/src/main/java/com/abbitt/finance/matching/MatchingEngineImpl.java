package com.abbitt.finance.matching;


import com.abbitt.finance.Side;
import com.abbitt.finance.event.OrderCreated;
import com.abbitt.finance.event.OrderTraded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchingEngineImpl implements MatchingEngine {
    private static final Logger LOG = LoggerFactory.getLogger(MatchingEngine.class);

    private OrderBook bidBook;
    private OrderBook askBook;

    public MatchingEngineImpl() {
        bidBook = OrderBook.createBidBook();
        askBook = OrderBook.createAskBook();
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
    public OrderTraded addLiquidity(OrderCreated event) {
        return null;
    }

    @Override
    public OrderTraded takeLiquidity(OrderCreated event) throws NoQuantityException {
        long quantity = -1;

        // Could add support for market orders
        if (event.getSide() == Side.BUY) {
            quantity = processEvent(askBook, event);
        } else if (event.getSide() == Side.SELL) {
            quantity = processEvent(bidBook, event);
        }

        if (quantity == -1) {
            LOG.error("Unable to calculate quantity");
            return null;
        }
        // Add to client repository log?
        return new OrderTraded(event.getClientId(), event.getPrice(), quantity, event.getSide());
    }

    public static long processEvent(OrderBook book, OrderCreated event) throws NoQuantityException {
        if (book.hasQuantityAtPrice(event.getPrice())) {
            return book.takeQuantity(event.getPrice(), event.getQuantity());
        } else {
            throw new NoQuantityException(event.getPrice());
        }
    }
}
