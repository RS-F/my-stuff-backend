package de.telekom.sea.mystuffbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.telekom.sea.mystuffbackend.entities.Item;
import de.telekom.sea.mystuffbackend.repository.ItemRepository;

@RestController
@RequestMapping("/api/v1/items")
public class ItemRestController {
	
	private final ItemRepository repo;
	
	@Autowired
	public ItemRestController(ItemRepository repo) {
		super();
		this.repo = repo;
	}
	
	@GetMapping
	public List<Item> getAll() {
		return this.repo.findAll();
	}
	
	@PostMapping
	public Item saveItem(@RequestBody Item newItem) {
		return this.repo.save(newItem);
	}
	
	@GetMapping("{id}")
	public Item getById(@PathVariable("id") Long id) {
		return this.repo.findById(id).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		});
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	ResponseEntity<?> deleteTopic(@PathVariable long id) {
		try {
			this.repo.deleteById(id);
			return new ResponseEntity<String>("Data deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Resource not found", HttpStatus.NOT_FOUND);
		}
	}

}
