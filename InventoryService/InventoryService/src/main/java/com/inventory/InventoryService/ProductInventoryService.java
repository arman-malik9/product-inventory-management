package com.inventory.InventoryService;

/**
 * Product Inventory services Class which has some necessary methods like create/update/delete/get
 * @author arman_a
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.InventoryModel.ProductInventory;
import com.inventory.InventoryRepository.ProductInventoryRepo;
import com.inventory.Utilities.Status;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductInventoryService {

	@Autowired
	private ProductInventoryRepo productInventoryRepo;

	/**
	 * create the inventory
	 * 
	 * @param productInventory
	 * @return added productInventory
	 */
	public Mono<ProductInventory> createProductInventory(ProductInventory productInventory) {
		if (productInventory.getStockCount() > 0) {
			productInventory = ProductInventory.builder().stockCount(productInventory.getStockCount())
					.stockStatus(Status.IN_STOCK).productId(productInventory.getProductId()).build();
		}
		// productInventory.setStockStatus(Status.IN_STOCK);
		else if (productInventory.getStockCount() == 0) {
			productInventory = ProductInventory.builder().stockCount(productInventory.getStockCount())
					.stockStatus(Status.OUT_OF_STOCK).productId(productInventory.getProductId()).build();
		}
		// productInventory.setStockStatus(Status.OUT_OF_STOCK);
		else {
			productInventory = ProductInventory.builder().stockCount(productInventory.getStockCount())
					.stockStatus(Status.AVAILABLE_SOON).productId(productInventory.getProductId()).build();
		}
		// productInventory.setStockStatus(Status.AVAILABLE_SOON);
		log.info("Product Inventory of productID of" + productInventory.getProductId() + " is created. ");
		return productInventoryRepo.save(productInventory);
	}

	/**
	 * fetch the all entries of product inventory
	 * 
	 * @return ProductInventory
	 */
	public Flux<ProductInventory> getAllProductInventory() {
		// productInventoryRepo.findAll().subscribe(System.out::println);
		return productInventoryRepo.findAll();

	}

	/**
	 * fetch the entries of specific product inventory
	 * 
	 * @param id
	 * @return specific ProductInventory
	 */
	public Mono<ProductInventory> getProductInventory(String id) {
		return productInventoryRepo.findById(id).flatMap(prodInventory -> Mono.just(prodInventory))
				.switchIfEmpty(Mono.empty());

	}

	/**
	 * update ProductInventory using inventory Id
	 * 
	 * @param updateInventory
	 * @param id
	 * @return updated ProductInventory
	 */
	public Mono<ProductInventory> updateProductInventory(ProductInventory updated, String id) {
		return productInventoryRepo.findById(id).flatMap(inventory -> {
			ProductInventory updatedInventory;
			if (updated.getStockCount() > 0) {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.IN_STOCK).productId(inventory.getProductId()).build();
			} else if (updated.getStockCount() == 0) {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.OUT_OF_STOCK).productId(inventory.getProductId()).build();
			}

			else {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.AVAILABLE_SOON).productId(inventory.getProductId()).build();
			}
			log.info("Product Inventory of InventoryId of " + id + " is updated. ");
			return productInventoryRepo.save(updatedInventory).then(Mono.just(updatedInventory));
		}).switchIfEmpty(Mono.empty());

	}

	/**
	 * update ProductInventory using productId
	 * 
	 * @param updated
	 * @param productId
	 * @return updated ProductInventory
	 */
	public Mono<ProductInventory> updateByProductId(ProductInventory updated, String productId) {
		return productInventoryRepo.findByProductId(productId).flatMap(inventory -> {
			ProductInventory updatedInventory;
			System.out.println(inventory.getProductId());
			if (updated.getStockCount() > 0) {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.IN_STOCK).productId(inventory.getProductId()).build();
			} else if (updated.getStockCount() == 0) {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.OUT_OF_STOCK).productId(inventory.getProductId()).build();
			}

			else {
				updatedInventory = ProductInventory.builder().id(inventory.getId()).stockCount(updated.getStockCount())
						.stockStatus(Status.AVAILABLE_SOON).productId(inventory.getProductId()).build();
			}

			log.info("Product Inventory of productID of " + inventory.getProductId() + " is updated. ");
			return productInventoryRepo.save(updatedInventory).then(Mono.just(updatedInventory));
		}).switchIfEmpty(Mono.empty());
	}

	/**
	 * delete the product inventory using inventory id.
	 * 
	 * @param id
	 * @return deleted ProductInventory
	 */
	public Mono<ProductInventory> deleteProductInventory(String id) {
		return productInventoryRepo.findById(id).flatMap(prodInventory -> {
			log.info("Product Inventory of InventoryId of" + id + " is deleted. ");

			return productInventoryRepo.deleteById(id).then(Mono.just(prodInventory));
		}).switchIfEmpty(Mono.empty());

	}

	/**
	 * delete the product inventory using productId
	 * 
	 * @param productId
	 * @return deleted ProductInventory
	 */
	public Mono<ProductInventory> deleteInventoryByProductId(String productId) {
		log.info("Product Inventory of Product Id of" + productId + " is deleted. ");
		return productInventoryRepo.deleteByProductId(productId).switchIfEmpty(Mono.empty());

	}

	/**
	 * fetching the product inventory using product Id
	 * 
	 * @param productId
	 * @return specific ProductInventory
	 */
	public Mono<ProductInventory> getProductInventoryById(String productId) {
		return productInventoryRepo.findByProductId(productId).switchIfEmpty(Mono.empty());
	}
}
