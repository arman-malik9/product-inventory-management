package com.reactiveApp.model;
/**
 * This DTO class handles the data coming from Inventory Service.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventoryDTO {	//Product Inventory Data Transfer Object
	
	//private String id;
	private int stockCount;
	private String stockStatus;  //enum string //In Stock , out of stock , available soon
	private String productId;
		
}
