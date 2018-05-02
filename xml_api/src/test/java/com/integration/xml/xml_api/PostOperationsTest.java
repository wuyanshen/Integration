package com.integration.xml.xml_api;

import com.integration.xml.xml_api.model.ClientPerson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class PostOperationsTest {
	private static final String POST_URL = "http://localhost:8092/spring/persons";
	private static final String GET_URL = "http://localhost:8092/spring/persons/{personId}";
	private final RestTemplate restTemplate = new RestTemplate();
	
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		return headers;
	}
	
	@Test
	public void addResource_noContentStatusCodeReturned() {
		ClientPerson person = new ClientPerson(9, "Jana");
		HttpEntity<ClientPerson> entity = new HttpEntity<ClientPerson>(person, buildHeaders());
		
		ResponseEntity<ClientPerson> response = restTemplate.exchange(POST_URL, HttpMethod.POST, entity, ClientPerson.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		HttpEntity<Integer> getEntity = new HttpEntity<>(buildHeaders());
		response = restTemplate.exchange(GET_URL, HttpMethod.GET, getEntity, ClientPerson.class, 9);
		person = response.getBody();
		assertEquals("Jana", person.getName());
	}
}
