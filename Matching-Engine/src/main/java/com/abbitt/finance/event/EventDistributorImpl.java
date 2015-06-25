package com.abbitt.finance.event;


import com.abbitt.finance.client.ClientRepository;
import com.abbitt.finance.connectivity.TcpConnection;
import com.abbitt.finance.matching.MatchingEngine;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventDistributorImpl implements EventDistributor {
    private static final Logger LOG = LoggerFactory.getLogger(EventDistributorImpl.class);

    private final ClientRepository clientRepository;
    private final MatchingEngine matchingEngine;

    @Inject
    public EventDistributorImpl(MatchingEngine matchingEngine, ClientRepository clientRepository) {
        this.matchingEngine = matchingEngine;
        this.clientRepository = clientRepository;
    }

    @Override
    public void handleClientRegisterRequested(ClientRegisterRequested event, TcpConnection conn) {
        LOG.info(event.toString());
        boolean result = clientRepository.addClient(event.getClientId());

        if (result) {
            LOG.info("Client registered, id: {}", event.getClientId());
            conn.write(new ClientRegistered(event.getClientId()));
        } else {
            String reason = "Client already registered";
            LOG.error("Client registration failed, id: {}, reason: {}", event.getClientId(), reason);
            conn.write(new ClientRegistrationFailed(event.getClientId(), reason));
        }
    }

    @Override
    public void handleOrderCreated(OrderCreated event, TcpConnection conn) {
        LOG.info(event.toString());

        if (!clientRepository.clientRegistered(event.getClientId())) {
            LOG.error("Client {} not registered", event.getClientId());
            conn.write(new OrderRejected(event.getClientId(), "Client not registered"));
            return;
        }

        try {
            OrderTraded orderTraded = matchingEngine.takeLiquidity(event);
            if (orderTraded == null) {
                conn.write(new OrderRejected(event.getClientId(), "An internal error occurred"));
                return;
            }
            conn.write(orderTraded);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            conn.write(new OrderRejected(event.getClientId(), e.getMessage()));
        }
    }

    @Override
    public void handleOrderPosted(OrderCreated event, TcpConnection conn) {
        LOG.info(event.toString());

        OrderTraded orderTraded = matchingEngine.addLiquidity(event);
        if (orderTraded == null) {
            conn.write(new OrderRejected(event.getClientId(), "An internal error occurred"));
            return;
        }
        conn.write(orderTraded);
    }
}
