package com.reactiveApp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProductInventoryClientConfig {
	
	/**
	 * It create the bean of WebClient 
	 * @param not required
	 * @return webClient object of inventory service which is running on http://localhost:8091
	 */
	@Bean
	public WebClient webClient() {
	  return WebClient.builder().baseUrl("http://localhost:8091")
			 // .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
			  .build();
	}

}
