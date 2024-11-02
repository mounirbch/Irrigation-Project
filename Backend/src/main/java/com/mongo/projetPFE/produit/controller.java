package com.mongo.projetPFE.produit;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class controller {
    private final Service ser;
@Autowired
    ProductRepository repo;
String t="hhjj";
    @PostMapping("/tutorials")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product _product = repo.save(new Product(product.getNom(), product.getPrenom()) );
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tutorials1")
    public ResponseEntity<String> save(@RequestBody Product product) {


        return ResponseEntity.ok(ser.save(product));
    }

    @GetMapping("lesProduits")
    public ResponseEntity<List<Product>> findAll(){
        return ResponseEntity.ok(ser.findAll());

    }

    @GetMapping("/{product-id}")
    public ResponseEntity<Product> findById(@PathVariable ("product-id") String productId){

        return  ResponseEntity.ok(ser.finById(productId));

    }
    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> delete(@PathVariable ("product-id") String productId){
        ser.delete(productId);
        return  ResponseEntity.accepted().build();

    }



}
