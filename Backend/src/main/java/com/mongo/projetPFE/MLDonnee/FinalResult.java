package com.mongo.projetPFE.MLDonnee;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "final")
public class FinalResult {
    private double finalTemperature;
    private double finalHumidity;
    private double finalSoilHumidity;
    private double finalPh;

    public void setFinalTemperature(double finalTemperature) {
        this.finalTemperature = finalTemperature;
    }

    public void setFinalHumidity(double finalHumidity) {
        this.finalHumidity = finalHumidity;
    }

    public void setFinalSoilHumidity(double finalSoilHumidity) {
        this.finalSoilHumidity = finalSoilHumidity;
    }

    public void setFinalPh(double finalPh) {
        this.finalPh = finalPh;
    }
}

