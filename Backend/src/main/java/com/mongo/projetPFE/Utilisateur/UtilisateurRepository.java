package com.mongo.projetPFE.Utilisateur;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur,String> {

    Optional<Utilisateur> findByEmail(String email);
    long countByActif(boolean actif);

    long countByUserArchive(boolean userArchive);
    long countByGoogleUser(boolean googleUser);}
