package com.integration.xml.xml_api;

import com.integration.xml.xml_api.model.ClientPerson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class DeleteOperationsTest {
	private static final String URL = "http://localhost:8080/spring/persons/{personId}";
	private final RestTemplate restTemplate = new RestTemplate();
	
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		return headers;
	}
	
	@Test
	public void deleteResource_noContentStatusCodeReturned() {
		HttpEntity<Integer> entity = new HttpEntity<>(buildHeaders());
		ResponseEntity<ClientPerson> response = restTemplate.exchange(URL, HttpMethod.DELETE, entity, ClientPerson.class, 3);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		try {
			response = restTemplate.exchange(URL, HttpMethod.GET, entity, ClientPerson.class, 3);
			Assert.fail("404 error expected");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
		
}
