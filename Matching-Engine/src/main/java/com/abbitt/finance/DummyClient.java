package com.abbitt.finance;


import com.abbitt.finance.event.ClientRegisterRequested;
import com.abbitt.finance.event.ClientRegistered;
import com.abbitt.finance.event.ClientRegistrationFailed;
import com.abbitt.finance.event.Command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DummyClient {

    // TODO Change to have input and output buffers!

    private SocketChannel client;
    private ByteBuffer inBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException, InterruptedException {
        DummyClient temp = new DummyClient();
        temp.run();
    }

    private void run() throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6789);
        client = SocketChannel.open(hostAddress);

        System.out.println("Client sending messages to server...");
        registrationFailedScenario();
        client.close();
    }

    private void registrationFailedScenario() throws IOException {
        registrationScenario(1);
        registrationScenario(1);
    }

    private void registrationScenario(int clientId) throws IOException {
        writeClientRegistrationRequest(clientId);
        sendAndWaitForResponse();
        Command response = parseResponse();
        printResponse(response);
    }

    private void writeClientRegistrationRequest(int clientId) {
        buffer.putInt(ClientRegisterRequested.MESSAGE_ID);
        buffer.putInt(clientId);
    }

    private void sendAndWaitForResponse() throws IOException {
        send();
        waitForResponse();
    }

    private void send() throws IOException {
        System.out.println("Sending message");
        buffer.flip();
        client.write(buffer);
        buffer.clear();
    }

    private void waitForResponse() throws IOException {
        System.out.println("Waiting for response...");

        int bytes = client.read(buffer);
        if (bytes == -1) {
            System.out.println("Socket closed");
            System.exit(1);
        }
        buffer.clear();
    }

    private Command parseResponse() {
        int messageId = buffer.getInt();
        if (messageId == ClientRegistered.MESSAGE_ID) {
            return new ClientRegistered(buffer);
        } else if (messageId == ClientRegistrationFailed.MESSAGE_ID) {
            return new ClientRegistrationFailed(buffer);
        }
        return null;
    }

    private void printResponse(Command response) {
        if (response != null) {
            System.out.println(response.toString());
        } else {
            System.out.println("null response");
        }
    }
}
