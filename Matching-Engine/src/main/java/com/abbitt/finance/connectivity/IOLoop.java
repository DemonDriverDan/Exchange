package com.abbitt.finance.connectivity;


import com.abbitt.finance.event.EventDistributorI;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class IOLoop {
    private static final Logger LOG = LoggerFactory.getLogger(IOLoop.class);

    private final EventDistributorI distributor;
    private int port;
    private Selector selector;

    @Inject
    public IOLoop(EventDistributorI distributor, @Named("port") int port) throws IOException {
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
                        ((TcpConnection)key.attachment()).read();
                    } else if (key.isWritable()) {
                        ((TcpConnection)key.attachment()).write();
                    }
                }

            } catch (Exception e) {
                // This should never be hit, exceptions should be caught downstream and the correct error message sent back
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        SelectionKey newKey = socketChannel.register(selector, SelectionKey.OP_READ);
        new TcpConnection(newKey, distributor);
        LOG.debug("Client is connected");
    }
}
