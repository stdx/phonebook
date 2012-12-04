package edu.htwm.vsp.phonebook.rest.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import edu.htwm.vsp.phone.bind.PhoneModule;

public class PhonebookApplication extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			
			@Override
			protected void configureServlets() {
				
				// install the persistence modules
				install(PhoneModule.build());
				
				filter("/*").through(PersistFilter.class);
				
				// bind JAX-RS resources
//				bind(UsersResource.class).to(UsersResourceImpl.class);
				bind(UsersResourceImpl.class);
				
				bind(GuiceContainer.class);
				
				serve("/*").with(GuiceContainer.class);
			}
		});
	}

}
