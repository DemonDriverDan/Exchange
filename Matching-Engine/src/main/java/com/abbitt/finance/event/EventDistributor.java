package com.abbitt.finance.event;


import com.abbitt.finance.client.ClientRepository;
import com.abbitt.finance.connectivity.TcpWriter;
import com.abbitt.finance.matching.MatchingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventDistributor {
    private static final Logger LOG = LoggerFactory.getLogger(EventDistributor.class);

    private final ClientRepository clientRepository;
    private final TcpWriter tcpWriter;
    private final MatchingEngine matchingEngine;

    public EventDistributor(TcpWriter tcpWriter, MatchingEngine matchingEngine, ClientRepository clientRepository) {
        this.tcpWriter = tcpWriter;
        this.matchingEngine = matchingEngine;
        this.clientRepository = clientRepository;
    }

    public void handleClientRegisterRequested(ClientRegisterRequested event) {
        LOG.info("Client register requested, id: {}", event.getClientId());
        boolean result = clientRepository.addClient(event.getClientId());

        if (result) {
            LOG.info("Client registered, id: {}", event.getClientId());
            tcpWriter.clientRegistered(new ClientRegistered(event.getClientId()));
        } else {
            String reason = "Client already registered";
            LOG.error("Client registration failed, id: {}, reason: {}", event.getClientId(), reason);
            tcpWriter.clientRegistrationFailed(new ClientRegistrationFailed(event.getClientId(), reason));
        }
    }

    public void handleOrderCreated(OrderCreated event) {
        LOG.info("Order created. {}", event.toString());

        if (!clientRepository.clientRegistered(event.getClientId())) {
            LOG.error("Client {} not registered", event.getClientId());
            tcpWriter.orderRejected(new OrderRejected(event.getClientId(), "Client not registered"));
            return;
        }

        try {
            OrderTraded orderTraded = matchingEngine.takeLiquidity(event);
            if (orderTraded == null) {
                tcpWriter.orderRejected(new OrderRejected(event.getClientId(), "An internal error occurred"));
                return;
            }
            tcpWriter.orderTraded(orderTraded);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            tcpWriter.orderRejected(new OrderRejected(event.getClientId(), e.getMessage()));
        }
    }

    public void handleOrderPosted(OrderCreated event) {
        LOG.info("Order posted. {}", event.toString());
    }
}
