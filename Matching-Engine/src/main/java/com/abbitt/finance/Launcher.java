package com.abbitt.finance;


import com.abbitt.finance.connectivity.IOLoop;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new Module());
        IOLoop loop = injector.getInstance(IOLoop.class);
        loop.loop();
    }


}
