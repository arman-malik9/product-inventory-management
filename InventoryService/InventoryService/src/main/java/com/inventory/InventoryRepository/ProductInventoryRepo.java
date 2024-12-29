package com.inventory.InventoryRepository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.inventory.InventoryModel.ProductInventory;

import reactor.core.publisher.Mono;
@Repository
public interface ProductInventoryRepo  extends ReactiveMongoRepository<ProductInventory, String>, CustomInventoryRepository{
	
//	Mono<ProductInventory> findByProductId(String productId);
//	Mono<Void> deleteByProductId(String productId);
}
