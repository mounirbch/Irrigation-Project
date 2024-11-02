package com.mongo.projetPFE.Utilisateur.image;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection="Image")
public class EntityImage {
    public EntityImage() {
        super();
    }

    public EntityImage(String name, String type, byte[] picByte, Utilisateur utilisateur) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
        this.utilisateur=utilisateur;
    }
    @Id
    private  String id ;
    private String name;
    private String type;
    private byte[] picByte;
    private Utilisateur utilisateur;
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

}
