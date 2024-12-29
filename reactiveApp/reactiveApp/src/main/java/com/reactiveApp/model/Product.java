package com.reactiveApp.model;
/**
 * Product Model class
 * @author arman_a
 */

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection =  "armanDB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
	@Id
	private String id;
	private String productName;
	//private int qty;
	private double price;
	private String description;
	//private String stockStatus;
	
}
