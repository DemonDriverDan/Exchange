package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.ClientRegistered;
import com.abbitt.finance.event.ClientRegistrationFailed;
import com.abbitt.finance.event.OrderRejected;
import com.abbitt.finance.event.OrderTraded;
import com.google.inject.Inject;

public class TcpWriterImpl implements TcpWriter {

    private final IOLoop loop;

    @Inject
    public TcpWriterImpl(IOLoop loop) {
        this.loop = loop;
    }

    @Override
    public void clientRegistered(ClientRegistered command) {
        loop.writeCommand(command);
    }

    @Override
    public void clientRegistrationFailed(ClientRegistrationFailed command) {
        loop.writeCommand(command);
    }

    @Override
    public void orderTraded(OrderTraded command) {
        loop.writeCommand(command);
    }

    @Override
    public void orderRejected(OrderRejected command) {
        loop.writeCommand(command);
    }
}
