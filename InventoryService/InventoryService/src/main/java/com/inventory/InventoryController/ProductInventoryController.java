package com.inventory.InventoryController;
/**
 * Handling the end-points of the Inventory services.
 * @author arman_a
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.InventoryModel.ProductInventory;
import com.inventory.InventoryService.ProductInventoryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductInventoryController {
	
	@Autowired
	private ProductInventoryService productInventoryService;
	
	@PostMapping("/productInventory")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductInventory> createProductInventory(@RequestBody ProductInventory productInventory)
	{
		return productInventoryService.createProductInventory(productInventory);
	}
	@GetMapping("/productInventory")
	@ResponseStatus(HttpStatus.OK)
	public Flux<ProductInventory> getAllProductInventory()
	{
		return productInventoryService.getAllProductInventory();
	}
	@DeleteMapping("/productInventory/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> deleteProductInventory(@PathVariable("id") String id)
	{
		return productInventoryService.deleteProductInventory(id);
	}
	
	@DeleteMapping("/productInventory/deleteInventory/{productId}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> deleteInventoryByProductId(@PathVariable("productId") String productId)
	{
		return productInventoryService.deleteInventoryByProductId(productId);
	}
	
	@GetMapping("/productInventory/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> getProductInventory(@PathVariable("id") String id)
	{
		return productInventoryService.getProductInventory(id);
	}
	
	@PutMapping("/productInventory/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductInventory> updateProductInventory(@RequestBody ProductInventory productInventory, @PathVariable("id") String id)
	{
		return productInventoryService.updateProductInventory(productInventory, id);
	}
	
	@PutMapping("/productInventory/byProductId/{productId}")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductInventory> updateByProductId(@RequestBody ProductInventory productInventory, @PathVariable("productId") String productId)
	{
		return productInventoryService.updateByProductId(productInventory, productId);
	}
	
	@GetMapping("/productInventory/get/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> getByProductId(@PathVariable("id") String productId)
	{
		return productInventoryService.getProductInventoryById(productId);
	}
}
