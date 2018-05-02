package com.integration.xml.xml_api;

import com.integration.xml.xml_api.model.ClientPerson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class GetOperationsTest {
	private static final String URL = "http://localhost:8080/spring/persons/{personId}";
	private final RestTemplate restTemplate = new RestTemplate();
	
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		return headers;
	}
	
	@Test
	public void getResource_responseIsConvertedToPerson() {
		HttpEntity<Integer> entity = new HttpEntity<>(buildHeaders());
		ResponseEntity<ClientPerson> response = restTemplate.exchange(URL, HttpMethod.GET, entity, ClientPerson.class, 1);
		assertEquals("John" , response.getBody().getName());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getResource_responseIsReceivedAsJson() {
		HttpEntity<Integer> entity = new HttpEntity<>(buildHeaders());
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class, 1);
		assertEquals("{\"id\":1,\"name\":\"John\",\"age\":25}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void getResource_sendXml_415errorReturned() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_XML);
		HttpEntity<Integer> entity = new HttpEntity<>(headers);
		restTemplate.exchange(URL, HttpMethod.GET, entity, ClientPerson.class, 1);
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void getResource_expectXml_receiveJson_406errorReturned() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Integer> entity = new HttpEntity<>(headers);
		restTemplate.exchange(URL, HttpMethod.GET, entity, ClientPerson.class, 1);
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void getResource_resourceNotFound_404errorReturned() {
		HttpEntity<Integer> entity = new HttpEntity<>(buildHeaders());
		restTemplate.exchange(URL, HttpMethod.GET, entity, ClientPerson.class, 8);
	}
}
