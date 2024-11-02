package com.mongo.projetPFE.Calendrier;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import jdk.jfr.Event;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository


public interface RepositoryCalender extends CrudRepository<EntityCalender,String> {
    // Recherche par utilisateur
    Iterable<EntityCalender> findByUtilisateur(Utilisateur utilisateur);
    List<EntityCalender> findByUtilisateurId(String userId);

    // Recherche par titre
    Optional<EntityCalender> findByTitle(String title);
    Optional<EntityCalender> findByTitleAndUtilisateur(String title, Utilisateur utilisateur);

    // Recherche par date de début et de fin
    Optional<EntityCalender> findByStart(String start);
    Optional<EntityCalender> findByStartAndEndAndUtilisateur(Date start, Date end, Utilisateur utilisateur);
    Optional<EntityCalender>  findByStartAndEnd(Date start,Date  end);
    // Recherche par date de début ou de fin
    List<EntityCalender> findByEndLessThanEqualAndStartGreaterThanEqual(Date end, Date start);
    List<EntityCalender> findByStartLessThanEqual(Date start);
    List<EntityCalender> findByEndGreaterThanEqual(Date end);

    List<EntityCalender> findByStartLessThanEqualAndIdNot(Date start);
    List<EntityCalender> findByEndGreaterThanEqualAndIdNot(Date end);

    // Recherche avec dates spécifiques et utilisateur
    List<EntityCalender> findByStartLessThanEqualAndUtilisateur(Date start, Utilisateur utilisateur);
    List<EntityCalender> findByEndGreaterThanEqualAndUtilisateur(Date end, Utilisateur utilisateur);

    List<EntityCalender> findAllByStartBetweenAndUtilisateur(Date start, Date end, Utilisateur utilisateur);
    List<EntityCalender> findByStartGreaterThanEqualAndUtilisateur(Date start, Utilisateur utilisateur);
    List<EntityCalender> findByStartBetweenAndUtilisateur(Date start, Date end, Utilisateur utilisateur);
    List<EntityCalender> findByEndBetweenAndUtilisateur(Date start, Date end, Utilisateur utilisateur);
    List<EntityCalender> findByStartLessThanEqualAndEndGreaterThanEqualAndUtilisateur(Date start, Date end, Utilisateur utilisateur);

    // Recherche avec requête personnalisée
    @Query("{$and:[{'end': {$lte: ?0}}, {'start': {$gte: ?1}}]}")
    List<EntityCalender> findEventsByNewEventDates(Date end, Date start);






}
