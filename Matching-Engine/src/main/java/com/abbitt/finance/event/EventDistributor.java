package com.abbitt.finance.event;


import com.abbitt.finance.connectivity.TcpConnection;

public interface EventDistributor {

    void handleClientRegisterRequested(ClientRegisterRequested event, TcpConnection conn);
    void handleOrderCreated(OrderCreated event, TcpConnection conn);
    void handleOrderPosted(OrderPosted event, TcpConnection conn);

}
