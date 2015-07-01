package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TcpConnection {
    private static final Logger LOG = LoggerFactory.getLogger(TcpConnection.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String MALFORMED_MESSAGE = "Malformed message";
    private static final String INTERNAL_ERROR = "An internal error occurred";

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
            LOG.error("An error occurred", e);
            close();
        }
    }

    private void parseMessage() {
        int messageType = inBuffer.getInt();
        LOG.trace("Message type: {}", messageType);
        if (messageType == ClientRegisterRequested.MESSAGE_ID) {
            try {
                ClientRegisterRequested event = new ClientRegisterRequested(inBuffer);
                distributor.handleClientRegisterRequested(event, this);
            } catch (BufferUnderflowException bufEx) {
                write(new ClientRegistrationFailed(0, MALFORMED_MESSAGE));
                LOG.error(MALFORMED_MESSAGE, bufEx);
            } catch (Exception e) {
                write(new ClientRegistrationFailed(0, INTERNAL_ERROR));
                LOG.error("Error occurred parsing client register requested event", e);
            }
        } else if (messageType == OrderCreated.MESSAGE_ID) {
            try {
                OrderCreated event = new OrderCreated(inBuffer);
                distributor.handleOrderCreated(event, this);
            } catch (BufferUnderflowException bufEx) {
                write(new OrderRejected(0, MALFORMED_MESSAGE));
                LOG.error(MALFORMED_MESSAGE, bufEx);
            } catch (Exception e) {
                write(new OrderRejected(0, INTERNAL_ERROR));
                LOG.error("Error occurred parsing order event", e);
            }
        } else if (messageType == OrderPosted.MESSAGE_ID) {
            try {
                OrderPosted event = new OrderPosted(inBuffer);
                distributor.handleOrderPosted(event, this);
            } catch (BufferUnderflowException bufEx) {
                write(new OrderRejected(0, MALFORMED_MESSAGE));
                LOG.error(MALFORMED_MESSAGE, bufEx);
            } catch (Exception e) {
                write(new OrderRejected(0, INTERNAL_ERROR));
                LOG.error("Error occurred parsing order posted event", e);
            }
        } else {
            LOG.error("Unknown message type {}", messageType);
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
