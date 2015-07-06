package com.abbitt.finance;


import com.abbitt.finance.event.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DummyClient implements Runnable {

    private final int clientId;
    private SocketChannel client;
    private ByteBuffer inBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer outBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException, InterruptedException {
        DummyClient postClient = new DummyClient(1);
        postClient.openSocket();
        postClient.registrationScenario();
        postClient.orderPostedScenario(100, 50000, Side.BUY);

        for (int i = 2; i <= 11; i++) {
            DummyClient temp = new DummyClient(i);
            Thread thread = new Thread(temp);
            thread.start();
        }
    }

    public DummyClient(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run(){
        try {
            openSocket();
            registrationScenario();
            orderCreatdScenario(100, 500, Side.BUY);
            boolean run = true;
            while(run) {

            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSocket() throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6789);
        client = SocketChannel.open(hostAddress);

        System.out.println("Client sending messages to server...");
    }

    private void registrationFailedScenario() throws IOException {
//        registrationScenario(1);
//        registrationScenario(1);
    }

    private void registrationScenario() throws IOException {
        writeClientRegistrationRequest();
        sendAndWaitForResponse();
        Command response = parseResponse();
        printMessage(response);
    }

    private void orderPostedScenario(int price, long qty, Side side) throws IOException {
        writeOrderPosted(price, qty, side);
        sendAndWaitForResponse();
        Command response = parseResponse();
        printMessage(response);
    }

    private void orderCreatdScenario(int price, long qty, Side side) throws IOException {
        writeOrderCreated(price, qty, side);
        sendAndWaitForResponse();
        Command response = parseResponse();
        printMessage(response);
    }

    private void writeClientRegistrationRequest() {
        ClientRegisterRequested req = new ClientRegisterRequested(clientId);
        outBuffer.putInt(ClientRegisterRequested.MESSAGE_ID);
        outBuffer.putInt(req.getClientId());
        printMessage(req);
    }

    private void writeOrderPosted(int price, long qty, Side side) {
        OrderPosted req = new OrderPosted(clientId, price, qty, side);
        outBuffer.putInt(OrderPosted.MESSAGE_ID);
        outBuffer.putInt(req.getClientId());
        outBuffer.putInt(req.getPrice());
        outBuffer.putLong(req.getQuantity());
        outBuffer.putShort(req.getSide().getBinaryVal());
        printMessage(req);
    }

    private void writeOrderCreated(int price, long qty, Side side) {
        OrderCreated req = new OrderCreated(clientId, price, qty, side);
        outBuffer.putInt(OrderCreated.MESSAGE_ID);
        outBuffer.putInt(req.getClientId());
        outBuffer.putInt(req.getPrice());
        outBuffer.putLong(req.getQuantity());
        outBuffer.putShort(req.getSide().getBinaryVal());
        printMessage(req);
    }

    private void sendAndWaitForResponse() throws IOException {
        send();
        waitForResponse();
    }

    private void send() throws IOException {
        System.out.println("Sending message");
        outBuffer.flip();
        client.write(outBuffer);
        outBuffer.clear();
    }

    private void waitForResponse() throws IOException {
        System.out.println("Waiting for response...");

        int bytes = client.read(inBuffer);
        if (bytes == -1) {
            System.out.println("Socket closed");
            System.exit(1);
        }
        inBuffer.clear();
    }

    private Command parseResponse() {
        int messageId = inBuffer.getInt();
        if (messageId == ClientRegistered.MESSAGE_ID) {
            return new ClientRegistered(inBuffer);
        } else if (messageId == ClientRegistrationFailed.MESSAGE_ID) {
            return new ClientRegistrationFailed(inBuffer);
        } else if (messageId == OrderTraded.MESSAGE_ID) {
            return new OrderTraded(inBuffer);
        } else if (messageId == OrderRejected.MESSAGE_ID) {
            return new OrderRejected(inBuffer);
        }
        return null;
    }

    private void printMessage(Event response) {
        if (response != null) {
            System.out.println("-- [" + response.getClientId() + "] -- " + response.toString());
        } else {
            System.out.println("null response");
        }
    }
}
