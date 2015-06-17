package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.ClientRegistered;
import com.abbitt.finance.event.ClientRegistrationFailed;
import com.abbitt.finance.event.OrderRejected;
import com.abbitt.finance.event.OrderTraded;

public interface TcpWriter {

    void clientRegistered(ClientRegistered command);
    void clientRegistrationFailed(ClientRegistrationFailed command);

    void orderTraded(OrderTraded command);
    void orderRejected(OrderRejected command);

}
