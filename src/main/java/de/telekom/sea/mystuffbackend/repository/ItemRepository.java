package de.telekom.sea.mystuffbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.telekom.sea.mystuffbackend.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{
	
}
