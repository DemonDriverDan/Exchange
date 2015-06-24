package com.abbitt.finance.event;


import com.abbitt.finance.connectivity.TcpConnection;

public interface EventDistributorI {

    void handleClientRegisterRequested(ClientRegisterRequested event, TcpConnection conn);
    void handleOrderCreated(OrderCreated event, TcpConnection conn);
    void handleOrderPosted(OrderCreated event, TcpConnection conn);

}
