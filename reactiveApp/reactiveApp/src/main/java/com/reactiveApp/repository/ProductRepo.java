package com.reactiveApp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactiveApp.model.Product;

@Repository
public interface ProductRepo extends ReactiveMongoRepository<Product, String>
{

}
