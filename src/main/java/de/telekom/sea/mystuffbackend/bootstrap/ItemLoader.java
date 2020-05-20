package de.telekom.sea.mystuffbackend.bootstrap;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import de.telekom.sea.mystuffbackend.entities.Item;
import de.telekom.sea.mystuffbackend.repository.ItemRepository;

@Component
public class ItemLoader implements ApplicationListener<ContextRefreshedEvent> {

	private ItemRepository itemRepository;
	
	@Autowired
	public void setItemRepository(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		Item item1 = new Item();
		item1.setName("Fahradschlauch");
		item1.setAmount(3);
		item1.setDescription("Größe 24, 26, 28");
		item1.setLocation("Schrank, 2. Fach links");
		item1.setLastUsed(Date.valueOf("2020-05-01"));
	
		itemRepository.save(item1);
		
		Item item2 = new Item();
		item2.setName("Gummistiefel");
		item2.setAmount(2);
		item2.setDescription("Größe 44, 38");
		item2.setLocation("Regal, unten");
		item2.setLastUsed(Date.valueOf("2019-12-18"));
	
		itemRepository.save(item2);
		
		Item item3 = new Item();
		item3.setName("Heckenschere");
		item3.setAmount(1);
		item3.setDescription("elektrisch");
		item3.setLocation("Schrank, unterstes Fach links");
		item3.setLastUsed(Date.valueOf("2019-10-15"));
	
		itemRepository.save(item3);


	}
	
	

}
