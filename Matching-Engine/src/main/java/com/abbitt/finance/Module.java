package com.abbitt.finance;

import com.abbitt.finance.client.ClientRepository;
import com.abbitt.finance.connectivity.IOLoop;
import com.abbitt.finance.event.EventDistributor;
import com.abbitt.finance.event.EventDistributorImpl;
import com.abbitt.finance.matching.MatchingEngine;
import com.abbitt.finance.matching.MatchingEngineImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;


public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(MatchingEngine.class).to(MatchingEngineImpl.class);
        bind(EventDistributor.class).to(EventDistributorImpl.class);

        bind(IOLoop.class).asEagerSingleton();
        bind(ClientRepository.class).asEagerSingleton();

        bind(Integer.class).annotatedWith(Names.named("port")).toInstance(6789);
    }
}
