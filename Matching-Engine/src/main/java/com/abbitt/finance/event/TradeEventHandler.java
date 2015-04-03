package com.abbitt.finance.event;


import com.abbitt.finance.Instrument;
import com.abbitt.finance.Side;

public interface TradeEventHandler {

    void tradeCreated(double price, long quantity, Side side, Instrument instrument, int clientId);

    void tradeAmended();
}
