package com.mongo.projetPFE.produit.Category;

import com.mongo.projetPFE.produit.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Category")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    CategoryRepository repository;
    @PostMapping("/tutorials")
    public ResponseEntity<Category> createProduct(@RequestBody Category c1) {
        try {
            Category _c1= repository.save(new Category(c1.getNom1()) );
            return new ResponseEntity<>(c1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
