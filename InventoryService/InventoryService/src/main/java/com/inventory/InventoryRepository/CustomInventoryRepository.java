package com.inventory.InventoryRepository;

import com.inventory.InventoryModel.ProductInventory;

import reactor.core.publisher.Mono;

public interface CustomInventoryRepository {
	// find the product inventory using product ID
	Mono<ProductInventory> findByProductId(String productId);
	// delete the product inventory using product ID
	Mono<ProductInventory> deleteByProductId(String productId);

}
