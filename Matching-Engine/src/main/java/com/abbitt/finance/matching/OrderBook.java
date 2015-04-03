package com.abbitt.finance.matching;


import com.abbitt.finance.command.TradeCommand;
import com.abbitt.finance.command.TradeCommandHandler;
import com.abbitt.finance.event.TradeEvent;

import java.util.*;

class OrderBook {

    private Map<Double, Queue<TradeEvent>> trades;
    private final TradeCommandHandler tradeCommandHandler;

    private OrderBook(Comparator<Double> comparator, TradeCommandHandler tradeCommandHandler) {
        trades = new TreeMap<>(comparator);
        this.tradeCommandHandler = tradeCommandHandler;
    }

    void addTrade(TradeEvent tradeEvent) {
        trades.putIfAbsent(tradeEvent.getPrice(), new LinkedList<>());
        trades.get(tradeEvent.getPrice()).add(tradeEvent);
    }

    void takeQuantity(TradeEvent tradeEvent) {
        Queue<TradeEvent> tradeEventAtPrice = trades.get(tradeEvent.getPrice());
        long remainingQuantity = tradeEvent.getQuantity();

        while (remainingQuantity > 0 && tradeEventAtPrice != null && tradeEventAtPrice.size() > 0) {
            if (tradeEventAtPrice.peek().getQuantity() > remainingQuantity) {
                tradeEventAtPrice.peek().removeQuantity(remainingQuantity);
                remainingQuantity = 0;
            } else {
                TradeEvent poll = tradeEventAtPrice.poll();
                remainingQuantity -= poll.getQuantity();
                poll.removeQuantity(poll.getQuantity());
                tradeCommandHandler.tradeExecuted();
//                tradeCommandHandler.tradeCompleted(new TradeCommand(poll, "Filled"));
            }
        }
    }

    long quantityAtPrice(double price) {
        long quantity = 0;
        for (TradeEvent tradeEvent : trades.get(price)) {
            quantity += tradeEvent.getQuantity();
        }
        return quantity;
    }

    boolean hasQuantityAtPrice(double price) {
        return trades.containsKey(price) && trades.get(price).size() > 0;
    }

    double getInsidePrice() {
        return trades.keySet().iterator().hasNext() ? trades.keySet().iterator().next() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Double, Queue<TradeEvent>> tradesEntry : trades.entrySet()) {
            sb.append(tradesEntry.getKey()).append(" ->");
            tradesEntry.getValue().stream().forEach(trade -> sb.append(" ").append(trade.getQuantity()));
            sb.append(" | ").append(quantityAtPrice(tradesEntry.getKey())).append("\n");
        }
        return sb.toString();
    }

    public static OrderBook createBidBook(TradeCommandHandler tradeCommandHandler) {
        Comparator<Double> comparator = (o1, o2) -> (int)(o2 - o1);
        return new OrderBook(comparator, tradeCommandHandler);
    }

    public static OrderBook createAskBook(TradeCommandHandler tradeCommandHandler) {
        Comparator<Double> comparator = (o1, o2) -> (int)(o1 - o2);
        return new OrderBook(comparator, tradeCommandHandler);
    }
}
