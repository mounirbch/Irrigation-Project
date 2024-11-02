package com.mongo.projetPFE.Utilisateur.Validation;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="validation")
public class Validation {
    @Id
    private String id;
    private Instant creation;
    private Instant expiration;
    private Instant Activation;
    private String code;
    private Utilisateur utilisateur;

}
