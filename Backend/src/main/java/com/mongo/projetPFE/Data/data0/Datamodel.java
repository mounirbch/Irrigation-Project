package com.mongo.projetPFE.Data.data0;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Document(collection = "iot") //  DATA
public class Datamodel {
    @Id
    private String id;
    private final float temperature;
    private final float humidity;
    private final float soilHumidity;
    private final float ph;

    @Setter
    private Date receivedDate;
    //private LocalDateTime receivedDate; // Utiliser LocalDateTime



    public Datamodel(String id, float temperature, float humidity, float soilHumidity, float ph ) {
        super();
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.soilHumidity = soilHumidity;
        this.ph = ph;
        this.receivedDate = new Date(); // Initialiser la date de réception avec la date actuelle
        //this.receivedDate = LocalDateTime.now(); // Utiliser LocalDateTime
    }


    @Override
    public String toString() {
        return "Datamodel{" +
                "id='" + id + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", soilHumidity=" + soilHumidity +
                ", ph=" + ph +
                ", receivedDate=" + receivedDate +
                '}';
    }





}