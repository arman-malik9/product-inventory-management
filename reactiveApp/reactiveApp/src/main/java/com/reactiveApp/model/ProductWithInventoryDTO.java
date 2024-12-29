package com.reactiveApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWithInventoryDTO {
	
	private Product product;
	private int stockCount;
	private String stockStatus;

}
