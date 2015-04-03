package com.abbitt.finance.event;


import com.abbitt.finance.Side;

public class TradeEvent {

    private double price;
    private long quantity;
    private int clientId;
    private Side side;

    public TradeEvent(double price, long quantity, int clientId, Side side) {
        this.price = price;
        this.quantity = quantity;
        this.clientId = clientId;
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public int getClientId() {
        return clientId;
    }

    public Side getSide() {
        return side;
    }

    public void removeQuantity(long removeAmount) {
        quantity -= removeAmount;
    }

    @Override
    public String toString() {
        return "Price: " + price + " Qty: " + quantity + " ClientId: " + clientId + " Side: " + side;
    }
}
