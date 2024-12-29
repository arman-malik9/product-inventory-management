package com.inventory.InventoryModel;
/**
 * Product Inventory Model class
 * @author arman_a
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.inventory.Utilities.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "productsInventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//builder Design pattern
//how many ways object creation.
//error and exception handling.
public class ProductInventory {
	@Id
	private String id;
	private int stockCount;
	private Status stockStatus;  //enum string //In Stock , out of stock , available soon
	private String productId;

}
