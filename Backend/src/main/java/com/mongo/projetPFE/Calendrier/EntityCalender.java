package com.mongo.projetPFE.Calendrier;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="calendrier")
public class EntityCalender {
    String id ;
    String title ;


    Date start;


    Date end;
    Utilisateur utilisateur;
}
