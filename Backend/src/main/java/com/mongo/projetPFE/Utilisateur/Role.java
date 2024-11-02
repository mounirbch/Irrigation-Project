package com.mongo.projetPFE.Utilisateur;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role {
    @Id
    private String id;
    private TypeDeRole libelle;


}
