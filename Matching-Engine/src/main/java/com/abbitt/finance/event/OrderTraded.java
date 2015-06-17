package com.abbitt.finance.event;


import com.abbitt.finance.Side;

public class OrderTraded extends Event {

    private final int price;
    private final long quantity;
    private final Side side;

    public OrderTraded(int clientId, int price, long quantity, Side side) {
        super(clientId);
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public int getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return super.toString() + ", Price: " + price + ", Qty: " + quantity + ", Side: " + side;
    }
}
