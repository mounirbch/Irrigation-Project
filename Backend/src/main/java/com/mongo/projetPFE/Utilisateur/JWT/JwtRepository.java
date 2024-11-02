package com.mongo.projetPFE.Utilisateur.JWT;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt,String> {

    Optional<Jwt> findByValeurAndDesactiveAndExpire(String valeur, boolean desactive, boolean expire);



    //@Query("FROM Jwt j WHERE j.expire = :expire AND j.desactive = :desactive AND j.utilisateur.email = :email")
    Optional<Jwt> findByUtilisateurEmailAndDesactiveAndExpire(String email, boolean desactive, boolean expire);


   // @Query("FROM Jwt j WHERE j.utilisateur.email = :email")
    Stream<Jwt> findByUtilisateurEmail(String email);

    void deleteByExpireAndDesactive(boolean expire, boolean desactive);

}
