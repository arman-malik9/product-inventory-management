package com.reactiveApp.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactiveApp.model.ProductInventoryDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class ProductServiceClient {
	@Autowired
	private WebClient webClient;
	
	public ProductServiceClient(WebClient webClient) {
		// TODO Auto-generated constructor stub
		this.webClient = webClient;
	}
	/**
	 * It create the inventory using webClient
	 * @param productInventoryDTO
	 * @return New created Product zInventory 
	 */
	public Mono<ProductInventoryDTO> createInventory(ProductInventoryDTO productInventoryDTO) {
		return webClient.post().uri("/productInventory").bodyValue(productInventoryDTO).retrieve()
				.bodyToMono(ProductInventoryDTO.class);

	}
	/**
	 * It update the inventory using webClient
	 * @param productInventoryDTO
	 * @return Updated Product Inventory.
	 */
	public Mono<ProductInventoryDTO> updateInventory(ProductInventoryDTO productInventoryDTO) {
		return webClient.put().uri("/productInventory/byProductId/{id}", productInventoryDTO.getProductId())
				.bodyValue(productInventoryDTO).retrieve().bodyToMono(ProductInventoryDTO.class);
	}
	
	/**
	 * It delete the inventory using webClient
	 * @param productId
	 * @return deleted inventory using productId
	 */
	public Mono<ProductInventoryDTO> deleteInventory(String productId) {
		return webClient.delete().uri("/productInventory/deleteInventory/{id}", productId).retrieve()
				.bodyToMono(ProductInventoryDTO.class);
	}
	
	
	/**
	 * It fetch the inventory using webClient
	 * @param NA
	 * @return Product Inventory
	 */
	public Flux<ProductInventoryDTO> getAllInventory(){
		return webClient.get().uri("/productInventory")
						 .retrieve()
						 .bodyToFlux(ProductInventoryDTO.class);
	}
	/**
	 * It Fetch the inventory using productId
	 * @param productId
	 * @return Product Inventory using productId
	 */
	public Mono<ProductInventoryDTO> getInventoryByProductId(String productId){
		return webClient.get()
						.uri("/productInventory/get/{id}", productId)
						.retrieve()
						.bodyToMono(ProductInventoryDTO.class);
	}

}
