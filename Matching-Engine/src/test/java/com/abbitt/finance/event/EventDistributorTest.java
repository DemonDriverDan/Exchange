package com.abbitt.finance.event;

import com.abbitt.finance.client.ClientRepository;
import com.abbitt.finance.matching.MatchingEngine;
import com.abbitt.finance.matching.NoQuantityException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class EventDistributorTest {

    private EventDistributorImpl eventDistributor;
    private MatchingEngine matchingEngine;
    private ClientRepository clientRepository;

    @Before
    public void setup() {
        matchingEngine = mock(MatchingEngine.class);
        clientRepository = new ClientRepository();
        eventDistributor = new EventDistributorImpl(matchingEngine, clientRepository);
    }

    @Test
    public void clientRegistered() {
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        ArgumentCaptor<ClientRegistered> captor = ArgumentCaptor.forClass(ClientRegistered.class);
//        verify(tcpWriter).clientRegistered(captor.capture());
//        assertEquals(1, captor.getValue().getClientId());
    }

    @Test
    public void clientAlreadyRegistered() {
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        ArgumentCaptor<ClientRegistrationFailed> captor = ArgumentCaptor.forClass(ClientRegistrationFailed.class);
//        verify(tcpWriter).clientRegistrationFailed(captor.capture());
//        assertEquals(1, captor.getValue().getClientId());
//        assertEquals("Client already registered", captor.getValue().getReason());
    }


    @Test
    public void orderCreated() throws NoQuantityException {
//        when(matchingEngine.takeLiquidity(any(OrderCreated.class))).thenReturn(new OrderTraded(1, 100, 200, Side.BUY));
//
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        eventDistributor.handleOrderCreated(new OrderCreated(1, 100, 200, Side.BUY));
//        verifyOrderTraded(1, 100, 200, Side.BUY);
    }

    @Test
    public void orderCreatedNoQuantity() throws NoQuantityException {
//        doThrow(new NoQuantityException(100)).when(matchingEngine).takeLiquidity(any());
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        eventDistributor.handleOrderCreated(new OrderCreated(1, 100, 200, Side.BUY));
//        verifyOrderRejected("No quantity at price 100");
    }

    @Test
    public void orderCreatedNullLiquidity() throws NoQuantityException {
//        when(matchingEngine.takeLiquidity(any())).thenReturn(null);
//        eventDistributor.handleClientRegisterRequested(new ClientRegisterRequested(1));
//        eventDistributor.handleOrderCreated(new OrderCreated(1, 100, 200, Side.BUY));
//        verifyOrderRejected("An internal error occurred");
    }

    @Test
    public void orderCreatedClientNotRegistered() {
//        eventDistributor.handleOrderCreated(new OrderCreated(1, 100, 200, Side.BUY));
//        verifyOrderRejected("Client not registered");
    }
}
