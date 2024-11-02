package com.mongo.projetPFE.produit;


import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.produit.Category.Category;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Document(collection = "mounir")
@AllArgsConstructor
@Data
@Builder
public class Product {
    @Id
    private String id;

    private String nom;

    private String prenom;
    private List tag;
    private String description;
    private String utilisateurNom;


    @DBRef
   private Category c1;




    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private  LocalDateTime lastModified;
    public Product(){

    }
    public Product(String nom,String prenom){
       this.nom=nom;
       this.prenom=prenom;
    }




    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }


}
