package com.abbitt.finance.matching;


import com.abbitt.finance.event.OrderCreated;
import com.abbitt.finance.event.OrderTraded;

public interface MatchingEngine {

    OrderBook getBidBook();

    OrderBook getAskBook();

    OrderTraded takeLiquidity(OrderCreated event) throws NoQuantityException;

    OrderTraded addLiquidity(OrderCreated event);
}
