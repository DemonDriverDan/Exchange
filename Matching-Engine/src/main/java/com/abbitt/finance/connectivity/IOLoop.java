package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class IOLoop {
    private static final Logger LOG = LoggerFactory.getLogger(IOLoop.class);
    private static final int BUFFER_SIZE = 1024;

    private final EventDistributor distributor;
    private int port;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private SelectionKey currentKey;

    @Inject
    public IOLoop(EventDistributor distributor, @Named("port") int port) throws IOException {
        this.distributor = distributor;
        this.port = port;
        selector = initSelector();
    }

    private Selector initSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        InetSocketAddress isa = new InetSocketAddress(port);
        serverChannel.socket().bind(isa);
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
        return socketSelector;
    }

    public void loop() {
        LOG.debug("Starting IO loop");
        while (true) {
            try{
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }

            } catch (Exception e) {
                // This should never be hit, exceptions should be caught downstream and the correct error message sent back
                e.printStackTrace();
                readBuffer.clear();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);
        LOG.debug("Client is connected");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        currentKey = key;

        readBuffer.clear();
        int read = socketChannel.read(readBuffer);
        if (read > 0) {
            readBuffer.flip();
            parseMessage();
        }
    }

    private void parseMessage() {
        int messageType = readBuffer.getInt();
        LOG.trace("Message type: {}", messageType);
        if (messageType == ClientRegisterRequested.MESSAGE_ID) {
            ClientRegisterRequested event = new ClientRegisterRequested(readBuffer);
            distributor.handleClientRegisterRequested(event);
        } else if (messageType == OrderCreated.MESSAGE_ID) {
            OrderCreated event = new OrderCreated(readBuffer);
            distributor.handleOrderCreated(event);
        } else if (messageType == OrderPosted.MESSAGE_ID) {
            OrderPosted event = new OrderPosted(readBuffer);
            distributor.handleOrderPosted(event);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        socketChannel.write(writeBuffer);
        if (writeBuffer.remaining() > 0) {
            LOG.error("Filled UP");
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    void writeCommand(Command command) {
        command.writeToBuffer(writeBuffer);
        currentKey.interestOps(SelectionKey.OP_WRITE);
    }
}
