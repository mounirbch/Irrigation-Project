package com.mongo.projetPFE.Utilisateur.JWT;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="jwt")
public class Jwt {
    @Id
    private String id ;
    private  String valeur;
    private boolean desactive;
    private boolean expire;
    private Utilisateur utilisateur;
}
