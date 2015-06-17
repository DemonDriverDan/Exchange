package com.abbitt.finance.client;

import java.util.ArrayList;
import java.util.List;


public class ClientRepository {

    private final List<Integer> clientIds;

    public ClientRepository() {
        clientIds = new ArrayList<>();
    }

    public boolean addClient(int clientId) {
        if (clientIds.contains(clientId)) {
            return false;
        }
        clientIds.add(clientId);
        return true;
    }

    public boolean clientRegistered(int clientId) {
        return clientIds.contains(clientId);
    }
}
