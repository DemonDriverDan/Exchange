package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TcpConnection {
    private static final Logger LOG = LoggerFactory.getLogger(TcpConnection.class);
    private static final int BUFFER_SIZE = 1024;

    private final SelectionKey key;
    private final EventDistributor distributor;
    private ByteBuffer inBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer outBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    public TcpConnection(SelectionKey key, EventDistributor distributor) {
        this.key = key;
        this.distributor = distributor;
        key.attach(this);
    }


    void read() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        try {
            inBuffer.clear();
            int bytes = socketChannel.read(inBuffer);
            if (bytes == -1) {
                throw new SocketClosedException();
            }

            if (bytes > 0) {
                inBuffer.flip();
                parseMessage();
            }
            inBuffer.clear();
        } catch (SocketClosedException e) {
            LOG.warn(e.getMessage());
            close();
        } catch (Exception e) {
            LOG.error("An error occured", e);
            close();
        }
    }

    private void parseMessage() {
        int messageType = inBuffer.getInt();
        LOG.trace("Message type: {}", messageType);
        if (messageType == ClientRegisterRequested.MESSAGE_ID) {
            ClientRegisterRequested event = new ClientRegisterRequested(inBuffer);
            distributor.handleClientRegisterRequested(event, this);
        } else if (messageType == OrderCreated.MESSAGE_ID) {
            OrderCreated event = new OrderCreated(inBuffer);
            distributor.handleOrderCreated(event, this);
        } else if (messageType == OrderPosted.MESSAGE_ID) {
            OrderPosted event = new OrderPosted(inBuffer);
            distributor.handleOrderPosted(event, this);
        }
        key.interestOps(SelectionKey.OP_WRITE);
    }

    public void write(Command command) {
        outBuffer.clear();
        command.writeToBuffer(outBuffer);
        outBuffer.flip();
    }

    void write() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        socketChannel.write(outBuffer);
        if (outBuffer.remaining() > 0) {
            LOG.error("Filled UP");
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    private void close() throws IOException {
        inBuffer.clear();
        key.cancel();
        key.channel().close();
    }

    private class SocketClosedException extends Exception {
        private SocketClosedException() {
            super("Socket closed");
        }
    }
}
