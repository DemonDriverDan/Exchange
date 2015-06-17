package com.abbitt.finance.matching;


public class NoQuantityException extends Exception {

    public NoQuantityException(int price) {
        super("No quantity at price " + price);
    }
}
