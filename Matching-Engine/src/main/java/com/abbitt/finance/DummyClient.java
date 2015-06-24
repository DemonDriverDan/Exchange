package com.abbitt.finance;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DummyClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 6789);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client sending messages to server...");

        // Send messages to server

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(1);
        buffer.putInt(5);
        buffer.flip();
        client.write(buffer);
        buffer.clear();

        System.out.println("Waiting for response...");
        int read = client.read(buffer);
        if (read > 0) {
            buffer.flip();
            int messageType = buffer.getInt();
            System.out.println("Message received, message type: " + messageType);
        }

        client.close();
    }

}
