package edu.htwm.vsp.phonebook.rest.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import edu.htwm.vsp.phone.bind.PhoneModule;

/**
 * 
 * Assembles the DI-configuration for Phonebook.
 * 
 * @author std
 * 
 */
public class PhonebookContainer extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				
				/*
				 *  install the persistence modules
				 */
				install(PhoneModule.build());
				
				filter("/*").through(PersistFilter.class);
				
				// bind JAX-RS resources
				bind(PhonebookResourceImpl.class);
				
				bind(GuiceContainer.class);
				
				serve("/*").with(GuiceContainer.class);
			}
		});
	}

}
