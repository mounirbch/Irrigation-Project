package com.mongo.projetPFE.produit.Category;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category,String> {
}
