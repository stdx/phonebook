package edu.htwm.vsp.phonebook.rest.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.inmemory.PhoneServiceInMemory;

/**
 *
 * An {@link Application} is the main entry point for a JAX-RS Webservice. Most
 * implementations of {@link Application}s are delivered with JAX-RS
 * distribution like Jersey or RestEasy.
 *
 * @author std
 *
 */
public class PhonebookApplication extends Application {

    private final Set<Object> singletons = new HashSet<Object>();
    private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
    private final PhonebookService phonebookService;

    public PhonebookApplication() {

        // add the phonebook service
        phonebookService = new PhoneServiceInMemory();
        SingletonTypeInjectableProvider<Context, PhonebookService> phonebookServiceProvider = new SingletonTypeInjectableProvider<Context, PhonebookService>(
                PhonebookService.class, phonebookService) {
        };
        getSingletons().add(phonebookServiceProvider);
        // register the resources 
        getClasses().add(PhonebookResourceImpl.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resourcesToRegister;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
