package com.abbitt.finance.matching;


import com.abbitt.finance.Side;
import com.abbitt.finance.event.TradeEvent;
import com.abbitt.finance.TradeHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchingEngineTest {

    private MatchingEngine engine;

    @Before
    public void setUp() {
        engine = new MatchingEngineImpl(new TradeHandler()); // TODO Fix by mocking TradeHandler
    }

    @Test
    public void postTest() {
        engine.handleTradeCreated(new TradeEvent(100, 1000, 0, Side.BUY));
        assertEquals(1000, engine.getBidBook().quantityAtPrice(100));
        assertEquals(100, engine.getBidBook().getInsidePrice(), 0.0001);
    }

    @Test
    public void postAndTake() {
        engine.handleTradeCreated(new TradeEvent(100, 1000, 0, Side.BUY));
        engine.handleTradeCreated(new TradeEvent(100, 500, 0, Side.SELL));
        assertEquals(500, engine.getBidBook().quantityAtPrice(100));
    }

    public void longTest() {
        for (double price = 98; price >= 94; price--) {
            for (long quantity = 1000; quantity <= 5000; quantity += 1000) {
                engine.handleTradeCreated(new TradeEvent(price, quantity, 0, Side.BUY));
            }
        }

        for (double price = 101; price <= 105; price++) {
            for (long quantity = 1000; quantity <= 5000; quantity += 1000) {
                engine.handleTradeCreated(new TradeEvent(price, quantity, 0, Side.SELL));
            }
        }

        engine.handleTradeCreated(new TradeEvent(102, 4000, 0, Side.BUY));
        engine.handleTradeCreated(new TradeEvent(102, 4000, 0, Side.BUY));
        engine.handleTradeCreated(new TradeEvent(102, 4000, 0, Side.BUY));
        engine.handleTradeCreated(new TradeEvent(102, 4000, 0, Side.BUY));

        engine.handleTradeCreated(new TradeEvent(102, 1000, 0, Side.BUY));
        engine.handleTradeCreated(new TradeEvent(102, 1000, 0, Side.SELL));
    }
}
