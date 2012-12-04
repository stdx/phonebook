package edu.htwm.vsp.phone.bind;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Modules {

	public static Injector build() {
		
		PhoneModule phoneModule = new PhoneModule("phonebook");
		
		
		return Guice.createInjector(phoneModule);
	}
	
	public static Injector buildForTest() {
		PhoneModule phoneModule = new PhoneModule("phonebook-test");
		return Guice.createInjector(phoneModule);
	}
	
	
}
