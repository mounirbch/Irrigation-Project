package com.mongo.projetPFE.produit.Category;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="category")
public class Category {
    @Id
    private String id;
    private  String nom;

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Category(String nom) {
        this.nom = nom;
    }
    public Category() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom1() {
        return nom;
    }
}
