package de.telekom.sea.mystuffbackend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.ResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import de.telekom.sea.mystuffbackend.entities.Item;
import de.telekom.sea.mystuffbackend.repository.ItemRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ItemRestControllerTest {

	private static final String BASE_PATH = "/api/v1/items";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ItemRepository repo;

	@BeforeEach
	void setupRepo() {
		repo.deleteAll();
	}

	@Test
	void shouldBeAbleToUploadAnItem() {
		// Given | Arrange
		Item lawnMower = buildLawnMower();
		// When | Act
		ResponseEntity<Item> response = restTemplate.postForEntity(BASE_PATH, lawnMower, Item.class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getId()).isNotNull();
	}

	@Test
	void shouldReadAllItems() {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem(buildLawnMower()).getBody();
		Item lawnTrimmer = givenAnInsertedItem(buildLawnTrimmer()).getBody();
		Item fourTyres = givenAnInsertedItem(build4Tires()).getBody();
		// When | Act
		ResponseEntity<Item[]> response = restTemplate.getForEntity(BASE_PATH, Item[].class);
//		// Then | Assert
		System.out.println("--> Status: " + response.getStatusCodeValue() + ", Länge: " + response.getBody().length);
		assertThat(response.getBody().length).isEqualTo(3);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldFinfOneItem() {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem(buildLawnMower()).getBody();
		// When | Act
		ResponseEntity<Item> response = restTemplate.getForEntity(BASE_PATH + "/" + lawnMower.getId(), Item.class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualToComparingFieldByField(lawnMower);
	}

	@Test
	void shouldFindNoItemForUnknownId() throws URISyntaxException {
		// Given | Arrange
		final Long unKnownID = 4711L;
		// When | Act
		ResponseEntity<Item> response = restTemplate.getForEntity(BASE_PATH + "/" + unKnownID, Item.class);
		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldBeAbleToDeleteAnItem() throws URISyntaxException {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem(buildLawnMower()).getBody();
		Item fourTyres = givenAnInsertedItem(build4Tires()).getBody();
		// When | Act
		ResponseEntity<Integer> response = restTemplate.exchange(BASE_PATH + "/" + fourTyres.getId(), HttpMethod.DELETE,
				HttpEntity.EMPTY, Integer.class);
		// Then | Assert
		System.out.println("--> Status (Delete): " + response.getStatusCodeValue());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void shouldNotBeAbleToDeleteAnItemWithUnknownId() throws URISyntaxException {
		// Given | Arrange
		final Long unKnownID = 4711L;
		// When | Act
		RequestEntity<String> request = new RequestEntity<>(HttpMethod.DELETE,
				new URI(restTemplate.getRootUri() + BASE_PATH + "/" + unKnownID));
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		// Then | Assert
		System.out.println("--> Status (Delete Unknown ID): " + response.getStatusCodeValue());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldBeAbleToReplaceAnItem() throws URISyntaxException {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem(buildLawnMower()).getBody();
		Item fourTyres = build4Tires();
		// When | Act
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + lawnMower.getId());
		HttpEntity<Item> request = new HttpEntity<Item>(fourTyres, headers);
		fourTyres.setId(lawnMower.getId());
		ResponseEntity<Item> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Item.class);
		// Then | Assert
		System.out.println("--> Request (Replace): " + response.getStatusCodeValue());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldNotBeAbleToReplaceAnItemWithUnknownId() throws URISyntaxException {
		// Given | Arrange
		Item lawnMower = givenAnInsertedItem(buildLawnMower()).getBody();
		final Long unKnownID = 4711L;
		// When | Act
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = new URI(restTemplate.getRootUri() + BASE_PATH + "/" + unKnownID);
		HttpEntity<Item> request = new HttpEntity<Item>(lawnMower, headers);
		ResponseEntity<Item> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Item.class);
		// Then | Assert
		System.out.println("--> Request (Replace, Unknown ID): " + response.getStatusCodeValue());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}


	private Item buildLawnMower() {
//		Item item = Item.builder().name("Lawn mower").amount(1).lastUsed(Date.valueOf("2019-05-01"))
//				.location("Basement").build();
		Item item = new Item();
		item.setName("Lawn mower");
		item.setAmount(1);
		item.setLastUsed(Date.valueOf("2019-05-01"));
		item.setLocation("Basement");
		return item;
	}

	private Item buildLawnTrimmer() {
		Item item = Item.builder().name("Lawn trimmer").amount(1).lastUsed(Date.valueOf("2018-05-01"))
				.location("Basement").build();
		return item;
	}

	private Item build4Tires() {
		Item item = Item.builder().name("Tyres").amount(4).lastUsed(Date.valueOf("2020-05-01")).location("Basement")
				.description("for winter use").build();
		return item;
	}

	private ResponseEntity<Item> givenAnInsertedItem(Item newItem) {
//		Item item = buildLawnMower();
		System.out.println("--> newItem: " + newItem.getName());
		return restTemplate.postForEntity(BASE_PATH, newItem, Item.class);
	}

}
