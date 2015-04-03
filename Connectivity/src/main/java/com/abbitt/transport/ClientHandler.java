package com.abbitt.transport;


import java.util.HashMap;
import java.util.Map;

public class ClientHandler {

    private final Map<Integer, Client> clientMap;
    private int lastClientIdAssigned;

    public ClientHandler() {
        clientMap = new HashMap<>();
        lastClientIdAssigned = 0;
    }

    public void registerClient(Client client) {
        clientMap.putIfAbsent(lastClientIdAssigned++, client);
    }
}
