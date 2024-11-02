package com.mongo.projetPFE.plante_ml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlanteController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/pred_plante")
    public List<PredPlante> getAllPredPlantes() {
        return mongoTemplate.findAll(PredPlante.class);
    }
}
