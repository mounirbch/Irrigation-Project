package com.mongo.projetPFE.plante_ml;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "pred_plante")
public class PredPlante {
    @Id
    private String id;
    private String crop;
    private double probability;



    public PredPlante(String crop, double probability) {
        this.crop = crop;
        this.probability = probability;

    }

}
