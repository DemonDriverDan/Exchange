package com.abbitt.finance.event;

import com.abbitt.finance.Side;

import java.nio.ByteBuffer;


public class OrderPosted extends OrderCreated {

    public static final int MESSAGE_ID = 7;

    public OrderPosted(int clientId, int price, long quantity, Side side) {
        super(clientId, price, quantity, side);
    }

    public OrderPosted(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public String toString() {
        return "OrderPosted{" +
                "clientId=" + getClientId() +
                ", price=" + super.getPrice() +
                ", quantity=" + super.getQuantity() +
                ", side=" + super.getSide() +
                '}';
    }
}
