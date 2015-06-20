package com.abbitt.finance;


import com.abbitt.finance.client.ClientRepository;
import com.abbitt.finance.connectivity.TcpReadWriter;
import com.abbitt.finance.event.EventDistributor;
import com.abbitt.finance.matching.MatchingEngine;
import com.abbitt.finance.matching.MatchingEngineImpl;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        MatchingEngine matchingEngine = new MatchingEngineImpl();
        ClientRepository clientRepository = new ClientRepository();
        EventDistributor eventDistributor = new EventDistributor(null, matchingEngine, clientRepository);
        TcpReadWriter tcpReadWriter = new TcpReadWriter(6789);

        tcpReadWriter.loop();
    }


}
