package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.EventDistributor;

public class TcpReader {

    private final EventDistributor eventDistributor;

    public TcpReader(EventDistributor eventDistributor) {
        this.eventDistributor = eventDistributor;
    }



}
