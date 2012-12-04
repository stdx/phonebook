package edu.htwm.vsp.phone.bind;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.jpa.PhonebookServiceImpl;

public class PhoneModule extends AbstractModule {

	private final String persistenceUnit;
	private final Module persistModule;

	private PhoneModule(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
		persistModule = new JpaPersistModule(persistenceUnit);
		
	}

	public Module getPersistModule() {
		return persistModule;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	@Override
	protected void configure() {
		install(getPersistModule());
		bind(PhonebookService.class).to(PhonebookServiceImpl.class);
	}
	
	public static Module build() {
		return new PhoneModule("phonebook");
	}
	
	public static Module buildForTest() {
		return new PhoneModule("phonebook-test");
	}
	
	
}
