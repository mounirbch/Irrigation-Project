package com.mongo.projetPFE.plante_ml;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "moyenne")
public class PlanteModel {
    private double avgTemperature;
    private double avgHumidity;
    private double avgPh;

    public void setFinalTemperature(double avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public void setFinalHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }


    public void setFinalPh(double avgPh) {
        this.avgPh = avgPh;
    }
}
