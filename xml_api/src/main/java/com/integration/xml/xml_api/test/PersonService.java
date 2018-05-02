package com.integration.xml.xml_api.test;


public interface PersonService {

	ServerPerson getPerson(long id);
	
	void updatePerson(ServerPerson person);
	
	void insertPerson(ServerPerson person);
	
	void deletePerson(long id);
}
