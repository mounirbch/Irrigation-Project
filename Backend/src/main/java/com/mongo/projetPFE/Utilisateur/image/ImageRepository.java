package com.mongo.projetPFE.Utilisateur.image;

import com.mongo.projetPFE.Calendrier.EntityCalender;
import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.produit.Category.CategoryRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<EntityImage, String> {
    Optional<EntityImage> findByName(String name);

    Optional<EntityImage> findByUtilisateur(Utilisateur utilisateur);
    void deleteByUtilisateur(Utilisateur utilisateur);


}
