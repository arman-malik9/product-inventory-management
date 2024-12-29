package com.inventory.InventoryRepository;
/**
 * implementation of Custom repository.
 * @author arman_a
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.inventory.InventoryModel.ProductInventory;
import reactor.core.publisher.Mono;

public class CustomInventoryRepositoryIMPL implements CustomInventoryRepository {
	
	// for MongoDB interaction
	@Autowired
	private ReactiveMongoTemplate template;
	
	/**
	 * find the inventory 
	 * @param productId
	 * @return product inventory
	 */
	@Override
	public Mono<ProductInventory> findByProductId(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));

		return template.findById(query, ProductInventory.class);
	}
	/**
	 * delete the inventory 
	 * @param productId
	 * @return deleted product inventory
	 */
	@Override
	public Mono<ProductInventory> deleteByProductId(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));

		return template.findAndRemove(query, ProductInventory.class);
	}

}
