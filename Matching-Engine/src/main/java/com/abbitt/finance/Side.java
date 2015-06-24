package com.abbitt.finance;


public enum Side {
    BUY((short)1),
    SELL((short)2);

    private final short binaryVal;

    Side(short binaryVal) {
        this.binaryVal = binaryVal;
    }

    public short getBinaryVal() {
        return binaryVal;
    }

    public static Side getByBinaryVal(short binaryVal) {
        if (binaryVal == 1) {
            return BUY;
        } else if (binaryVal == 2) {
            return SELL;
        }
        return null;
    }
}
