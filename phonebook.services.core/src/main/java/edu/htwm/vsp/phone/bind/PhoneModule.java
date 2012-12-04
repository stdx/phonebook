package edu.htwm.vsp.phone.bind;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

import edu.htwm.vsp.phone.service.PhoneService;
import edu.htwm.vsp.phone.service.jpa.PhoneServiceImpl;

public class PhoneModule extends AbstractModule {

	private final String persistenceUnit;
	private final Module persistModule;

	public PhoneModule(String persistenceUnit) {
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
		bind(PhoneService.class).to(PhoneServiceImpl.class);
	}
	
}
