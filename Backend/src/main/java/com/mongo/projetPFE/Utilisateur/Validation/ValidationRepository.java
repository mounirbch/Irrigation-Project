package com.mongo.projetPFE.Utilisateur.Validation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends CrudRepository<Validation,String> {
    Optional<Validation>findByCode (String code);
}
