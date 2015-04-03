package com.abbitt.transport;


public class ClientLoop implements Runnable {

    private final ClientHandler clientHandler;

    public ClientLoop(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {

    }
}
