package com.mongo.projetPFE.Data.data0;

import com.mongo.projetPFE.Data.data0.Datamodel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataRepository extends CrudRepository<Datamodel,String> {
    List<Datamodel> findByTemperatureIsNotNull();
    List<Datamodel> findByHumidityIsNotNull();
    List<Datamodel> findBySoilHumidityIsNotNull();
}
