package de.telekom.sea.mystuffbackend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.telekom.sea.mystuffbackend.entities.Item;
import de.telekom.sea.mystuffbackend.repository.ItemRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ItemRestControllerTest {

	public class ItemList {
		private List<Item> items;
		public ItemList() {
			items = new ArrayList<>();
		}
	}
	
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
	void test() {
//		fail("Not yet implemented");
		System.out.println("--> Test: " + restTemplate);
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
//		ResponseEntity <List<Item>> response = restTemplate.get(BASE_PATH, Item.class);
		ResponseEntity<Item[]> response = restTemplate.getForEntity(BASE_PATH, Item[].class);
//		// Then | Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().length).isEqualTo(3);
		System.out.println("--> Status: " + response.getStatusCodeValue() + ", Länge: " + response.getBody().length);
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

//	@Test
//	void shouldFindNoItemForUnknownId() throws URISyntaxException {
//		fail();
//	}

//	@Test
//	void shouldBeAbleToDeleteAnItem() throws URISyntaxException {
//		fail();
//	}

//	@Test
//	void shouldNotBeAbleToDeleteAnItemWithUnknownId() throws URISyntaxException {
//		fail();
//	}

//	@Test
//	void shouldBeAbleToReplaceAnItem() throws URISyntaxException {
//		fail();
//	}

//	@Test
//	void shouldNotBeAbleToReplaceAnItemWithUnknownId() throws URISyntaxException {
//		fail();
//	}

	
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
		Item item = Item.builder().name("Tyres").amount(4).lastUsed(Date.valueOf("2020-05-01"))
				.location("Basement").description("for winter use").build();
		return item;
	}

	private ResponseEntity<Item> givenAnInsertedItem(Item newItem) {
//		Item item = buildLawnMower();
		System.out.println("--> newItem: " + newItem.getName());
		return restTemplate.postForEntity(BASE_PATH, newItem, Item.class);
	}

}
