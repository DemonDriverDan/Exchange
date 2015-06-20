package com.abbitt.finance.connectivity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class TcpReadWriter {
    private static final Logger LOG = LoggerFactory.getLogger(TcpReadWriter.class);

    private static final int BUFFER_SIZE = 1024;
    private final static int DEFAULT_PORT = 9090;

    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private InetAddress hostAddress = null;

    private int port;
    private Selector selector;

    private long loopTime;
    private long numMessages = 0;

    public TcpReadWriter() throws IOException {
        this(DEFAULT_PORT);
    }

    public TcpReadWriter(int port) throws IOException {
        this.port = port;
        selector = initSelector();
    }

    private Selector initSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        InetSocketAddress isa = new InetSocketAddress(hostAddress, port);
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

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        SelectionKey newKey = socketChannel.register(selector, SelectionKey.OP_READ);

        LOG.debug("Client is connected");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
            int s = readBuffer.getInt();
            LOG.info("Num read: {}", numRead);
            LOG.info("Int: {}", s);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();

            LOG.error("Forceful shutdown");
            return;
        }

        if (numRead == -1) {
            LOG.info("Graceful shutdown");
            key.channel().close();
            key.cancel();

            return;
        }

        socketChannel.register(selector, SelectionKey.OP_WRITE);

        numMessages++;
        if (numMessages%100000 == 0) {
            long elapsed = System.currentTimeMillis() - loopTime;
            loopTime = System.currentTimeMillis();
            LOG.debug("" + elapsed);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer dummyResponse = ByteBuffer.wrap("ok".getBytes("UTF-8"));

        socketChannel.write(dummyResponse);
        if (dummyResponse.remaining() > 0) {
            LOG.error("Filled UP");
        }

        key.interestOps(SelectionKey.OP_READ);
    }
}
