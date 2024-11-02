package com.mongo.projetPFE.Calendrier;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter

@Document(collection = "quantity")
public class Quantity {
    @Id
    private String id;
    private float valeur;
    private Date timestamp;
}
