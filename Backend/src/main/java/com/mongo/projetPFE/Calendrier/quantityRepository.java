package com.mongo.projetPFE.Calendrier;

import org.springframework.data.repository.CrudRepository;

public interface quantityRepository extends CrudRepository<Quantity,String> {
    Quantity findTopByOrderByTimestampDesc();
}
