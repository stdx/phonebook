package edu.htwm.vsp.phonebook.rest;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.inmemory.PhoneServiceInMemory;
import edu.htwm.vsp.phonebook.rest.impl.PhonebookResourceImpl;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 *
 * An {@link Application }
 *
 * @author std
 *
 */
public class PhonebookApplication extends Application {

    private final Set<Object> singletons = new HashSet<Object>();
    private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
    private final PhonebookService phonebookService;

    public PhonebookApplication() {
        // add the birthday service
        phonebookService = new PhoneServiceInMemory();
        SingletonTypeInjectableProvider<Context, PhonebookService> birthdayServiceProvider = new SingletonTypeInjectableProvider<Context, PhonebookService>(
                PhonebookService.class, phonebookService) {
        };
        singletons.add(birthdayServiceProvider);
    }

    @Override
    public Set<Class<?>> getClasses() {

        resourcesToRegister.add(PhonebookResourceImpl.class);
        return resourcesToRegister;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
