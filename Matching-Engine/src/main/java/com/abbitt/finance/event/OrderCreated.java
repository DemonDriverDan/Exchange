package com.abbitt.finance.event;


import com.abbitt.finance.Side;

import java.nio.ByteBuffer;

public class OrderCreated extends Event {

    public static final int MESSAGE_ID = 4;

    private int price;
    private long quantity;
    private Side side;

    public OrderCreated(int clientId, int price, long quantity, Side side) {
        super(clientId);
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public OrderCreated(ByteBuffer buffer) {
        super(buffer);
        this.price = buffer.getInt();
        this.quantity = buffer.getLong();
        this.side = Side.getByBinaryVal(buffer.getShort());
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
        return "OrderCreated{" +
                "clientId=" + getClientId() +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side=" + side +
                '}';
    }
}
