package com.abbitt.finance.matching;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

class OrderBook {

    private Map<Integer, Long> trades;

    private OrderBook(Comparator<Integer> comparator) {
        trades = new TreeMap<>(comparator);
    }

    long takeQuantity(int price, long quantity) {
        if (!hasQuantityAtPrice(price)) {
            return -1;
        }

        long qtyOnBook = trades.get(price);

        if (qtyOnBook < quantity) {
            trades.remove(price);
            return qtyOnBook;
        }

        qtyOnBook -= quantity;
        trades.put(price, qtyOnBook);

        return quantity;
    }

    void addQuantity(int price, long quantity) {
        if (trades.get(price) != null) {
            long currentQty = trades.get(price);
            trades.put(price, currentQty + quantity);
        } else {
            trades.put(price, quantity);
        }
    }

    boolean hasQuantityAtPrice(int price) {
        return trades.containsKey(price) && trades.get(price) > 0;
    }

    int getInsidePrice() {
        return trades.keySet().iterator().hasNext() ? trades.keySet().iterator().next() : -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Long> tradesEntry : trades.entrySet()) {
            sb.append(tradesEntry.getKey()).append(" -> ").append(tradesEntry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public static OrderBook createBidBook() {
        Comparator<Integer> comparator = (o1, o2) -> (int)(o2 - o1);
        return new OrderBook(comparator);
    }

    public static OrderBook createAskBook() {
        Comparator<Integer> comparator = (o1, o2) -> (int)(o1 - o2);
        return new OrderBook(comparator);
    }
}
